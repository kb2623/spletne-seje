package org.sessionization.parser.datastruct;

import org.datastruct.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.AWebLogParser;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAWebLogParser;

import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class AUserIdTest {

	private ClassLoader loader;
	private AWebLogParser parser;

	@Before
	public void setUp() {
		loader = new ClassPoolLoader();
		parser = new NCSAWebLogParser();
	}

	@Test
	public void testGetKey() throws Exception {
		parser.setFieldType(LogFormats.CommonLogFormat.make());
		parser.setPool(new ObjectPool());
		parser.openFile(new StringReader(
				"157.55.39.19 - - [26/Jun/2014:04:44:51 +0200] \"GET /jope-puloverji/moski-pulover-b74-red?limit=18 HTTP/1.1\" 200 9545\n" +
						"157.55.39.19 - - [28/Jun/2014:04:44:51 +0200] \"GET /jope-puloverji/moski-pulover-b74-black?limit=18 HTTP/1.1\" 200 9545"
		));
		ParsedLine line1 = parser.parseLine(), line2 = parser.parseLine();
		DRequest.dump(parser.getFieldType(), (ClassPoolLoader) loader);
		DPageView.dump((ClassPoolLoader) loader);
		DUserSession.dump((ClassPoolLoader) loader);
		DUserId.dump(parser.getFieldType(), (ClassPoolLoader) loader);
		Class userId = loader.loadClass(DUserId.getName());
		assertNotNull(userId);
		Constructor init = userId.getConstructor(ParsedLine.class);
		AUserId u1 = (AUserId) init.newInstance(line1);
		assertEquals(line1.getKey(), u1.getKey());
		AUserId u2 = (AUserId) init.newInstance(line2);
		assertEquals(line2.getKey(), u2.getKey());
		assertEquals(172800, u1.secBetwene(u2));
		assertFalse(u1.getLocalDate().equals(LocalDate.MIN));
	}

	@Test
	public void testAddParsedLine() throws Exception {

	}
}