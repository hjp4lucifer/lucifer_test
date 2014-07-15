package cn.lucifer.demo.itext;

import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TableTest {

	/** The resulting PDF file. */
	public final String RESULT = "results/table_test.pdf";

	@Test
	public void testMain() throws IOException, DocumentException {
		// step 1
		Document document = new Document(PageSize.A4.rotate());
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		// step 3
		document.open();
		// step 4

		document.add(new Phrase("This is table test !!!"));

		PdfPTable table = new TableTest().createTalbe();
		document.add(table);

		// step 5
		document.close();

	}

	public PdfPTable createTalbe() {
		// Create a table with 7 columns
		PdfPTable table = new PdfPTable(new float[] { 2, 1, 2, 5, 1, 3, 2 });
		table.setWidthPercentage(100f);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setUseDescender(true);

		// Add the first header row
		Font f = new Font();
		f.setColor(BaseColor.WHITE);
		PdfPCell cell = new PdfPCell(new Phrase("this is title ", f));
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(7);
		table.addCell(cell);

		// Add the second header row twice
		table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
		for (int i = 0; i < 1; i++) {
			table.addCell("Location");
			table.addCell("Time");
			table.addCell("Run Length");
			table.addCell("Title");
			table.addCell("Year");
			table.addCell("Directors");
			table.addCell("Countries");
		}

		table.getDefaultCell().setBackgroundColor(null);
		// There are three special rows
		table.setHeaderRows(3);
		// One of them is a footer
		// table.setFooterRows(1);

		for (int i = 0; i < 300; i++) {
			for (int j = 0; j < 7; j++) {
				table.addCell(i + " x " + j);
			}
		}

		return table;
	}
}
