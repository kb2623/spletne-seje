package org.sessionization.parser.datastruct;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.datastruct.concurrent.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.ClassPoolLoader;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAWebLogParser;
import org.sessionization.parser.WebLogParser;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
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
		assertEquals("Address{id=null, serverAddress=false, address=/157.55.39.19} RemoteLogname{id=null, logname='-'} RemoteUser{id=null, user='-'} [PageViewAbs{id=null, requests=[DateTime{date=2014-06-26, time=04:44:51} RequestLine{id=null, method=Method{GET}, steamQuery=UriSteamQuery{id=null, uriSteam=UriSteam{id=null, file='/jope-puloverji/moski-pulover-b74-red'}, query=UriQuery{id=null, pairs=[UriQueryPair{id=null, value='18', key=UriQueryKey{id=null, name='limit'}}]}}, protocol=Protocol{id=null, protocol='HTTP', version=1.1}} StatusCode{status=200} SizeOfResponse{size=9545}]}]", uS1.toString());
		assertNotNull(uS2);
		assertEquals("Address{id=null, serverAddress=false, address=/157.55.39.19} RemoteLogname{id=null, logname='-'} RemoteUser{id=null, user='-'} [PageViewAbs{id=null, requests=[DateTime{date=2014-06-28, time=04:44:51} RequestLine{id=null, method=Method{GET}, steamQuery=UriSteamQuery{id=null, uriSteam=UriSteam{id=null, file='/jope-puloverji/gtk-3.0.css'}, query=UriQuery{id=null, pairs=[UriQueryPair{id=null, value=' ', key=UriQueryKey{id=null, name=' '}}]}}, protocol=Protocol{id=null, protocol='HTTP', version=1.1}} StatusCode{status=200} SizeOfResponse{size=9545}]}]", uS2.toString());
		assertEquals(uS1.getKey(), uS2.getKey());
		assertEquals(LocalDateTime.of(2014, 6, 26, 4, 44, 51), uS1.getLocalDateTime());
		assertEquals(LocalDateTime.of(2014, 6, 28, 4, 44, 51), uS2.getLocalDateTime());
		assertEquals(172800, uS2.secBetwene(uS1));
		assertEquals(172800, uS1.secBetwene(uS2));
		/** Vecji minus manjsi */
		assertEquals(172800, uS2.minus(uS1));
		/** Manjsi minus vecji */
		assertEquals(-172800, uS1.minus(uS2));
	}

	@Test
	public void testAddParsedLine() throws Exception {
		ParsedLine l1 = parser.parseLine(), l2 = parser.parseLine(), l3 = parser.parseLine();
		assertFalse(l1.isWebPageResource());
		assertTrue(l2.isWebPageResource());
		assertFalse(l3.isWebPageResource());
		Class session = loader.loadClass(UserSessionDump.getName());
		assertNotNull(session);
		Constructor init = session.getConstructor(ParsedLine.class);
		UserSessionAbs uSession = (UserSessionAbs) init.newInstance(l1);
		assertNotNull(uSession);
		assertTrue(uSession.addParsedLine(l2));
		assertTrue(uSession.addParsedLine(l3));
		assertEquals("Address{id=null, serverAddress=false, address=/157.55.39.19} RemoteLogname{id=null, logname='-'} RemoteUser{id=null, user='-'} [PageViewAbs{id=null, requests=[DateTime{date=2014-06-26, time=04:44:51} RequestLine{id=null, method=Method{GET}, steamQuery=UriSteamQuery{id=null, uriSteam=UriSteam{id=null, file='/jope-puloverji/moski-pulover-b74-red'}, query=UriQuery{id=null, pairs=[UriQueryPair{id=null, value='18', key=UriQueryKey{id=null, name='limit'}}]}}, protocol=Protocol{id=null, protocol='HTTP', version=1.1}} StatusCode{status=200} SizeOfResponse{size=9545}, DateTime{date=2014-06-28, time=04:44:51} RequestLine{id=null, method=Method{GET}, steamQuery=UriSteamQuery{id=null, uriSteam=UriSteam{id=null, file='/jope-puloverji/gtk-3.0.css'}, query=UriQuery{id=null, pairs=[UriQueryPair{id=null, value=' ', key=UriQueryKey{id=null, name=' '}}]}}, protocol=Protocol{id=null, protocol='HTTP', version=1.1}} StatusCode{status=200} SizeOfResponse{size=9545}]}, PageViewAbs{id=null, requests=[DateTime{date=2014-06-28, time=04:46:23} RequestLine{id=null, method=Method{GET}, steamQuery=UriSteamQuery{id=null, uriSteam=UriSteam{id=null, file='/jope-puloverji/moski-pulover-b74-red'}, query=UriQuery{id=null, pairs=[UriQueryPair{id=null, value='18', key=UriQueryKey{id=null, name='limit'}}]}}, protocol=Protocol{id=null, protocol='HTTP', version=1.1}} StatusCode{status=200} SizeOfResponse{size=9545}]}]", uSession.toString());
	}
}