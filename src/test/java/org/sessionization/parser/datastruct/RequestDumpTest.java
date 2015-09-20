package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class RequestDumpTest {

	@Test
	public void testOne() throws IOException {
		byte[] bytes = RequestDump.dump(LogFormats.CommonLogFormat.create(null));
		File file = new File("Request.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}

}