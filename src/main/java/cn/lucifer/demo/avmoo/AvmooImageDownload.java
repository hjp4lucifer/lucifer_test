package cn.lucifer.demo.avmoo;

import cn.lucifer.http.HttpSocket;
import cn.lucifer.util.HttpClientHelper;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AvmooImageDownload {

	private final AvmooStar star;
	private final File savePath;

	public AvmooImageDownload(AvmooStar star, File savePath) {
		this.star = star;
		this.savePath = savePath;
	}

	public void downloadAndSave() throws Exception {
		List<String> allMoviePageUrl = star.getAllMoviePageUrl();

		// System.out.println(JSON.toJSONString(allMoviePageUrl));

		List<AvmooMovie> avmooMovieList = Lists.newArrayListWithExpectedSize(allMoviePageUrl.size());

		for (String moviePageUrl : allMoviePageUrl) {
			try {
				System.out.println("movie=" + moviePageUrl);
				AvmooMovie avmooMovie = new AvmooMovie(moviePageUrl);
				avmooMovie.parseUrl();
				avmooMovieList.add(avmooMovie);
			} catch (org.jsoup.HttpStatusException e) {
				if (e.getStatusCode() == 403) {
					System.err.println(e.toString());
				} else {
					throw e;
				}
			}
		}

		for (AvmooMovie avmooMovie : avmooMovieList) {
			saveMovie(savePath, avmooMovie);
		}

//		httpSocket.close();
	}

	private void saveMovie(File savePath, AvmooMovie avmooMovie) throws Exception {
		File folder = new File(savePath, avmooMovie.getId());
		if (!folder.exists()) {
			folder.mkdirs();
		}

		saveReadMe(avmooMovie, folder);

		saveImage(avmooMovie.getCover(), folder, "cover");
		if (null != avmooMovie.getSampleImageList()) {
			int i = 0;
			for (String sample : avmooMovie.getSampleImageList()) {
				saveImage(sample, folder, String.format("sample_%02d", i++));
			}
		}
	}

	private void saveReadMe(AvmooMovie avmooMovie, File folder) throws Exception {
		File saveFile = new File(folder, "ReadMe.txt");
		OutputStream output = new FileOutputStream(saveFile);
		ArrayList<String> lines = Lists.newArrayList(avmooMovie.getTitle());
		lines.add(StringUtils.EMPTY);
		lines.addAll(avmooMovie.getInfoList());
		IOUtils.writeLines(lines, "\r\n", output, "utf-8");
		IOUtils.closeQuietly(output);
		System.out.println("save finish : " + saveFile.getAbsolutePath());
	}

	private void saveImage(String url, File folder, String fn) throws Exception {
		String suffix = StringUtils.substring(url, StringUtils.lastIndexOf(url, '.'));
		File saveFile = new File(folder, fn + suffix);
		if (saveFile.exists()) {
			System.out.println("已存在 = " + saveFile.getAbsolutePath() + " url=" + url);
			return;
		}
//		System.out.println(suffix);
		byte[] data = new byte[0];
		for (int i = 0; i < 3; i++) {
			try {
				data = HttpClientHelper.httpGet(url, null);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				Thread.sleep((long) (Math.random() * 10 + 1000));
			}
		}

		OutputStream output = new FileOutputStream(saveFile);
		IOUtils.write(data, output);
		IOUtils.closeQuietly(output);
		System.out.println(url + "\tsave finish : " + saveFile.getAbsolutePath());

	}
}
