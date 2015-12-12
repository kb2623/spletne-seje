package org.kohsuke.args4j;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;

/**
 * @see https://args4j.dev.java.net/issues/show_bug.cgi?id=10
 */
@SuppressWarnings("unused")
public class Issue10Test extends TestCase {
	@Option(name = "-enum", required = false, usage = "Enum2")
	private Enum crash;

	// The bug should be fixed with changing from manual printing to printf.
	public void testIssue10() {
		CmdLineParser parser = new CmdLineParser(this);
		parser.printUsage(new ByteArrayOutputStream());
		// occurred error: StringIndexOutOfBoundsException with index < 0

	}

	enum Enum {
		THIS, ENUM, HAS, A, VERY, LONG, USAGE, LINE,
		BECAUSE, OF, ITS, HUGE, LIST, Of, VALUES,
		SO, IT, WILL, CRASH
	}

}
