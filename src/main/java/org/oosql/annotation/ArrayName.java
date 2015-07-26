package org.oosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayName {
	/**
	 *
	 * @return
	 */
	String arrayName() default "";
	/**
	 *
	 * @return
	 */
	String valueName() default "";

	/**
	 *
	 * @return
	 */
	Column column() default @Column;
}
