package org.oosql.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.sql.JDBCType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayTable {
	/**
	 *
	 * @return
	 */
	Table arrayTable() default @Table(id = @Column(name = {"array_id"}, pk = true));
	/**
	 *
	 * @return
	 */
	Table valueTable() default @Table;
	/**
	 *
	 * @return
	 */
	Column arrayid() default @Column;
	/**
	 *
	 * @return
	 */
	String dimPrefix() default "";
	/**
	 *
	 * @return
	 */
	JDBCType dimType() default JDBCType.INTEGER;
	/**
	 *
	 * @return
	 */
	int dimLen() default 0;
	/**
	 *
	 * @return
	 */
	Column valueColum() default @Column;
	/**
	 *
	 * @return
	 */
	EnumTable enumColumn() default @EnumTable;
}
