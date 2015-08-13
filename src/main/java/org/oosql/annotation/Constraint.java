package org.oosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
	/**
	 * Ime omejitve
	 * @return niz, ki predstavlja ime omejitve
	 */
	String name() default "";
}
