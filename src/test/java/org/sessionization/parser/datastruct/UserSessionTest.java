package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.datastruct.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.WebLogParser;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAWebLogParser;
import sun.util.resources.LocaleData;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class UserSessionTest {

	private ClassLoader loader;
	private WebLogParser parser;

	@Before
	public void setUp() throws IOException, CannotCompileException, NotFoundException {
		loader = new ClassPoolLoader();
		parser = new NCSAWebLogParser();
		ObjectPool pool = new ObjectPool();
		pool.setLoader(loader);
		parser.setPool(pool);
		parser.setFieldType(LogFormats.CommonLogFormat.make());
		parser.openFile(new StringReader(
				"157.55.39.19 - - [26/Jun/2014:04:44:51 +0200] \"GET /jope-puloverji/moski-pulover-b74-red?limit=18 HTTP/1.1\" 200 9545\n" +
						"157.55.39.19 - - [28/Jun/2014:04:44:51 +0200] \"GET /jope-puloverji/gtk-3.0.css HTTP/1.1\" 200 9545\n" +
						"157.55.39.19 - - [28/Jun/2014:04:46:23 +0200] \"GET /jope-puloverji/moski-pulover-b74-red?limit=18 HTTP/1.1\" 200 9545"
		));
		initClasses((ClassPoolLoader) loader);
	}

	private void initClasses(ClassPoolLoader loader) throws NotFoundException, CannotCompileException, IOException {
		assertNotNull(RequestDump.dump(parser.getFieldType(), loader));
		assertNotNull(PageViewDump.dump(loader));
		assertNotNull(UserIdDump.dump(parser.getFieldType(), loader));
		assertNotNull(UserSessionDump.dump(loader));
	}

	@Test
	public void testGetKey() throws Exception {
		ParsedLine line1 = parser.parseLine(), line2 = parser.parseLine();
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs u1 = (UserSessionAbs) init.newInstance(line1);
		assertEquals(line1.getKey(), u1.getKey());
		UserSessionAbs u2 = (UserSessionAbs) init.newInstance(line2);
		assertEquals(line2.getKey(), u2.getKey());
		assertEquals(u1.getKey(), u2.getKey());
	}

	@Test
	public void testGetLocalDate() throws Exception {
		ParsedLine line = parser.parseLine();
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs uSession = (UserSessionAbs) init.newInstance(line);
		assertEquals(LocalDate.of(2014, 6, 26), uSession.getLocalDate());
	}

	@Test
	public void testGetLocalTime() throws Exception {
		ParsedLine line = parser.parseLine();
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs uSession = (UserSessionAbs) init.newInstance(line);
		assertEquals(LocalTime.of(4, 44, 51), uSession.getLocalTime());
	}

	@Test
	public void testGetLocalDateTime() throws Exception {
		ParsedLine line = parser.parseLine();
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs uSession = (UserSessionAbs) init.newInstance(line);
		assertEquals(LocalDateTime.of(2014, 6, 26, 4, 44, 51), uSession.getLocalDateTime());
	}

	@Test
	public void testTimePoint() throws Exception {
		ParsedLine l1 = parser.parseLine(), l2 = parser.parseLine();
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs uS1 = (UserSessionAbs) init.newInstance(l1), uS2 = (UserSessionAbs) init.newInstance(l2);
		assertNotNull(uS1);
		assertEquals("Client 157.55.39.19 - - [[04:44:51 26.06.2014 GET /jope-puloverji/moski-pulover-b74-red[[limit = 18]] HTTP/1.1 200 9545]]", uS1.toString());
		assertNotNull(uS2);
		assertEquals("Client 157.55.39.19 - - [[04:44:51 28.06.2014 GET /jope-puloverji/gtk-3.0.css HTTP/1.1 200 9545]]", uS2.toString());
		assertEquals(uS1.getKey(), uS2.getKey());
		assertEquals(LocalDateTime.of(2014, 6, 26, 4, 44, 51), uS1.getLocalDateTime());
		assertEquals(LocalDateTime.of(2014, 6, 28, 4, 44, 51), uS2.getLocalDateTime());
		assertEquals(172800, uS2.secBetwene(uS1));
		assertEquals(172800, uS1.secBetwene(uS2));
	}

	@Test
	public void testAddParsedLine() throws Exception {
		ParsedLine l1 = parser.parseLine(), l2 = parser.parseLine(), l3 = parser.parseLine();
		assertFalse(l1.isResource());
		assertTrue(l2.isResource());
		assertFalse(l3.isResource());
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs uSession = (UserSessionAbs) init.newInstance(l1);
		assertNotNull(uSession);
		assertTrue(uSession.addParsedLine(l2));
		assertTrue(uSession.addParsedLine(l3));
		assertEquals("Client 157.55.39.19 - - [[04:44:51 26.06.2014 GET /jope-puloverji/moski-pulover-b74-red[[limit = 18]] HTTP/1.1 200 9545, 04:44:51 28.06.2014 GET /jope-puloverji/gtk-3.0.css HTTP/1.1 200 9545], [04:46:23 28.06.2014 GET /jope-puloverji/moski-pulover-b74-red[[limit = 18]] HTTP/1.1 200 9545]]", uSession.toString());
	}
}