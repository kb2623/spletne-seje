package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;

public class UserIdDumpTest {

	@Test
	public void testOne() throws Exception {
		byte[] bytes = UserIdDump.dump(LogFormats.CommonLogFormat.create(null));
		File file = new File("UserId.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}
}