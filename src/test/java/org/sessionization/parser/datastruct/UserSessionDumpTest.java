package org.sessionization.parser.datastruct;

import javassist.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogFieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserSessionDumpTest {

	private ClassPoolLoader loader;
	private List<LogFieldType> allFieldTypes;

	private PageViewDumpTest viewDumpTest = new PageViewDumpTest();
	private UserIdDumpTest idDumpTest = new UserIdDumpTest();

	@Before
	public void startUp() {
		loader = new ClassPoolLoader();
		viewDumpTest.setLoader(loader);
		idDumpTest.setLoader(loader);
	}

	@After
	public void endUp() throws Exception {
		viewDumpTest.endUp();
		idDumpTest.endUp();
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
		viewDumpTest.testCommon();
		idDumpTest.testCommon();
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
		viewDumpTest.testCommon();
		assertNotNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testNoPageView() throws Exception {
		allFieldTypes = LogFormats.CommonLogFormat.make();
		idDumpTest.testCommon();
		assertNotNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testCombined() throws Exception {
		allFieldTypes = LogFormats.CombinedLogFormat.make();
		viewDumpTest.testCombined();
		idDumpTest.testCombined();
		assertNotNull(UserSessionDump.dump(loader));
	}
}