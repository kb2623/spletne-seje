package org.sessionization.parser.fields;

import org.junit.Test;
import org.sessionization.parser.LogType;
import org.sessionization.parser.fields.ncsa.DateTime;
import org.sessionization.parser.fields.ncsa.RequestLine;
import org.sessionization.parser.fields.w3c.Time;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

@SuppressWarnings("deprecation")
public class TestLogField {

	@Test
	public void testUserAgent() {
		String testString = "Mozilla/5.0+(compatible;+Yahoo!+Slurp/3.0;+http://help.yahoo.com/help/us/ysearch/slurp)";
		UserAgent userAgent = new UserAgent(testString, LogType.W3C);
		String testString2 = "Mozilla/5.0+(Windows;+U;+Windows+NT+5.1;+en-US;+rv:1.9.0.16)+Gecko/2010010414+Firefox/3.0.16+Flock/2.5.6";
		UserAgent userAgent2 = new UserAgent(testString2, LogType.W3C);
		String testString3 = "Mozilla/5.0 (compatible; MSIE 10.0; Windows Phone 8.0; Trident/6.0; IEMobile/10.0; ARM; Touch; NOKIA; Lumia 520)";
		UserAgent userAgent3 = new UserAgent(testString3, LogType.NCSA);
		String testString4 = "facebookexternalhit/1.1 (+http://www.facebook.com/externalhit_uatext.php)";
		UserAgent userAgent4 = new UserAgent(testString4, LogType.NCSA);
		String testString5 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36";
		UserAgent userAgent5 = new UserAgent(testString5, LogType.NCSA);
		System.out.printf("%s%n%s%n%s%n%s%n%s%n", userAgent.izpis(), userAgent2.izpis(), userAgent3.izpis(), userAgent4.izpis(), userAgent5.izpis());
	}

	@Test
	public void testUserAgetForCrawlers() {
		UserAgent agent = new UserAgent("Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; XH; rv:8.578.498) fr, Gecko/20121021 Camino/8.723+ (Firefox compatible)", LogType.NCSA);
		// FIXME popravi delovanje metode isCrawler
		assertFalse(agent.isCrawler());
	}

	@Test
	public void testCookie() {
		String testString = "USERID=CustomerA;IMPID=01234";
		Cookie cookie = new Cookie(testString, LogType.NCSA);
		String testString2 = "USERID=CustomerA;+IMPID=01234";
		Cookie cookie2 = new Cookie(testString2, LogType.W3C);
		String testString3 = "-";
		Cookie cookie3 = new Cookie(testString3, LogType.W3C);
		Cookie cookie4 = new Cookie(testString3, LogType.NCSA);
		String testString4 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;+__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		Cookie cookie5 = new Cookie(testString4, LogType.W3C);
		assertEquals("[[USERID = CustomerA][IMPID = 01234]]", cookie.izpis());
		assertEquals("[[USERID = CustomerA][IMPID = 01234]]", cookie2.izpis());
		assertEquals("[-]", cookie3.izpis());
		assertEquals("[-]", cookie4.izpis());
		assertEquals("[[__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8][GAMBIT_ID = 89.142.126.214-4231287712.29944903][referencna = -][__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa][productsForComparison = 712150%21701041%21610119%21403083][ASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM][__utmb = 237691092.48.10.1238543004][__utmc = 237691092]]", cookie5.izpis());
	}

	@Test
	public void testCookieEquals() {
		String testString = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;+GAMBIT_ID=89.142.126.214-4231287712.29944903;+referencna=;__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;+productsForComparison=712150%21701041%21610119%21403083;+ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;+__utmb=237691092.48.10.1238543004;+__utmc=237691092";
		String testString2 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;GAMBIT_ID=89.142.126.214-4231287712.29944903;referencna=;__utmz=237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa;productsForComparison=712150%21701041%21610119%21403083;ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;__utmb=237691092.48.10.1238543004;__utmc=237691092";
		String testString3 = "__utma=237691092.2338317182835442000.1214229487.1238435165.1238543004.8;GAMBIT_ID=89.142.126.214-4231287712.29944903;referencna=;productsForComparison=712150%21701041%21610119%21403083;ASPSESSIONIDASARQBAB=MDLBNDABLMEFLDGCFIFOJIGM;__utmb=237691092.48.10.1238543004;__utmc=237691092";
		Cookie cookie = new Cookie(testString, LogType.W3C);
		Cookie cookie2 = new Cookie(testString2, LogType.NCSA);
		Cookie cookie3 = new Cookie(testString3, LogType.NCSA);
		assertTrue(cookie.equals(cookie2));
		assertFalse(cookie.equals(cookie3));
		assertFalse(cookie.equals(new UserAgent("-", LogType.NCSA)));
		assertFalse(cookie.equals(new UserAgent("-", LogType.W3C)));
	}

