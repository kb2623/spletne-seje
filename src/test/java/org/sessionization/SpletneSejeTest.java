package org.sessionization;

import org.datastruct.RadixTreeMap;
import org.junit.Before;
import org.junit.Ignore;
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

	private WebLogParser parser;

	@Before
	public void setUp() throws IOException {
		parser = new NCSAWebLogParser();
	}

	@Test
	public void testNCSAParserCommonResHashMap() throws IOException {
		Map<String, List<ParsedLine>> testMap = new HashMap<>();
		parser.openFile(new File[]{new File(ClassLoader.getSystemResource("logCommon").getPath())});
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
	@Ignore
	public void testNCSAParserCommonResRadixTree() throws IOException {
		Map<String, List<ParsedLine>> testMap = new RadixTreeMap<>();
		parser.openFile(new File[]{new File(ClassLoader.getSystemResource("logCommon").getPath())});
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
		parser.openFile(new File[]{new File(ClassLoader.getSystemResource("logCommon").getPath())});
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
	@Ignore
	public void testRunHelp() {
		SpletneSeje.main("-h");
	}

	/**
	 * Pri testih nad datoteko <code>testLog</code> so rezultati v tablelah naslednji:
	 * Address ima 13 zapisov
	 * UserId ima 13 zapisov
	 * UserSession ima 13 zapisov
	 */
	@Test
	@Ignore
	public void testRunCommonShortSQLite() {
		SpletneSeje.main("-fl", "COMMON", ClassLoader.getSystemResource("testLog").getPath());
	}

	@Test
	@Ignore
	public void testRunCommonShortH2() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), ClassLoader.getSystemResource("testLog").getPath());
	}

	@Test
	@Ignore
	public void testRunCommonShortHSQLBD() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), ClassLoader.getSystemResource("testLog").getPath());
	}

	@Test
	@Ignore
	public void testRunCommonShortDerby() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), ClassLoader.getSystemResource("testLog").getPath());
	}

	/**
	 * Pri testih nad datoteko <code>logCommon</code> so rezultati v tablelah naslednji:
	 * Address ima 187 zapisov
	 * UserSession ima 200 zapisov
	 */
	@Test
	@Ignore
	public void testRunCommonSQLite() {
		SpletneSeje.main("-fl", "COMMON", ClassLoader.getSystemResource("logCommon").getPath());
	}

	@Test
	@Ignore
	public void testRunCommonH2() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), ClassLoader.getSystemResource("logCommon").getPath());
	}

	@Test
	@Ignore
	public void testRunCommonHSQLBD() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), ClassLoader.getSystemResource("logCommon").getPath());
	}

	@Test
	@Ignore
	public void testRunCommonDerby() {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), ClassLoader.getSystemResource("logCommon").getPath());
	}

	/**
	 * Pri testih nad datoteko <code>access_log</code> so rezultati v tablelah naslednji:
	 * Address ima 187 zapisov
	 * UserId ima 198 zapisov
	 * UserSession ima 210 zapisov
	 */
	@Test
	@Ignore
	public void testRunCombinedSQLite() {
		SpletneSeje.main("-fl", "COMBINED", ClassLoader.getSystemResource("access_log").getPath());
	}

	@Test
	@Ignore
	public void testRunCombinedH2() {
		SpletneSeje.main("-fl", "COMBINED", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), ClassLoader.getSystemResource("access_log").getPath());
	}

	@Test
	@Ignore
	public void testRunCombinedHSQLBD() {
		SpletneSeje.main("-fl", "COMBINED", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), ClassLoader.getSystemResource("access_log").getPath());
	}

	@Test
	@Ignore
	public void testRunCombinedDerby() {
		SpletneSeje.main("-fl", "COMBINED", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), ClassLoader.getSystemResource("access_log").getPath());
	}

	/**
	 * Pri testih nad datoteko <code>ex080814.log</code> so rezultati v tablelah naslednji:
	 * Address ima 9 zapisov
	 * UserSession ima 9 zapisov
	 */
	@Test
	@Ignore
	public void testRunExtendedSQLite() {
		SpletneSeje.main("-fl", "EXTENDED", ClassLoader.getSystemResource("ex051222.log").getPath());
	}

	@Test
	@Ignore
	public void testRunExtendedH2() {
		SpletneSeje.main("-fl", "EXTENDED", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), ClassLoader.getSystemResource("ex051222.log").getPath());
	}

	@Test
	@Ignore
	public void testRunExtendedHSQLBD() {
		SpletneSeje.main("-fl", "EXTENDED", "-props", ClassLoader.getSystemResource("HSQLDB.properties").getPath(), ClassLoader.getSystemResource("ex051222.log").getPath());
	}

	@Test
	@Ignore
	public void testRunExtendedDerby() {
		SpletneSeje.main("-fl", "EXTENDED", "-props", ClassLoader.getSystemResource("Derby.properties").getPath(), ClassLoader.getSystemResource("ex051222.log").getPath());
	}
}