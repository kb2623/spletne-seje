package org.oosql.tree;

import org.oosql.annotation.Column;

public class VrsticaNavadna implements Vrstica {

	private String name;
	private String type;
	private Column anno;

	public VrsticaNavadna(String name, String type, Column anno) {
		this.name = name;
		this.type = type;
		this.anno = anno;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isPrimaryKey() {
		return anno.pk();
	}

	public boolean isNotNull() {
		return anno.notNull();
	}

	public boolean isUnique() {
		return anno.unique();
	}
}
