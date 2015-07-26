package org.oosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapName {
	/**
	 *
	 * @return
	 */
	String value() default "";
	/**
	 *
	 * @return
	 */
	String key() default "";
	/**
	 *
	 * @return
	 */
	ArrayName[] arrayName() default {};
	/**
	 *
	 * @return
	 */
	EnumName[] enumName() default {};
}