/**
 * 
 */
package cn.lucifer.demo.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.junit.Test;

/**
 * @author Lucifer
 * 
 */
public class CoverTest {
	@Test
	public void testMain(String[] args) throws IOException {
		File to = new File("E:/info/11111.jpg");

		File coverFile = new File("E:/info/gif封面_20110807.png");
		BufferedImage coverBufferedImage = ImageIO.read(coverFile);
		File image = new File(
				"C:/Users/Lucifer/Pictures/cosplay/1840034_980x1200_236.jpg");
		BufferedImage bufferedImage = ImageIO.read(image);

		// out
		BufferedImage dest = new BufferedImage(300, 380,
				BufferedImage.TYPE_INT_RGB);

		Image coverSized = coverBufferedImage.getScaledInstance(300, 380,
				Image.SCALE_SMOOTH);
		Image imageSized = bufferedImage.getScaledInstance(240, 320,
				Image.SCALE_SMOOTH);
		Graphics graphics = dest.getGraphics();
		graphics.setColor(new Color(225, 225, 225));
		graphics.fillRect(0, 0, 300, 380);
		graphics.drawImage(imageSized, 31, 31, null);
		graphics.drawImage(coverSized, 0, 0, null);

		String mimeType = "image/jpeg";
		ImageWriter writer = ImageIO.getImageWritersByMIMEType(mimeType).next();
		ImageWriteParam params = writer.getDefaultWriteParam();
		FileImageOutputStream toFs = new FileImageOutputStream(to);
		writer.setOutput(toFs);
		IIOImage image2 = new IIOImage(dest, null, null);
		writer.write(null, image2, params);
		toFs.flush();
		toFs.close();
	}

}
