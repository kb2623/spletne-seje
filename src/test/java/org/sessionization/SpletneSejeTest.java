package org.sessionization;

import org.datastruct.RadixTree;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SpletneSejeTest {

	private String pathNCSACombined;
	private String pathNCSACommon;

	private AbsParser parser;

	@Before
	public void setUp() {
		pathNCSACombined = ClassLoader.getSystemResource("access_log").getFile();
		pathNCSACommon = ClassLoader.getSystemResource("logCommon").getFile();
		parser = new NCSAParser();
	}

	@Test
	public void testNCSAParserCommonResHashMap() {
		Map<String, List<ParsedLine>> testMap = new HashMap<>();
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
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
			parser.closeFile();
			for (Map.Entry<String, List<ParsedLine>> entry : testMap.entrySet()) {
				System.out.println(entry.getKey() + " <> " + entry.getValue().size());
//				entry.getValue().stream().forEach((f1) -> {
//					if (!f1.isResource()) System.out.print("\t" + f1.izpis() + "\n");
//				});
				System.out.println();
			}
			assertEquals(269, testMap.size());
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCommonResRadixTree() {
		Map<String, List<ParsedLine>> testMap = new RadixTree<>();
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
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
			parser.closeFile();
			for (Map.Entry<String, List<ParsedLine>> entry : testMap.entrySet()) {
				System.out.println(entry.getKey() + " <> " + entry.getValue().size());
//				entry.getValue().stream().forEach((f1) -> {
//					if (!f1.isResource()) System.out.print("\t" + f1.izpis() + "\n");
//				});
				System.out.println();
			}
			assertEquals(269, testMap.size());
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCommonResRadixTreeHashMap() {
		Map<String, List<ParsedLine>> radixMap = new RadixTree<>();
		Map<String, List<ParsedLine>> hashMap = new HashMap<>();
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
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
			parser.closeFile();
			assertEquals(hashMap.size(), radixMap.size());
			for (Map.Entry<String, List<ParsedLine>> entry : hashMap.entrySet()) {
				assertEquals(entry.getValue().size(), radixMap.get(entry.getKey()).size());
			}
			radixMap.keySet().forEach(e -> hashMap.remove(e));
			assertTrue(hashMap.isEmpty());
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testRunZero() {
		SpletneSeje.main("-?");
	}

	@Test
	public void testRunOne() {
		SpletneSeje.main("-in", pathNCSACommon, "-fl", "COMMON", "-dbsqf", "-dbsq");
	}

	@Test
	public void testRunTwo() throws MalformedURLException {
		SpletneSeje.main("-in", pathNCSACommon, "-fl", "COMMON", "-xml", "H2.cfg.xml", "-dbdr", "lib/h2-1.4.188.jar", "-dbdrc", "adfkl;");
	}

}