package org.sessionization.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogFormatsTest {

	@Test
	public void testCommonLogFormat() {
		List<LogFieldType> list = LogFormats.CommonLogFormat.make();
		assertEquals(7, list.size());
		assertTrue(list.contains(LogFieldType.RemoteHost));
		assertTrue(list.contains(LogFieldType.DateTime));
		assertTrue(list.contains(LogFieldType.RequestLine));
		assertTrue(list.contains(LogFieldType.RemoteLogname));
		assertTrue(list.contains(LogFieldType.RemoteUser));
		assertTrue(list.contains(LogFieldType.StatusCode));
		assertTrue(list.contains(LogFieldType.SizeOfResponse));
	}

	@Test
	public void testCombinedLogFormat() {
		List<LogFieldType> list = LogFormats.CombinedLogFormat.make();
		assertEquals(9, list.size());
		assertTrue(list.contains(LogFieldType.RemoteHost));
		assertTrue(list.contains(LogFieldType.DateTime));
		assertTrue(list.contains(LogFieldType.RequestLine));
		assertTrue(list.contains(LogFieldType.RemoteLogname));
		assertTrue(list.contains(LogFieldType.RemoteUser));
		assertTrue(list.contains(LogFieldType.StatusCode));
		assertTrue(list.contains(LogFieldType.SizeOfResponse));
		assertTrue(list.contains(LogFieldType.RefererNCSA));
		assertTrue(list.contains(LogFieldType.UserAgentNCSA));
	}

	@Test
	public void testExtendedLogF1() {
		List<LogFieldType> list = LogFormats.ParseCmdArgs.create("#Fields:", "date", "time", "c-ip", "cs-username", "s-ip s-port", "cs-method", "cs-uri-stem", "cs-uri-query", "sc-status", "cs(User-Agent)");
		assertEquals(10, list.size());
		assertTrue(list.contains(LogFieldType.UserAgentNCSA));
	}

	@Test
	public void testExtendedLogF2() {
		List<LogFieldType> list = LogFormats.ParseCmdArgs.create("#Fields:", "date", "time", "c-ip", "cs-username", "s-ip", "s-port", "cs-method", "cs-uri-stem", "cs-uri-query", "sc-status", "sc-bytes", "cs-bytes", "time-taken", "cs(User-Agent)", "cs(Referrer)");
		assertEquals(15, list.size());
		assertTrue(list.contains(LogFieldType.UserAgentNCSA));
		assertTrue(list.contains(LogFieldType.RefererNCSA));
	}

	@Test
	public void tesetCLF1() {
		List<LogFieldType> list = LogFormats.ParseCmdArgs.make("%h %l %u %t %r %s %b %{Referrer}i %{User-agent}i %C".split(" "));
		assertEquals(10, list.size());
		list.forEach(Assert::assertNotNull);
	}

	@Test
	public void testExtendedLogF3() {
		List<LogFieldType> list = LogFormats.ParseCmdArgs.create("#Fields:", "time", "c-ip", "cs-method", "cs-uri-stem", "sc-status");
		assertEquals(5, list.size());
		assertTrue(list.contains(LogFieldType.Time));
		assertTrue(list.contains(LogFieldType.ClientIP));
		assertTrue(list.contains(LogFieldType.Method));
		assertTrue(list.contains(LogFieldType.UriSteam));
		assertTrue(list.contains(LogFieldType.StatusCode));
	}
}