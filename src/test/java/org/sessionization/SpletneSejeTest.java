package org.sessionization;

import org.junit.Before;
import org.junit.Test;
import org.datastruct.RadixTree;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAParser;
import org.sessionization.parser.datastruct.ParsedLine;
import org.sessionization.parser.datastruct.WebPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SpletneSejeTest {

    private String pathNCSACombined;
    private String pathNCSACommon;

    private AbsParser parser;

    @Before
    public void setUp() {
        pathNCSACombined = ClassLoader.getSystemResource("Logs/Combined/access_log").getFile();
        pathNCSACommon = ClassLoader.getSystemResource("Logs/Common/logCommon").getFile();
        parser = new NCSAParser();
    }

    @Test
    public void testNCSAParserCommonResHashMap() {
        Map<String, List<ParsedLine>> testMap = new HashMap<>();
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CommonLogFormat.create(null));
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
                    if (!f1.isResource()) System.out.print("\t" + f1.izpis() + "\n");
                });
                System.out.println();
            }
        } catch(NullPointerException | IOException e) {
            fail();
        }
    }

    @Test
    public void testNCSAParserCommonResRadixTree() {
        Map<String, List<ParsedLine>> testMap = new RadixTree<>();
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CommonLogFormat.create(null));
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
                    if (!f1.isResource()) System.out.print("\t" + f1.izpis() + "\n");
                });
                System.out.println();
            }
        } catch(NullPointerException | IOException e) {
            fail();
        }
    }

    @Test
    public void testNCSAParserCommonResRadixTreeHashMap() {
        Map<String, List<ParsedLine>> radixMap = new RadixTree<>();
        Map<String, List<ParsedLine>> hashMap = new HashMap<>();
        try {
            //Odpri datoteko
            parser.openFile(pathNCSACommon);
            //Nastavi tipe podatkov
            parser.setFieldType(LogFormats.CommonLogFormat.create(null));
            //Pridobi podatke
            for (ParsedLine line : parser) {
                List<ParsedLine> list = radixMap.get(line.getKey());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(line);
                    radixMap.put(line.getKey(), list);
                } else {
                    list.add(line);
                }
                list = hashMap.get(line.getKey());
                if (list == null) {
                    list = new ArrayList<>();
                    list.add(line);
                    hashMap.put(line.getKey(), list);
                } else {
                    list.add(line);
                }
            }
            //Zapri datoteko
            parser.closeFile();
            radixMap.keySet().forEach(e -> hashMap.remove(e));
            assertTrue(hashMap.isEmpty());
        } catch(NullPointerException | IOException e) {
            fail();
        }
    }
}