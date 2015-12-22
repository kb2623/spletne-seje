package org.sessionization.parser;

public interface LogField {
	/**
	 * @return
	 */
	String izpis();

	/**
	 * @return
	 */
	default String getKey() {
		return "";
	}
}
