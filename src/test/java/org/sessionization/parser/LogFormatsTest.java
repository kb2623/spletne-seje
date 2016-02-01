package org.sessionization.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogFormatsTest {

	@Test
	public void testCommonLogFormat() {
		List<LogFieldTypeImp> list = LogFormats.CommonLogFormat.make();
		assertEquals(7, list.size());
		assertTrue(list.contains(LogFieldTypeImp.RemoteHost));
		assertTrue(list.contains(LogFieldTypeImp.DateTime));
		assertTrue(list.contains(LogFieldTypeImp.RequestLine));
		assertTrue(list.contains(LogFieldTypeImp.RemoteLogname));
		assertTrue(list.contains(LogFieldTypeImp.RemoteUser));
		assertTrue(list.contains(LogFieldTypeImp.StatusCode));
		assertTrue(list.contains(LogFieldTypeImp.SizeOfResponse));
	}

	@Test
	public void testCombinedLogFormat() {
		List<LogFieldTypeImp> list = LogFormats.CombinedLogFormat.make();
		assertEquals(9, list.size());
		assertTrue(list.contains(LogFieldTypeImp.RemoteHost));
		assertTrue(list.contains(LogFieldTypeImp.DateTime));
		assertTrue(list.contains(LogFieldTypeImp.RequestLine));
		assertTrue(list.contains(LogFieldTypeImp.RemoteLogname));
		assertTrue(list.contains(LogFieldTypeImp.RemoteUser));
		assertTrue(list.contains(LogFieldTypeImp.StatusCode));
		assertTrue(list.contains(LogFieldTypeImp.SizeOfResponse));
		assertTrue(list.contains(LogFieldTypeImp.RefererNCSA));
		assertTrue(list.contains(LogFieldTypeImp.UserAgentNCSA));
	}

	@Test
	public void testExtendedLogF1() {
		List<LogFieldTypeImp> list = LogFormats.ParseCmdArgs.create("#Fields:", "date", "time", "c-ip", "cs-username", "s-ip s-port", "cs-method", "cs-uri-stem", "cs-uri-query", "sc-status", "cs(User-Agent)");
		assertEquals(10, list.size());
		assertTrue(list.contains(LogFieldTypeImp.UserAgentW3C));
	}

	@Test
	public void testExtendedLogF2() {
		List<LogFieldTypeImp> list = LogFormats.ParseCmdArgs.create("#Fields:", "date", "time", "c-ip", "cs-username", "s-ip", "s-port", "cs-method", "cs-uri-stem", "cs-uri-query", "sc-status", "sc-bytes", "cs-bytes", "time-taken", "cs(User-Agent)", "cs(Referer)");
		assertEquals(15, list.size());
		assertTrue(list.contains(LogFieldTypeImp.UserAgentW3C));
		assertTrue(list.contains(LogFieldTypeImp.RefererW3C));
	}

	@Test
	public void tesetCLF1() {
		List<LogFieldTypeImp> list = LogFormats.ParseCmdArgs.make("%h %l %u %t %r %s %b %{Referrer}i %{User-agent}i %C".split(" "));
		assertEquals(10, list.size());
		list.forEach(Assert::assertNotNull);
	}

	@Test
	public void testExtendedLogF3() {
		List<LogFieldTypeImp> list = LogFormats.ParseCmdArgs.create("#Fields:", "time", "c-ip", "cs-method", "cs-uri-stem", "sc-status");
		assertEquals(5, list.size());
		assertTrue(list.contains(LogFieldTypeImp.Time));
		assertTrue(list.contains(LogFieldTypeImp.ClientIP));
		assertTrue(list.contains(LogFieldTypeImp.Method));
		assertTrue(list.contains(LogFieldTypeImp.UriSteam));
		assertTrue(list.contains(LogFieldTypeImp.StatusCode));
	}
}