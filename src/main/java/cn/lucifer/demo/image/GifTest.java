/**
 * 
 */
package cn.lucifer.demo.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

import cn.lucifer.image.gif.AnimatedGifEncoder;
import cn.lucifer.util.ImageUtils;

/**
 * @author Lucifer
 * 
 */
public class GifTest {
	@Test
	public void testMain() {
		File directory = new File(
				"C:/Users/Lucifer/Pictures/2011上海车展/世爵车模 thais");
		if (!directory.isDirectory()) {
			System.err.println("not a directory !");
			return;
		}
		File[] images = directory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().indexOf(".jpg") > 0
						|| pathname.getName().indexOf(".jpeg") > 0
						|| pathname.getName().indexOf(".png") > 0) {
					return true;
				}
				return false;
			}
		});

		long start = System.currentTimeMillis(), end;

		AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
		int delay = 300;
		animatedGifEncoder.setRepeat(0);
		animatedGifEncoder.start("D:/test/demo.gif");
		try {
			for (File image : images) {
				BufferedImage bufferedImage = ImageIO.read(image);
				animatedGifEncoder.setDelay(delay); // 1 frame per sec
				animatedGifEncoder.addFrame(bufferedImage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		animatedGifEncoder.finish();

		end = System.currentTimeMillis();
		System.out.println(String.format("used time : %d", (end - start)));
	}

	@Test
	public void testDecodeGif() throws IOException {
		String path = "C:/test/111/f216f98d7d5a82a73bee7bb90bdb81ba_stamp.gif";
		BufferedImage image = ImageIO.read(new File(path));
		System.out.println(String.format("w=%d, h=%d", image.getWidth(),
				image.getHeight()));
		File to = new File("C:/test/111/out.jpg");
		to.delete();
		ImageUtils.crop(image, to , 0, 0, image.getWidth(), image.getHeight());
	}
}
