package org.oosqljet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entry {
    /**
     *
     * @return
     */
    String[] name() default "";
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
    boolean notNull() default true;
    /**
     *
     * @return
     */
    boolean unique() default false;
	/**
	 *
	 * @return
	 */
	boolean autoincrement() default false;
    /**
     *
     * @return
     */
    Direction increment() default Direction.ASC;

}
