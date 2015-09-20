package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;

public class WebPageRequestDumpTest {

	@Test
	public void testOne() throws Exception {
		byte[] bytes = WebPageRequestDump.dump(LogFormats.CommonLogFormat.create(null));
		File file = new File("WebPageRequest.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}
}