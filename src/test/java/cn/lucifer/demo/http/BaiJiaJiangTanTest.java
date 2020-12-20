package cn.lucifer.demo.http;

import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;


import static org.junit.Assert.*;

public class BaiJiaJiangTanTest {

    @Test
    public void downloadVideo() throws Exception {
        BaiJiaJiangTan baiJiaJiangTan = new BaiJiaJiangTan();
        BaiJiaJiangTan.VideoConfig config = baiJiaJiangTan.configList.get(baiJiaJiangTan.configList.size() - 1);

        assertNotNull(config);

        for (String pageUrl : baiJiaJiangTan.getVideoDetailUrlSet(config)) {
            baiJiaJiangTan.downloadVideo(pageUrl, config);
        }

    }
}