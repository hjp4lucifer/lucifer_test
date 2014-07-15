package cn.lucifer.demo.file;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ClassRenameWithPackageTest {

	private ClassRename classRename = new ClassRename();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateClassRenameArgs() {
		String[] args = new String[] { "c:/test", "cn.lucifer",
				"AdManager	;  ByteUtils; 	AdReceive;AdServices",
				";	  	Ayte2Dtils;;" };
		String[] newArgs;
		try {
			newArgs = ClassRenameWithPackage.createClassRenameArgs(classRename,
					args);
			for (String arg : newArgs) {
				System.out.println(arg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
