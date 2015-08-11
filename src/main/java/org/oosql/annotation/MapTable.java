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
	Column valueColumn() default @Column;
	/**
	 *
	 * @return
	 */
	Column keyColumn() default @Column;
}
