package org.sessionization;

import org.datastruct.RadixTreeMap;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAWebLogParser;
import org.sessionization.parser.WebLogParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpletneSejeTest {

	private String pathNCSACombined;
	private String pathNCSACommon;
	private String pathW3CExtended;

	private WebLogParser parser;

	@Before
	public void setUp() throws IOException {
		pathNCSACombined = ClassLoader.getSystemResource("access_log").getPath();
//		pathNCSACommon = ClassLoader.getSystemResource("logCommon").getPath();
		pathNCSACommon = ClassLoader.getSystemResource("testLog").getPath();
		pathW3CExtended = ClassLoader.getSystemResource("ex080814.log").getPath();
		parser = new NCSAWebLogParser();
	}

	@Test
	public void testNCSAParserCommonResHashMap() throws IOException {
		Map<String, List<ParsedLine>> testMap = new HashMap<>();
		parser.openFile(new File[]{new File(pathNCSACommon)});
		parser.setFieldType(LogFormats.CommonLogFormat.make());
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
		assertEquals(269, testMap.size());
	}

	@Test
	public void testNCSAParserCommonResRadixTree() throws IOException {
		Map<String, List<ParsedLine>> testMap = new RadixTreeMap<>();
		parser.openFile(new File[]{new File(pathNCSACommon)});
		parser.setFieldType(LogFormats.CommonLogFormat.make());
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
		boolean run = true;
		Scanner sc = new Scanner(System.in);
		Iterator<List<ParsedLine>> listIt = testMap.values().iterator();
		Iterator<ParsedLine> line = listIt.next().iterator();
		while (run) {
			int select = sc.nextInt();
			System.out.println();
			switch (select) {
				case 1:
					System.out.println("Skiping: " + line.next().getKey());
					line = listIt.next().iterator();
					break;
				case 2:
					System.out.println("Skiping: " + line.next().toString());
					while (!line.next().isWebPageResource()) {
					}
					break;
				case 3:
					ParsedLine l = null;
					do {
						l = line.next();
						System.out.println(line.next().toString());
					} while (l.isWebPageResource());
					break;
				default:
					run = false;

			}
		}
		assertEquals(269, testMap.size());
	}

	@Test
	public void testNCSAParserCommonResRadixTreeHashMap() throws IOException {
		Map<String, List<ParsedLine>> radixMap = new RadixTreeMap<>();
		Map<String, List<ParsedLine>> hashMap = new HashMap<>();
		parser.openFile(new File[]{new File(pathNCSACommon)});
		parser.setFieldType(LogFormats.CommonLogFormat.make());
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
	}

	@Test
	public void testRunZero() {
		SpletneSeje.main("-h");
	}

	@Test
	public void testRunSQLiteCommon() {
		SpletneSeje.main("-fl", "COMMON", pathNCSACommon);
	}

	@Test
	public void testRunSQLiteCombined() {
		SpletneSeje.main("-fl", "COMBINED", "-dbsqf", "-dbsq", pathNCSACombined);
	}

	@Test
	public void testRunSQLiteExtended() {
		SpletneSeje.main("-fl", "EXTENDED", "-dbsqf", "-dbsq", pathW3CExtended);
	}

	@Test
	public void testRunH2Common() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), pathNCSACommon);
	}

	@Test
	public void testRunH2Combined() {
		SpletneSeje.main("-fl", "COMBINED", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), pathNCSACombined);
	}

	@Test
	public void testRunH2Extended() {
		SpletneSeje.main("-fl", "EXTENDED", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), pathW3CExtended);
	}

	@Test
	public void testRunHSQLBDCommon() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), pathNCSACommon);
	}

	@Test
	public void testRunHSQLBDCombined() {
		SpletneSeje.main("-fl", "COMBINED", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), pathNCSACombined);
	}

	@Test
	public void testRunHSQLBDExtended() {
		SpletneSeje.main("-fl", "EXTENDED", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), pathW3CExtended);
	}

	@Test
	public void testRunDerbyCommon() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), pathNCSACommon);
	}

	@Test
	public void testRunDerbyCombined() {
		SpletneSeje.main("-fl", "COMBINED", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), pathNCSACombined);
	}

	@Test
	public void testRunDerbyExtended() {
		SpletneSeje.main("-fl", "EXTENDED", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), pathW3CExtended);
	}
}