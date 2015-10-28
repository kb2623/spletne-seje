package org.sessionization.fields;

public interface LogField {
	/**
	 *
	 * @return
	 */
	String izpis();
	/**
	 *
	 * @return
	 */
	default String getKey() {
		return "";
	}
}
