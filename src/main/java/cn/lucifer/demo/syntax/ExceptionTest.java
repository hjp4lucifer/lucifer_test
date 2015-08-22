package cn.lucifer.demo.syntax;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.log4j.DefaultThrowableRenderer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExceptionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		try {
			System.out.println(1);
			throw new Exception("aaaa");
		} catch (Exception e) {
			System.out.println(2);
			throw e;
		} finally {
			System.out.println(3);
		}
	}

	@Test
	public void testPrintThrowable(){
		Exception e = new Exception();
		ArrayList<String> list = render(e);
		for (String s : list) {
			System.out.println(s);
		}
	}
	
	/**
	 * @see DefaultThrowableRenderer#render(Throwable)
	 * @param throwable
	 * @return
	 */
    public ArrayList<String> render(final Throwable throwable) {
        StringWriter sw = getWriter(throwable);
        LineNumberReader reader = new LineNumberReader(
                new StringReader(sw.toString()));
        ArrayList<String> lines = new ArrayList<>();
        try {
          String line = reader.readLine();
          while(line != null) {
            lines.add(line);
            line = reader.readLine();
          }
        } catch(IOException ex) {
            if (ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            lines.add(ex.toString());
        }
        return lines;
    }
    
    @Test
    public void testGetWriter(){
		Exception e = new Exception();
		System.out.println(getWriter(e).toString());
    }

	protected StringWriter getWriter(final Throwable throwable) {
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
        } catch(RuntimeException ex) {
        }
        pw.flush();
		return sw;
	}
}
