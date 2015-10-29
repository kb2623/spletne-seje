package org.sessionization.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.fields.LogFieldType;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

@SuppressWarnings("deprecation")
public class NCSAParserTest {

	private String pathNCSACombined;
	private String pathNCSACommon;
	private NCSAParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new NCSAParser();
		pathNCSACombined = ClassLoader.getSystemResource("access_log").getFile();
		pathNCSACommon = ClassLoader.getSystemResource("logCommon").getFile();
	}

	@Test
	public void testNCSAParserCommon() {
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
			for (ParsedLine list : parser) list.forEach(Assert::assertNotNull);
			parser.closeFile();
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCommonTryResource() throws NullPointerException, IOException {
		try (NCSAParser parser1 = new NCSAParser()) {
			parser1.openFile(new File[]{new File(pathNCSACommon)});
					parser1.setFieldType(LogFormats.CommonLogFormat.create(null));
			for (ParsedLine list : parser1) list.forEach(Assert::assertNotNull);
		}
	}

	@Test
	public void testNCSAParserCommonForEach() {
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
			parser.forEach(line -> line.forEach(Assert::assertNotNull));
			parser.closeFile();
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCombinedForEachWithSetFormat() {
		try {
			parser.openFile(new File[]{new File(pathNCSACombined)});
			parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
			parser.forEach(line -> line.forEach(Assert::assertNotNull));
			parser.closeFile();
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCombinedWithSetFormat() {
		try {
			parser.openFile(new File[]{new File(pathNCSACombined)});
			parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
			parser.setDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
			for (ParsedLine list : parser) list.forEach(Assert::assertNotNull);
			parser.closeFile();
		} catch(NullPointerException | IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testNCSAParserCombinedForEach() {
		try {
			parser.openFile(new File[]{new File(pathNCSACombined)});
			parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
			parser.forEach(line -> line.forEach(Assert::assertNotNull));
			parser.closeFile();
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCombined() {
		try {
			parser.openFile(new File[]{new File(pathNCSACombined)});
			parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
			for (ParsedLine list : parser) {
				list.forEach(Assert::assertNotNull);
			}
			parser.closeFile();
		} catch(NullPointerException | IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testNCSAParserCombinedTryResource() {
		try (NCSAParser parser1 = new NCSAParser()) {
			parser1.openFile(new File[]{new File(pathNCSACombined)});
			parser1.setFieldType(LogFormats.CombinedLogFormat.create(null));
			parser1.forEach(Assert::assertNotNull);
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCombinedCookieTryResource() {
		String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
		try (NCSAParser parser1 = new NCSAParser()) {
			parser1.openFile(new StringReader(testNiz));
			String[] cookie = "%h %l %u %t %r %>s %b %{Referer}i %{User-agent}i %C".split(" ");
			parser1.setFieldType(LogFormats.CustomLogFormat.create(cookie));
			ParsedLine line = parser1.parseLine();
			assertEquals("[216.67.1.91 | - | leon | 2002-07-01T12:11:52 | GET /index.html HTTP/1.1 | 200 | 431 | www.loganalyzer.net/ | Mozilla/4.05 en (WinNT; I) | [[USERID = CustomerA][IMPID = 01234]]]", line.toString());
			assertFalse(line.isResource());

		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCombinedWithCookie() {
		String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
		try {
			parser.openFile(new StringReader(testNiz));
			String[] cookie = "%h %l %u %t %r %>s %b %{Referer}i %{User-agent}i %C".split(" ");
			List<LogFieldType> listType = LogFormats.CustomLogFormat.create(cookie);
			parser.setFieldType(listType);
			ParsedLine list = parser.parseLine();
			assertEquals("[216.67.1.91 | - | leon | 2002-07-01T12:11:52 | GET /index.html HTTP/1.1 | 200 | 431 | www.loganalyzer.net/ | Mozilla/4.05 en (WinNT; I) | [[USERID = CustomerA][IMPID = 01234]]]", list.toString());
			parser.closeFile();
		} catch(NullPointerException | ParseException | IOException e) {
			e.printStackTrace();
			fail();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}