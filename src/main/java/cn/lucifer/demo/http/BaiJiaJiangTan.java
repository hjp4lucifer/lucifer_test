package cn.lucifer.demo.http;

import cn.lucifer.http.HttpClientException;
import cn.lucifer.http.HttpHelper;
import cn.lucifer.util.ConfigUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 百家讲坛·国史通鉴
 *
 * @author Lucifer
 */
public class BaiJiaJiangTan {

    private static final int connectTimeout = 100000;

    private static final String DOWNLOAD_2_BASE_FOLDER = "E:\\百家讲坛·国史通鉴";

    protected List<VideoConfig> configList;

    public BaiJiaJiangTan() throws IOException {
        String videoConfig = ConfigUtils.loadStrResource(BaiJiaJiangTan.class, "BaiJiaJiangTan/videoConfig.json");
        System.out.println(videoConfig);
        configList = JSON.parseArray(videoConfig, VideoConfig.class);
    }

    /**
     * 解析视频列表->详情页的url
     *
     * @param config
     * @return
     * @throws Exception
     */
    public Set<String> getVideoDetailUrlSet(VideoConfig config) throws Exception {
        Document homeDoc = Jsoup.parse(new URL(config.home), 5000);
        Elements videoDetailUrlList = homeDoc.select("div.tp1 a");

        Set<String> videoDetailUrlSet = new HashSet<>();
        for (Element element : videoDetailUrlList) {
            videoDetailUrlSet.add(element.attr("href").trim());
        }
        return videoDetailUrlSet;
    }

    /**
     * 解析视频详情页, 并下载视频
     *
     * @param pageUrl 视频详情页url
     * @param config
     * @throws Exception
     */
    public void downloadVideo(String pageUrl, VideoConfig config) throws Exception {

        String pageSource = new String(HttpHelper.httpGet(pageUrl, connectTimeout));

        String videoCode = getVideoCode(pageSource, config);
        String title = StringUtils.substringBetween(pageSource, "<!--repaste.title.begin-->", "<!--repaste.title.end-->");
        title = StringUtils.removeStart(title, "《百家讲坛》").trim();

        if (StringUtils.isBlank(videoCode)) {
            throw new Exception("videoCode解析失败, pageUrl = " + pageUrl);
        }
        System.out.println("准备下载：《" + title + "》, videoCode=" + videoCode);

        File folder = new File(DOWNLOAD_2_BASE_FOLDER, title);
        if (!folder.exists()) {
            folder.mkdir();
        }

        for (int i = 0; i <= 1000; i++) {
            String videoUrl = String.format(config.videoUrlFormat, videoCode, i);
            System.out.println("准备下载：" + videoUrl);

            File saveFile = new File(folder, i + ".ts");
            if (saveFile.exists() && saveFile.length() > 4 * 1024) {
                System.out.println(videoUrl + " 文件存在，跳过下载!");
                continue;
            }
            byte[] videoData;
            try {
                videoData = HttpHelper.httpGet(videoUrl, connectTimeout);
            } catch (HttpClientException e) {
                if (404 == e.getHttpStatus()) {
                    System.out.println("---------------------------------");
                    System.out.println("404, 跳过本次下载");
                    System.out.println("---------------------------------");
                    return;
                }
                if (403 == e.getHttpStatus()) {
                    System.out.println("---------------------------------");
                    System.out.println("403, 跳过本次下载");
                    System.out.println("---------------------------------");
                    return;
                }
                throw e;
            }
            FileUtils.writeByteArrayToFile(saveFile, videoData);
        }
    }

    /**
     * 获取videoCode
     *
     * @param pageSource
     * @param config
     * @return
     */
    private String getVideoCode(String pageSource, VideoConfig config) {
        switch (config.parseVideoCodeMode) {
            case 1:
                return StringUtils.substringBetween(pageSource, "<!--repaste.video.code.begin-->", "<!--repaste.video.code.end-->");
            case 2:
                return StringUtils.substringBetween(pageSource, "var guid = \"", "\";");
            default:
                return null;
        }
    }

    public static class VideoConfig {
        public String title;
        public String home;
        public String videoUrlFormat;
        public int parseVideoCodeMode;
    }
}
