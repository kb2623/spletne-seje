package org.sessionization.parser;

import org.datastruct.concurrent.ObjectPool;

import java.text.ParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public interface LogFieldType {
	/**
	 * @return
	 */
	default boolean isKey() {
		return false;
	}

	/**
	 * @return
	 */
	Class<LogField> getClassE();

	/**
	 * @return
	 */
	default String getFieldName() {
		String s = getClassE().getSimpleName();
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}

	/**
	 * @return
	 */
	default String getSetterName() {
		return "set" + getClassE().getSimpleName();
	}

	/**
	 * @return
	 */
	default String getGetterName() {
		return "get" + getClassE().getSimpleName();
	}

	/**
	 * @return
	 */
	Class[] getDependencies();

	/**
	 * @param queue
	 * @param parser
	 * @return
	 * @throws ParseException
	 */
	LogField parse(final Scanner scanner, final WebLogParser parser) throws ParseException;

	/**
	 * @return
	 */
	String[] getFormatString();

	/**
	 * @return
	 */
	Pattern getPattern();

	/**
	 *
	 * @return
	 */
	Map<Class, ObjectPool.ObjectCreator> getCreators();
}
