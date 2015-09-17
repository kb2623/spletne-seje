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
		return "";
	}
	/**
	 *
	 * @return
	 */
	default FieldType getFieldType() {
		return FieldType.Unknown;
	}
}
