package org.sessionization.parser;

import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ArgsParserTest {

	private ArgsParser parser;

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
			e.printStackTrace();
			fail();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testArgsParsingOne() {
		try {
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/H2.cfg.xml").toURI().getPath(), "test");
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
	public void testArgsParsingTwo() {
		try {
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/H2.cfg.xml").toURI().getPath(), "testtwo");
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
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/Test2.xml").toURI().getPath(), "test");
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
	public void testArgsParsingFour() {
		try {
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/Test3.xml").toURI().getPath(), "test");
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
			String file1 = ClassLoader.getSystemResource("Logs/Extended/ex051222.log").toURI().getPath();
			String file2 = ClassLoader.getSystemResource("Logs/Extended/ex061023.log").toURI().getPath();
			String file3 = ClassLoader.getSystemResource("Logs/Extended/ex071130.log").toURI().getPath();
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
}