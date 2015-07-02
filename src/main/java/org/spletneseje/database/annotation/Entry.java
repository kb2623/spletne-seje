package org.spletneseje.database.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Entry {
    /**
     *
     * @return
     */
    String name() default "";
    /**
     *
     * @return
     */
    boolean primaryKey() default false;
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
    Direction autoincrement() default Direction.ASC;

}
