package org.oosqljet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 *
	 * @return
	 */
	String[] name() default "";
	/**
	 *
	 * @return
	 */
	String[] arrayName() default "";
	/**
	 *
	 * @return
	 */
	String[] mapName() default "";
	/**
	 *
	 * @return
	 */
	boolean primaryKey() default false;
	/**
	 *
	 * @return
	 */
	String[] index() default "";
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

}
