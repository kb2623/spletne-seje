package org.sessionization;

import org.datastruct.ClassPool;
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
import java.util.*;

import static org.junit.Assert.*;

public class SpletneSejeTest {

	private String pathNCSACombined;
	private String pathNCSACommon;

	private AbsParser parser;

	@Before
	public void setUp() throws IOException {
		ClassPool.initClassPool(null, null);
		pathNCSACombined = ClassLoader.getSystemResource("access_log").getFile();
		pathNCSACommon = ClassLoader.getSystemResource("testLog").getFile();
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
			assertEquals(28, testMap.size());
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
			assertEquals(28, testMap.size());
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
		SpletneSeje.main("-h");
	}

	@Test
	public void testRunOne() {
		SpletneSeje.main("-fl", "COMMON", "-dbsqf", "-dbsq", pathNCSACommon);
	}

	@Test
	public void testRunTwo() throws MalformedURLException {
		SpletneSeje.main("-fl", "COMMON", "-props", ClassLoader.getSystemResource("H2.properties").getPath(), "-dbdr", "lib/h2-1.4.188.jar", "-dbdrc", "adfkl;", pathNCSACommon);
	}

	@Test
	public void testSome() throws Exception {
		Properties properties = new Properties();
		assertNotNull(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		properties.load(ClassLoader.getSystemResourceAsStream("ClassPool.properties"));
		properties.storeToXML(System.out, null, "UTF-8");
		properties.storeToXML(System.out, null);
	}

}