package org.oosql.annotation;

import java.lang.annotation.*;

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
	String dataType() default "";
}
