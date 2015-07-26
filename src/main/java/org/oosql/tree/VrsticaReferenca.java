package org.oosql.tree;

import java.util.List;

public class VrsticaReferenca implements Vrstica {

	private List<VrsticaNavadna> columns;
	private Tabela refTable;

	public VrsticaReferenca(List<VrsticaNavadna> columns, Tabela table) {
		this.columns = columns;
		refTable = table;
	}

	public VrsticaReferenca(List<VrsticaNavadna> columns) {
		this(columns, null);
	}

	public List<VrsticaNavadna> getColumns() {
		return columns;
	}

	public Tabela getRefTable() {
		return refTable;
	}
}
