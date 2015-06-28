/**
 * 
 */
package org.kohsuke.args4j;

public class Bean {
	@Option(name = "-text")
	String text = "default";

	@Option(name = "-number")
	int number = -1;
}