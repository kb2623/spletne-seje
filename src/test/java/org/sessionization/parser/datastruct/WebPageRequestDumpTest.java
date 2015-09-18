package org.sessionization.parser.datastruct;

import org.junit.Test;
import org.sessionization.fields.Field;
import org.sessionization.fields.FieldType;
import org.sessionization.parser.LogFormats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class WebPageRequestDumpTest {

	@Test
	public void testOne() throws IOException {
		System.out.println(WebPageRequestAbs.class.getName().replace(".", "/"));
		byte[] bytes = WebPageRequestDump.dump(LogFormats.CommonLogFormat.create(null));
		System.out.println(bytes.length);
		File file = new File(WebPageRequestDump.getClassTName() + ".class");
		file.delete();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close();
	}
}