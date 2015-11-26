package org.sessionization;

import org.datastruct.RadixTree;
import org.datastruct.Sequence;
import org.junit.Before;
import org.junit.Test;
import org.sessionization.parser.AbsParser;
import org.sessionization.parser.LogFormats;
import org.sessionization.parser.NCSAParser;
import org.sessionization.parser.datastruct.ParsedLine;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import static org.junit.Assert.*;

public class SpletneSejeTest {

	private String pathNCSACombined;
	private String pathNCSACommon;

	private AbsParser parser;

	@Before
	public void setUp() {
		pathNCSACombined = ClassLoader.getSystemResource("access_log").getFile();
		pathNCSACommon = ClassLoader.getSystemResource("testLog").getFile();
		parser = new NCSAParser();
	}

	@Test
	public void testNCSAParserCommonResHashMap() {
		Map<Niz, List<ParsedLine>> testMap = new HashMap<>();
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
			for (ParsedLine line : parser) {
				List<ParsedLine> list = testMap.get(line.getKey());
				if (list == null) {
					list = new ArrayList<>();
					list.add(line);
					testMap.put(new Niz(line.getKey()), list);
				} else {
					list.add(line);
				}
			}
			parser.closeFile();
			assertEquals(269, testMap.size());
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCommonResRadixTree() {
		Map<Niz, List<ParsedLine>> testMap = new RadixTree<>();
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
			for (ParsedLine line : parser) {
				List<ParsedLine> list = testMap.get(line.getKey());
				if (list == null) {
					list = new ArrayList<>();
					list.add(line);
					testMap.put(new Niz(line.getKey()), list);
				} else {
					list.add(line);
				}
			}
			parser.closeFile();
			assertEquals(269, testMap.size());
		} catch(NullPointerException | IOException e) {
			fail();
		}
	}

	@Test
	public void testNCSAParserCommonResRadixTreeHashMap() {
		Map<Niz, List<ParsedLine>> radixMap = new RadixTree<>();
		Map<Niz, List<ParsedLine>> hashMap = new HashMap<>();
		try {
			parser.openFile(new File[]{new File(pathNCSACommon)});
			parser.setFieldType(LogFormats.CommonLogFormat.create(null));
			for (ParsedLine line : parser) {
				Niz key = new Niz(line.getKey());
				List<ParsedLine> list = radixMap.get(key);
				if (list == null) {
					list = new ArrayList<>();
					list.add(line);
					radixMap.put(key, list);
				} else {
					list.add(line);
				}
				list = hashMap.get(key);
				if (list == null) {
					list = new ArrayList<>();
					list.add(line);
					hashMap.put(key, list);
				} else {
					list.add(line);
				}
			}
			parser.closeFile();
			assertEquals(hashMap.size(), radixMap.size());
			for (Map.Entry<Niz, List<ParsedLine>> entry : hashMap.entrySet()) {
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
		SpletneSeje.main("-h");
	}

	@Test
	public void testRunOne() {
		SpletneSeje.main("-fl", "COMMON", "-dbsqf", "-dbsq", pathNCSACommon);
	}

	@Test
	public void testRunTwo() throws MalformedURLException {
		SpletneSeje.main("-fl", "COMMON", "-xml", "H2.cfg.xml", "-dbdr", "lib/h2-1.4.188.jar", "-dbdrc", "adfkl;", pathNCSACommon);
	}

	@Test
	public void testSome() throws Exception {
		Properties properties = new Properties();
		assertNotNull(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		properties.load(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		properties.storeToXML(System.out, null, "UTF-8");
		properties.storeToXML(System.out, null);
	}

	class Niz implements Sequence<Niz> {

		String niz;

		Niz(String niz) {
			this.niz = niz;
		}

		@Override
		public int equalDistance(Niz s) {
			int distance = 0;
			for (int i = 0; i < niz.length(); i++) {
				if (i < s.length()) {
					if (niz.charAt(i) == s.niz.charAt(i)) {
						distance++;
					} else {
						return distance;
					}
				} else {
					return distance;
				}
			}
			return distance;
		}

		@Override
		public int length() {
			return niz.length();
		}

		@Override
		public Niz append(Niz s) {
			return new Niz(niz + s.niz);
		}

		@Override
		public Niz subSequence(int start, int end) {
			return new Niz(niz.substring(start, end));
		}

		@Override
		public String toString() {
			return niz;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null) return false;
			if (o == this) return true;
			if (o instanceof String) {
				return niz.equals(o);
			} else {
				Niz n = (Niz) o;
				return niz.equals(n.niz);
			}
		}
	}

}