package org.sessionization.parser.datastruct;

public abstract class AbsUserId {
	/**
	 * @return
	 */
	public abstract String getKey();

	/**
	 * @param line
	 * @return
	 */
	public abstract boolean addParsedLine(ParsedLine line);
}
