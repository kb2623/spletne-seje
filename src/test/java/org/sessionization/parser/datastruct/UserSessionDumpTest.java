package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogFieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserSessionDumpTest {

	private ClassPoolLoader loader;
	private List<LogFieldType> allFieldTypes;

	@Before
	public void startUp() {
		loader = new ClassPoolLoader();
	}

	@After
	public void endUp() throws IOException, NotFoundException, CannotCompileException {
		try {
			byte[] bytes = loader.getPool().get(UserSessionDump.getName()).toBytecode();
			File file = new File("UserSession.class");
			file.delete();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		} catch (NotFoundException e) {
		}
	}

	@Test
	public void testCommon() throws Exception {
		allFieldTypes = LogFormats.CommonLogFormat.make();
		assertNotNull(RequestDump.dump(allFieldTypes, loader));
		assertNotNull(PageViewDump.dump(loader));
		assertNotNull(UserIdDump.dump(allFieldTypes, loader));
		assertNotNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testNoUserIdNoPageView() throws Exception {
		allFieldTypes = LogFormats.CommonLogFormat.make();
		assertNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testNoUserId() throws Exception {
		allFieldTypes = LogFormats.CommonLogFormat.make();
		assertNotNull(RequestDump.dump(allFieldTypes, loader));
		assertNotNull(PageViewDump.dump(loader));
		assertNotNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testNoPageView() throws Exception {
		allFieldTypes = LogFormats.CommonLogFormat.make();
		assertNotNull(UserIdDump.dump(allFieldTypes, loader));
		assertNotNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testCombined() throws Exception {
		allFieldTypes = LogFormats.CombinedLogFormat.make();
		assertNotNull(RequestDump.dump(allFieldTypes, loader));
		assertNotNull(PageViewDump.dump(loader));
		assertNotNull(UserIdDump.dump(allFieldTypes, loader));
		assertNotNull(UserSessionDump.dump(loader));
	}
}