package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class DumpUserSessionTest {

	@Test
	public void testDump() throws Exception {
		ClassPoolLoader loader = new ClassPoolLoader();
		List<LogFieldType> list = LogFormats.CommonLogFormat.create(null);
		DumpPageView.dump(list, loader);
		Class c = DumpUserSession.dump(loader);
		assertNotNull(c);
		File file = new File("UserSession.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = loader.getPool().get(DumpUserSession.getName()).toBytecode();
		fos.write(bytes);
		fos.close();
	}
}