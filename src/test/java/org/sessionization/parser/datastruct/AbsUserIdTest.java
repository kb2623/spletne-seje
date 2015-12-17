package org.sessionization.parser.datastruct;

import org.datastruct.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.AbsWebLogParser;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAWebLogParser;

import java.io.StringReader;
import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AbsUserIdTest {

	private ClassLoader loader;
	private AbsWebLogParser parser;

	@Before
	public void setUp() {
		loader = new ClassPoolLoader();
		parser = new NCSAWebLogParser();
	}

	@Test
	public void testGetKey() throws Exception {
		parser.setFieldType(LogFormats.CommonLogFormat.create(null));
		parser.setPool(new ObjectPool());
		parser.openFile(new StringReader(
				"157.55.39.19 - - [26/Jun/2014:04:44:51 +0200] \"GET /jope-puloverji/moski-pulover-b74-red?limit=18 HTTP/1.1\" 200 9545\n" +
						"157.55.39.19 - - [28/Jun/2014:04:44:51 +0200] \"GET /jope-puloverji/moski-pulover-b74-black?limit=18 HTTP/1.1\" 200 9545"
		));
		ParsedLine line1 = parser.parseLine(), line2 = parser.parseLine();
		DumpRequest.dump(parser.getFieldType(), (ClassPoolLoader) loader);
		DumpPageView.dump((ClassPoolLoader) loader);
		DumpUserSession.dump((ClassPoolLoader) loader);
		DumpUserId.dump(parser.getFieldType(), (ClassPoolLoader) loader);
		Class userId = loader.loadClass(DumpUserId.getName());
		assertNotNull(userId);
		Constructor init = userId.getConstructor(ParsedLine.class);
		AbsUserId u1 = (AbsUserId) init.newInstance(line1);
		assertEquals(line1.getKey(), u1.getKey());
		AbsUserId u2 = (AbsUserId) init.newInstance(line2);
		assertEquals(line2.getKey(), u2.getKey());
		System.out.println(u1.secBetwene(u2));
	}

	@Test
	public void testAddParsedLine() throws Exception {

	}
}