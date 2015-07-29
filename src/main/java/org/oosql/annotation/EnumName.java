package org.oosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumName {
	/**
	 *
	 * @return
	 */
	String name() default "";
	/**
	 *
	 * @return
	 */
	JDBCType pkType() default JDBCType.INTEGER;
	/**
	 *
	 * @return
	 */
	JDBCType valueType() default JDBCType.VARCHAR;
	/**
	 *
	 * @return
	 */
	int valueLength() default 10;
}
