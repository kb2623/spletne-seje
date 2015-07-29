package org.oosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**
	 *
	 * @return
	 */
	String name() default "";
	/**
	 *
	 * @return
	 */
	boolean noRowId() default false;
	/**
	 *
	 * @return
	 */
	boolean id() default false;
	/**
	 *
	 * @return
	 */
	JDBCType idType() default JDBCType.INTEGER;
	/**
	 *
	 * @return
	 */
	String pkConstraintName() default "";
}
