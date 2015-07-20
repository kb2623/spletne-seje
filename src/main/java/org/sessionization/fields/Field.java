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
	default FieldType getFieldType() {
		return null;
	}
}
