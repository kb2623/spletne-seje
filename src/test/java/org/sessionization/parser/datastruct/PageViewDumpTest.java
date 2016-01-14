package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class PageViewDumpTest extends RequestDumpTest {

	@After
	public void endUp() throws IOException, NotFoundException, CannotCompileException {
		super.endUp();
		File file = new File("PageView.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = super.loader.getPool().get(PageViewDump.getName()).toBytecode();
		fos.write(bytes);
		fos.close();
	}

	@Test
	public void testCommon() throws Exception {
		super.testCommon();
		assertNotNull(PageViewDump.dump(loader));
	}

	@Test
	public void testCombined() throws Exception {
		super.testCombined();
		assertNotNull(PageViewDump.dump(loader));
	}
}