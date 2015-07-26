package org.oosql.tree;

import java.util.List;

public class VrsticaTabela extends VrsticaReferenca {

	private Tabela tabelaVrednost;
	private List<VrsticaNavadna> vresnot;

	public VrsticaTabela(List<VrsticaNavadna> columns, Tabela table) {
		super(columns, table);
	}
}
