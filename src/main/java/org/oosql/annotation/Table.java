package org.oosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
	boolean autoId() default false;
	/**
	 *
	 * @return
	 */
	String autoIdType() default "";
}
