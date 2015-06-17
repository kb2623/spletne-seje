package spletneseje.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import spletneseje.fields.FieldType;
import spletneseje.fields.ncsa.RequestLine;
import spletneseje.parser.datastruct.ParsedLine;

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

    public NCSAParserTest() {
        String os = System.getProperty("os.name");
        if(os.contains("Windows")) {
            pathNCSACombined = System.getProperty("user.dir") + "\\Logs\\Combined\\access_log";
            pathNCSACommon = System.getProperty("user.dir") + "\\Logs\\Common\\logCommon";
        } else {
            pathNCSACombined = System.getProperty("user.dir") + "/Logs/Combined/access_log";
            pathNCSACommon = System.getProperty("user.dir") + "/Logs/Common/logCommon";
        }
    }

    @Before
    public void setUp() throws Exception {
        parser = new NCSAParser();
    }

    @Test
    public void testNCSAParserCommon() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(FieldType.createCommonLogFormat());
            //Pridobi podatke
            for (ParsedLine list : parser) list.forEach(Assert::assertNotNull);
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCommonTryResource() {
        try (NCSAParser parser1 = new NCSAParser(pathNCSACommon)) {
            parser1.setFieldType(FieldType.createCommonLogFormat());
            for (ParsedLine list : parser1) list.forEach(Assert::assertNotNull);
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCommonForEach() {
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(FieldType.createCommonLogFormat());
            //Pridobi podatke
            parser.forEach(line -> line.forEach(Assert::assertNotNull));
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCommonRes() {
        Map<String, List<ParsedLine>> testMap = new HashMap<>();
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(FieldType.createCommonLogFormat());
            //Pridobi podatke
            for (ParsedLine line : parser) {
                List<ParsedLine> list = testMap.get(line.getKey());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(line);
                    testMap.put(line.getKey(), list);
                } else {
                    list.add(line);
                }
            }
            //Zapri datoteko
            parser.closeFile();
            for (Map.Entry<String, List<ParsedLine>> entry : testMap.entrySet()) {
                System.out.println(entry.getKey() + " <> " + entry.getValue().size());
                entry.getValue().stream().forEach((f1) -> {
                    if (!f1.isResurse()) {
                        System.out.print("\t" + f1.izpis());
                        System.out.println();
                    }
                });
                System.out.println();
            }
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
            parser.setFieldType(FieldType.createCombinedLogFormat(false));
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
            parser.setFieldType(FieldType.createCombinedLogFormat(false));
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
            parser.setFieldType(FieldType.createCombinedLogFormat(false));
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
            parser.setFieldType(FieldType.createCombinedLogFormat(false));
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
            parser1.setFieldType(FieldType.createCombinedLogFormat(false));
            parser1.forEach(Assert::assertNotNull);
        } catch (IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedCookieTryResource() {
        String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
        try (NCSAParser parser1 = new NCSAParser(new StringReader(testNiz))) {
            parser1.setFieldType(FieldType.createCombinedLogFormat(true));
            parser1.forEach(Assert::assertNotNull);
        } catch (IOException e) {
            assert false;
        }
    }

    @Test
    public void testNCSAParserCombinedWithCookie() {
        String testNiz = "216.67.1.91 - leon [01/Jul/2002:12:11:52 +0000] \"GET /index.html HTTP/1.1\" 200 431 \"http://www.loganalyzer.net/\" \"Mozilla/4.05 [en] (WinNT; I)\" \"USERID=CustomerA;IMPID=01234\"";
        try {
            //Odpri datoteko
            parser.openFile(new StringReader(testNiz));
            //Nastavi tipe podatkov
            List<FieldType> listType = FieldType.createCombinedLogFormat(true);
            //Dodatni atribut
            parser.setFieldType(listType);
            //Pridobi podatke
            ParsedLine list = parser.parseLine();
            list.forEach(
                    (f) -> {
                        if (f == null) assert false;
                        else if (f instanceof RequestLine) {
                            URL r = ((RequestLine) f).getUrl();
                            System.out.print(((RequestLine) f).getMethod().izpis() + " | " + r.getPath() + " | " + r.getQuery() + " | " + r.getProtocol() + " | ");
                        } else System.out.print(f.izpis() + " || ");
                    }
            );
            //Zapri datoteko
            parser.closeFile();
        } catch(NullPointerException | ParseException | IOException e) {
            assert false;
        }
    }
}