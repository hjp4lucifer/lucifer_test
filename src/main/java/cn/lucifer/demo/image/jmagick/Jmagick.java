package cn.lucifer.demo.image.jmagick;

import java.awt.Dimension;
import java.awt.Rectangle;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

public class Jmagick {

	public void jwh(String URLPath, String srcImage, String DestImage,
			int WLen, int HLen) throws MagickException {
		MagickImage scaled = null;
		Rectangle rect = null;
		int x = 0;
		int y = 0;
		int lw = 0;
		int lh = 0;
		ImageInfo info = new ImageInfo(URLPath + srcImage);
		MagickImage image = new MagickImage(info);
		// 取长宽
		Dimension dim = image.getDimension();
		double wImage = dim.getWidth();
		double hImage = dim.getHeight();
		System.out.println(wImage + "," + hImage);
		if (HLen == 0) { // 正常缩小
			Boolean bWBig = wImage > hImage ? true : false;
			if (bWBig) {// 长大过高
				hImage = WLen * (hImage / wImage);
				wImage = WLen;
			} else {// 反之
				wImage = WLen * (wImage / hImage);
				hImage = WLen;
			}
		} else {// 剪切缩小
			Boolean bWBig = wImage - WLen < hImage - HLen ? true : false;
			if (bWBig) {// 长大过高
				hImage = WLen * (hImage / wImage);
				wImage = WLen;
			} else {// 反之
				wImage = HLen * (wImage / hImage);
				hImage = HLen;
			}
		}
		lw = (int) wImage;
		lh = (int) hImage;
		// 输出
		scaled = image.scaleImage(lw, lh);
		if (HLen > 0) {// 剪切缩小必须是ＪＰＧ格式
			x = (lw - WLen) / 2;
			y = (lh - HLen) / 2;
			lw = lw - (lw - WLen);
			lh = lh - (lh - HLen);
			rect = new Rectangle(x, y, lw, lh);
			scaled = scaled.cropImage(rect);
		}
		scaled.setFileName(URLPath + DestImage);
		scaled.writeImage(info);
		scaled.destroyImages();
		scaled.destroyImages();

	}

}
