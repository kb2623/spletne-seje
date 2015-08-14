package org.oosql.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Repeatable(ArrayTables.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayTable {
	/**
	 *
	 * @return
	 */
	Table arrayTable() default @Table(
			id = @Column(name = "array_id", pk = true)
	);
	/**
	 *
	 * @return
	 */
	Table valueTable() default @Table(
			id = @Column(pk = true),
			columns = @Columns({
					@Column(name = "dim_", pk = true),
					@Column(name = "vaule")
			})
	);
}
