package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RequestDumpTest {

	@Test
	public void testOne() throws IOException {
		byte[] bytes = ResourceDump.dump(LogFormats.CommonLogFormat.create(null));
		File file = new File("Request.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}

}