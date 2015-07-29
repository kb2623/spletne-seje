package org.oosql.annotation;

import java.lang.annotation.*;
import java.sql.JDBCType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 *
	 * @return
	 */
	String[] name() default {};
	/**
	 *
	 * @return
	 */
	boolean pk() default false;
	/**
	 *
	 * @return
	 */
	boolean notNull() default false;
	/**
	 *
	 * @return
	 */
	boolean unique() default false;
	/**
	 *
	 * @return
	 */
	JDBCType type() default JDBCType.INTEGER;
	/**
	 *
	 * @return
	 */
	int typeLen() default 0;
	/**
	 *
	 * @return
	 */
	String constaraintName() default "";
}
