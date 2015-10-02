package org.sessionization.parser;

import org.junit.Test;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * Created by klemen on 2.10.2015.
 */
public class ArgsParserTest {

	private ArgsParser parser;

	@Test
	public void testArgsParsingOne() {
		try {
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/H2.cfg.xml").toURI().getPath());
			assertEquals("org.hibernate.dialect.H2Dialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("test", parser.getInputFile().getName());
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
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/H2.cfg.xml").toURI().getPath(), "-in", "testtwo");
			assertEquals("org.hibernate.dialect.H2Dialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("testtwo", parser.getInputFile().getName());
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
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/Test2.xml").toURI().getPath());
			assertEquals("org.hibernate.dialect.H2Dialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("test", parser.getInputFile().getName());
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
			parser = new ArgsParser("-xml", ClassLoader.getSystemResource("XML/Test3.xml").toURI().getPath());
			assertEquals("org.hibernate.dialect.H2Dialect", parser.getDialectClass());
			assertEquals("org.h2.Driver", parser.getDriverClass());
			assertEquals("test", parser.getInputFile().getName());
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
}