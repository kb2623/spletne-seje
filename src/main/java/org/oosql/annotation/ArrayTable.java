package org.oosql.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayTable {
	/**
	 *
	 * @return
	 */
	Table arrayTable() default @Table(id = @Column(pk = true));
	/**
	 *
	 * @return
	 */
	Table valueTable() default @Table;
}
