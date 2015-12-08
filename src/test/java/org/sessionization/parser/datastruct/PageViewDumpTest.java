package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Test;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PageViewDumpTest {

	@Test
	public void testOne() throws IOException, CannotCompileException, NotFoundException {
		byte[] bytes = PageViewDump.dump(LogFormats.CommonLogFormat.create(null));
		File file = new File("PageView.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}

}