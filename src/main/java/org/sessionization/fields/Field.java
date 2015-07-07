package org.sessionization.fields;

public interface Field {
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
		return null;
	}
	/**
	 *
	 * @return
	 */
	FieldType getFieldType();
}
