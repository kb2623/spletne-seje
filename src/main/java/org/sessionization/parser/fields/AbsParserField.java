package org.sessionization.parser.fields;

import org.sessionization.fields.LogField;

import java.io.Reader;

public abstract class AbsParserField {

	private String prefix;
	private Character endChar;
	private String varName;

	public AbsParserField() {
		prefix = null;
		endChar = null;
		varName = null;
	}

	/**
	 * @return
	 */
	public boolean isKey() {
		return false;
	}

	public String getSetterName() {
		Class c = getCClass();
		if (c != null) {
			return "set" + c.getSimpleName();
		} else {
			return null;
		}
	}

	public String getGettrName() {
		Class c = getCClass();
		if (c != null) {
			return "get" + c.getSimpleName();
		} else {
			return null;
		}
	}

	public String getFieldName() {
		Class c = getCClass();
		if (c != null) {
			String name = c.getSimpleName();
			return Character.toLowerCase(name.charAt(0)) + name.substring(1);
		} else {
			return null;
		}
	}

	public Class[] getDependencies() {
		return new Class[0];
	}

	public Character getEndChar() {
		return endChar;
	}

	public void setEndChar(Character endChar) {
		this.endChar = endChar;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	/**
	 * @return
	 */
	public abstract String getFormatString();

	/**
	 * @param reader
	 * @return
	 */
	public abstract LogField parse(Reader reader);

	/**
	 * @return
	 */
	public abstract Class<? extends LogField> getCClass();

}
