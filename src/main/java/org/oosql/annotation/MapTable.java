package org.oosql.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Repeatable(MapTables.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapTable {
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
	ArrayTable[] arrayName() default {};
	/**
	 *
	 * @return
	 */
	Column[] columnName() default {};
}
