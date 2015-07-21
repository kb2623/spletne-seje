package org.sessionization.fields;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sessionization.fields.ncsa.RequestLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("deprecation")
public class TestFields {

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
		assertEquals("[[referencna = -][__utmz = 237691092.1238435165.7.2.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=enaa][GAMBIT_ID = 89.142.126.214-4231287712.29944903][ASPSESSIONIDASARQBAB = MDLBNDABLMEFLDGCFIFOJIGM][__utmb = 237691092.48.10.1238543004][__utmc = 237691092][__utma = 237691092.2338317182835442000.1214229487.1238435165.1238543004.8][productsForComparison = 712150%21701041%21610119%21403083]]", cookie5.izpis());
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
	public void testDateParsing() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.US);
		LocalDateTime dateTime = LocalDateTime.parse("26/Jul/2002:12:11:52 +0000", formatter);
		assertEquals(26, dateTime.getDayOfMonth());
	}

	@Test
	public void testRequestLine() throws MalformedURLException {
        RequestLine line = new RequestLine("GET", "/jope-puloverji/moski-pulover-b74-red?limit=18&test=hello", "HTTP/1.1");
        assertEquals("GET", line.getMethod().toString());
        assertEquals(1.1, line.getProtocol().getVersion(), 0.01);
        assertEquals("HTTP/1.1", line.getProtocol().toString());
        assertEquals("[[test = hello][limit = 18]]", line.getFile().getQuery().toString());
        assertEquals("/jope-puloverji/moski-pulover-b74-red?limit=18&test=hello[[test = hello][limit = 18]]", line.getFile().toString());
        assertEquals("/jope-puloverji/moski-pulover-b74-red", line.getFile().getFile());
        line = new RequestLine("POST", "/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css?limit=10", "HTTP/1.1");
        assertEquals("POST", line.getMethod().toString());
        assertEquals(1.1, line.getProtocol().getVersion(), 0.01);
        assertEquals("HTTP/1.1", line.getProtocol().toString());
        assertEquals("[[limit = 10]]", line.getFile().getQuery().toString());
        assertEquals("/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css?limit=10", line.getFile().toString());
        assertEquals("/catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css", line.getFile().getFile());
        line = new RequestLine("GET", "/image/cache/data/bigbananaPACK%20copy-cr-214x293.png", "HTTP");
        assertEquals("GET", line.getMethod().toString());
        assertEquals(0, line.getProtocol().getVersion(), 0.01);
        assertEquals("HTTP", line.getProtocol().toString());
        assertEquals("[]", line.getFile().getQuery().toString());
        assertEquals("/image/cache/data/bigbananaPACK%20copy-cr-214x293.png", line.getFile().toString());
    }

	@Test
	public void testReferer() throws MalformedURLException {
		URL u = new URL("http://sfashion.si/hlace?limit=30");
		System.out.println(u.getAuthority() + "\n" + u.getFile());
		u = new URL("http", null, "/hlace?limit=30");
		System.out.println(u.getProtocol() + "\n" + u.getFile());
	}

}
