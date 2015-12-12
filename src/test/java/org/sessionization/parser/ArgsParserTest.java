package org.sessionization.parser;

import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

public class ArgsParserTest {

	private ArgsParser parser;

	private static Class[] getClasses(String packageName, ClassLoader loader) throws ClassNotFoundException, IOException {
		assert loader != null;
		String path = packageName.replace('.', '/');
		InputStreamReader reader = new InputStreamReader(loader.getResourceAsStream(path));
		int c = (char) reader.read();
		while (c != -1) {
			System.out.print((char) c);
			c = reader.read();
		}
		reader.close();
		/*
		Enumeration<URL> resources = loader.getResources(path);
		List dirs = new ArrayList();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList classes = new ArrayList();
		for (Object directory : dirs) {
			classes.addAll(findClasses((File) directory, packageName, loader));
		}
		return (Class[]) classes.toArray(new Class[classes.size()]);
		*/
		return null;
	}

	private static List findClasses(File directory, String packageName, ClassLoader loader) throws ClassNotFoundException {
		List classes = new ArrayList();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName(), loader));
			} else if (file.getName().endsWith(".class")) {
				classes.add(loader.loadClass(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	@Test
	public void testArgsParsingHelp() {
		try {
			parser = new ArgsParser("-h");
			if (parser.isPrintHelp()) {
				parser.printHelp(System.out);
			} else {
				fail();
			}
		} catch (CmdLineException e) {
			fail();
		} catch (URISyntaxException e) {
			fail();
		}
	}

	@Test(expected = CmdLineException.class)
	public void testNoFileError() throws URISyntaxException, CmdLineException {
		parser = new ArgsParser("-prps", ClassLoader.getSystemResource("H2.properties").toURI().getPath());
	}

	@Test
	public void testArgsParsingZero() {
		try {
			parser = new ArgsParser("test");
			assertFalse(parser.isShowSql());
			assertFalse(parser.isIgnoreCrawlers());
			assertFalse(parser.isShowSqlFormat());
			assertEquals("org.sqlite.JDBC", parser.getDriverClass());
			assertEquals("org.dialect.SQLiteDialect", parser.getDialectClass());
			assertEquals(1, parser.getConnectoinPoolSize());
			assertEquals("jdbc:sqlite:qliteDB", parser.getDatabaseUrl().toASCIIString());
			assertEquals("test", parser.getInputFile()[0].getName());
			assertEquals(Locale.US, parser.getLocale());
			System.out.println();
		} catch (CmdLineException e) {
			fail();
		} catch (URISyntaxException e) {
			fail();
		}
	}

	@Test
	public void testArgsParsingOne() {
		try {
			parser = new ArgsParser("-props", ClassLoader.getSystemResource("H2.properties").toURI().getPath(), "test");
			assertEquals(Locale.CANADA_FRENCH, parser.getLocale());
			assertTrue(parser.isIgnoreCrawlers());
			assertTrue(parser.isShowSql());
			assertTrue(parser.isShowSqlFormat());
			assertEquals("testname", parser.getUserName());
			assertEquals("testpass", parser.getPassWord());
			assertEquals("test", parser.getInputFile()[0].getName());
		} catch (CmdLineException e) {
			fail();
		} catch (URISyntaxException e) {
			fail();
		}
	}

	@Test
	public void testArgsParsingTwo() {
		try {
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("Test2.xml").toURI().getPath(), "testtwo");
			assertEquals("org.hibernate.dialect.H2Dialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("testtwo", parser.getInputFile()[0].getName());
			assertEquals("jdbc:h2:./h2DB", parser.getDatabaseUrl().toString());
			assertEquals(ArgsParser.DdlOperation.Create, parser.getOperation());
		} catch (CmdLineException e) {
			e.printStackTrace();
			fail();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testArgsParsingThree() {
		try {
			parser = new ArgsParser("-props", ClassLoader.getSystemResource("H2.properties").toURI().getPath(), "test");
			assertEquals("org.dialect.SQLiteDialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("test", parser.getInputFile()[0].getName());
			assertEquals("jdbc:h2:./h2DB", parser.getDatabaseUrl().toString());
			assertEquals(ArgsParser.DdlOperation.Create, parser.getOperation());
		} catch (CmdLineException e) {
			e.printStackTrace();
			fail();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testArgsParsingFour() {
		try {
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("Test3.xml").toURI().getPath(), "test");
			assertEquals("org.hibernate.dialect.H2Dialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("test", parser.getInputFile()[0].getName());
			assertEquals("jdbc:h2:./h2DB", parser.getDatabaseUrl().toString());
			assertEquals(ArgsParser.DdlOperation.Create, parser.getOperation());
		} catch (CmdLineException e) {
			e.printStackTrace();
			fail();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testArgsParsingFive() {
		try {
			String file1 = ClassLoader.getSystemResource("ex051222.log").toURI().getPath();
			String file2 = ClassLoader.getSystemResource("ex061023.log").toURI().getPath();
			String file3 = ClassLoader.getSystemResource("ex071130.log").toURI().getPath();
			parser = new ArgsParser(file1);
			assertEquals("ex051222.log", parser.getInputFile()[0].getName());
			assertEquals(1, parser.getInputFile().length);
			parser = new ArgsParser(file1, file2);
			assertEquals(2, parser.getInputFile().length);
			assertEquals("ex051222.log", parser.getInputFile()[0].getName());
			assertEquals("ex061023.log", parser.getInputFile()[1].getName());
			parser = new ArgsParser(file1, file2, file3);
			assertEquals(3, parser.getInputFile().length);
			assertEquals("ex051222.log", parser.getInputFile()[0].getName());
			assertEquals("ex061023.log", parser.getInputFile()[1].getName());
			assertEquals("ex071130.log", parser.getInputFile()[2].getName());
		} catch (CmdLineException e) {
			e.printStackTrace();
			fail();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSome() throws IOException, ClassNotFoundException {
		Class[] cArray = getClasses("org.sessionization.parser", ClassLoader.getSystemClassLoader());
	}
}