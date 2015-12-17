package org.sessionization.parser.datastruct;

import org.sessionization.TimePoint;

public abstract class AbsUserId implements TimePoint {
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
