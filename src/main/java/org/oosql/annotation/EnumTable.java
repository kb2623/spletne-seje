package org.oosql.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.sql.JDBCType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumTable {
	/**
	 *
	 * @return
	 */
	Column keyColumn() default @Column(pk = true);
	/**
	 *
	 * @return
	 */
	Column valueColumn() default @Column(type = JDBCType.VARCHAR, typeLen = 25);
}
