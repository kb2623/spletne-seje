package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class DumpPageViewTest {

	@Test
	public void testOne() throws IOException, CannotCompileException, NotFoundException {
		ClassPoolLoader loader = new ClassPoolLoader();
		Class aClass = DumpPageView.dump(LogFormats.CommonLogFormat.create(null), loader);
		assertNotNull(aClass);
		File file = new File("PageView.class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] bytes = loader.getPool().get(DumpPageView.getName()).toBytecode();
		fos.write(bytes);
		fos.close();
	}

}