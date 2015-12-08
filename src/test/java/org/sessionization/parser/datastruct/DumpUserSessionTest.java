package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class DumpUserSessionTest {

	@Test
	public void testDump() throws Exception {
		List<LogFieldType> list = LogFormats.CommonLogFormat.create(null);
		DumpPageView.dump(list);
		byte[] bytes = DumpUserSession.dump();
		File file = new File("UserSession.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}
}