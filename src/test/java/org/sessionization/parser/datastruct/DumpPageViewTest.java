package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class DumpPageViewTest {

	private ClassPoolLoader loader;
	private List<LogFieldType> allFieldTypes;

	@Before
	public void startUp() {
		loader = new ClassPoolLoader();
	}

	@After
	public void endUp() throws IOException, NotFoundException, CannotCompileException {
		File file = new File("PageView.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = loader.getPool().get(DumpPageView.getName()).toBytecode();
		fos.write(bytes);
		fos.close();
	}

	@Test
	public void testCommon() throws Exception {
		allFieldTypes = LogFormats.CommonLogFormat.create(null);
		assertNotNull(DumpPageView.dump(allFieldTypes, loader));
	}

	@Test
	public void testCombined() throws NotFoundException, CannotCompileException, IOException {
		allFieldTypes = LogFormats.CombinedLogFormat.create(null);
		assertNotNull(DumpPageView.dump(allFieldTypes, loader));
	}
}