	@Test
	public void testRequestLine() {
		RequestLine line = null;
		try {
			line = new RequestLine("GET", "/jope-puloverji/moski-pulover-b74-red?limit=18&test=hello", "HTTP/1.1");
			assertEquals("GET", line.getMethod().toString());
			assertEquals(1.1, line.getProtocol().getVersion(), 0.01);
			assertEquals("HTTP/1.1", line.getProtocol().toString());
			assertEquals("[[limit = 18][test = hello]]", ((UriSteamQuery) line.getFile()).getQuery().toString());
			assertEquals("/jope-puloverji/moski-pulover-b74-red[[limit = 18][test = hello]]", line.getFile().toString());
			assertEquals("/jope-puloverji/moski-pulover-b74-red", line.getFile().getFile());
			assertFalse(line.getUri().isResource());
			line = new RequestLine("POST", "/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css?limit=10", "HTTP/1.1");
			assertEquals("POST", line.getMethod().toString());
			assertEquals(1.1, line.getProtocol().getVersion(), 0.01);
			assertEquals("HTTP/1.1", line.getProtocol().toString());
			assertEquals("[[limit = 10]]", ((UriSteamQuery) line.getFile()).getQuery().toString());
			assertEquals("/catalog/view/javascript/jquery/supermenu/templates/gray/" +
					"supermenu.css[[limit = 10]]", line.getFile().toString());
			assertEquals("/catalog/view/javascript/jquery/supermenu/templates/gray/" +
					"supermenu.css", line.getFile().getFile());
			assertTrue(line.getUri().isResource());
			line = new RequestLine("GET", "/image/cache/data/" +
					"bigbananaPACK%20copy-cr-214x293.png", "HTTP");
			assertEquals("GET", line.getMethod().toString());
			assertEquals(0, line.getProtocol().getVersion(), 0.01);
			assertEquals("HTTP", line.getProtocol().toString());
			assertEquals("[]", ((UriSteamQuery) line.getFile()).getQuery().toString());
			assertTrue(line.getUri().isResource());
			assertEquals("/image/cache/data/bigbananaPACK%20copy-cr-214x293.png", line.getFile().toString());
		} catch (URISyntaxException e) {
			fail();
		}
	}

	@Test
	public void testUriSteam() {
		UriSteam uriSteam = new UriSteam("/image/cache/data/topbananaBLU-cr-214x293.png");
		assertTrue(uriSteam.isResource());
		uriSteam = new UriSteam("/image/cache/data/topbananaBLU-cr-214x293");
		assertFalse(uriSteam.isResource());
	}

	@Test
	public void testUriSteamQuery() throws URISyntaxException {
		UriSteamQuery uriSteam = new UriSteamQuery(new URI("/srajce/srajce-kratek-rokav/srajca-s-kratkimi-rokavi-carisma-black-9024bl?sort=p.model&order=ASC"));
		assertFalse(uriSteam.isResource());
		uriSteam = new UriSteamQuery(new URI("/srajce/srajce-kratek-rokav/srajca-s-kratkimi-rokavi-carisma-black-9024bl.jpg?sort=p.model&order=ASC"));
		assertTrue(uriSteam.isResource());
		uriSteam = new UriSteamQuery(new URI("/image/cache/data/topbananaBLU-cr-214x293.png"));
		assertTrue(uriSteam.isResource());
	}

	@Test
	public void testTimePoint() {
		DateTime dt1 = new DateTime(LocalDateTime.of(2015, 12, 17, 0, 0, 12)), dt2 = new DateTime(LocalDateTime.of(2015, 12, 18, 0, 0, 12));
		assertEquals(24 * 3600, dt1.secBetwene(dt2));
		dt1 = new DateTime(LocalDateTime.of(2015, 6, 27, 4, 44, 51));
		dt2 = new DateTime(LocalDateTime.of(2015, 6, 28, 2, 44, 51));
		assertEquals(79200, dt1.secBetwene(dt2));
		System.out.println(dt2.secBetwene(dt1));
		Time t1 = new Time(LocalTime.of(23, 45, 45)), t2 = new Time(LocalTime.of(0, 10, 10));
		System.out.println(t1.secBetwene(t2));
		System.out.println(t2.secBetwene(t1));
	}

	@Test
	public void testInetAddress() throws UnknownHostException {
		InetAddress address = InetAddress.getByName("www.google.com");
		System.out.println(address.getHostAddress());
		System.out.println(address.getHostName());
		InetAddress address1 = InetAddress.getByName("8.8.8.8");
		System.out.println(address1.getHostAddress());
		System.out.println(address1.getHostName());
	}

	@Test
	public void testPatternMetaData() {
		Pattern p1 = Pattern.compile("#(Version|Fields|Software|Start-Date|End-Date|Date|Remark):");
		Pattern p2 = Pattern.compile("#Fields:");
		Scanner s = new Scanner("#Fields: ip ipv4 ipv6");
		System.out.println(s.hasNext(p1));
		System.out.println(s.hasNext(p2));
	}

	@Test
	public void testPatternDateTime() {
		Pattern p1 = Pattern.compile("\\[(.|\\p{Space})+");
		Scanner s = new Scanner("[12/Jan/2014 12:23:00 +0010]");
		System.out.println(s.hasNext(p1));
		System.out.println(s.next(p1));
	}
}
