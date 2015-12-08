package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class DumpUserIdTest {

	@Test
	public void testOne() throws Exception {
		List<LogFieldType> list = LogFormats.CommonLogFormat.create(null);
		DumpPageView.dump(list);
		DumpUserSession.dump();
		byte[] bytes = DumpUserId.dump(list);
		File file = new File("UserId.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}
}