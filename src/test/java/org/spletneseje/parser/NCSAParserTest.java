package org.spletneseje.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spletneseje.datastruct.RadixTree;
import org.spletneseje.fields.FieldType;
import org.spletneseje.fields.ncsa.RequestLine;
import org.spletneseje.parser.datastruct.ParsedLine;
import org.spletneseje.parser.datastruct.WebPage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

@SuppressWarnings("deprecation")
public class NCSAParserTest {

    private String pathNCSACombined;
    private String pathNCSACommon;

    private NCSAParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new NCSAParser();
        pathNCSACombined = ClassLoader.getSystemResource("Logs/Combined/access_log").getFile();
        pathNCSACommon = ClassLoader.getSystemResource("Logs/Common/logCommon").getFile();
    }

    @Test
    public void testNCSAParserCommon() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CommonLogFormat.create(null));
            //Pridobi podatke
            for (ParsedLine list : parser) list.forEach(Assert::assertNotNull);
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCommonTryResource() throws NullPointerException, IOException {
        try (NCSAParser parser1 = new NCSAParser(pathNCSACommon)) {
            parser1.setFieldType(LogFormats.CommonLogFormat.create(null));
            for (ParsedLine list : parser1) list.forEach(Assert::assertNotNull);
        }
    }

    @Test
    public void testNCSAParserCommonForEach() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CommonLogFormat.create(null));
            //Pridobi podatke
            parser.forEach(line -> line.forEach(Assert::assertNotNull));
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedForEachWithSetFormat() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACombined);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
            //Pridobi podatke
            parser.forEach(line -> line.forEach(Assert::assertNotNull));
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedWithSetFormat() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACombined);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
            //Nastavi format datuma
            parser.setDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
            //Pridobi podatke
            for (ParsedLine list : parser) list.forEach(Assert::assertNotNull);
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedForEach() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACombined);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
            //Pridobi podatke
            parser.forEach(line -> line.forEach(Assert::assertNotNull));
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombined() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACombined);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CombinedLogFormat.create(null));
            //Pridobi podatke
            for (ParsedLine list : parser) list.forEach(Assert::assertNotNull);
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedTryResource() {
        try (NCSAParser parser1 = new NCSAParser(pathNCSACombined)) {
            parser1.setFieldType(LogFormats.CombinedLogFormat.create(null));
            parser1.forEach(Assert::assertNotNull);
        } catch (FileNotFoundException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedCookieTryResource() {
        String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
        try (NCSAParser parser1 = new NCSAParser(new StringReader(testNiz))) {
            List<String> cookie = new ArrayList<>();
            cookie.add("%C");
            parser1.setFieldType(LogFormats.CombinedLogFormat.create(cookie));
            parser1.forEach(Assert::assertNotNull);
        }
    }

    @Test
    public void testNCSAParserCombinedWithCookie() {
        String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
        try {
            //Odpri datoteko
            parser.openFile(new StringReader(testNiz));
            //Nastavi tipe podatkov
            List<String> cookie = new ArrayList<>();
            cookie.add("%C");
            List<FieldType> listType = LogFormats.CombinedLogFormat.create(cookie);
            //Dodatni atribut
            parser.setFieldType(listType);
            //Pridobi podatke
            ParsedLine list = parser.parseLine();
            list.forEach(f -> {
                if (f == null) {
                    assert false;
                } else if (f instanceof RequestLine) {
                    URL r = ((RequestLine) f).getUrl();
                    System.out.print(((RequestLine) f).getMethod().izpis() + " | " + r.getPath() + " | " + r.getQuery() + " | " + r.getProtocol() + " | ");
                } else {
                    System.out.print(f.izpis() + " || ");
                }
            });
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | ParseException | IOException e) {
            assert false;
        }
    }
}