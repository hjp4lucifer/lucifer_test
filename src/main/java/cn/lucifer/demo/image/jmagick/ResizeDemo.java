package cn.lucifer.demo.image.jmagick;

import org.junit.Test;

import magick.MagickException;

public class ResizeDemo {

	@Test
	public void testMain() throws MagickException {
		String basePath = "D:/test/b";
		String Path = "/111.jpg";
		String PathS = "/111a.jpg";
		int Ww = 612;
		int Hh = 0;
		// 设置Hh高度为0的时候是按图片的宽度比例缩放图片，设置图片Hh大于0以上就按指定大小图片比例剪切为不变形的缩放图片。
		Jmagick jmagick = new Jmagick();
		jmagick.jwh(basePath, Path, PathS, Ww, Hh);
		System.out.println("ok!");
	}

}
