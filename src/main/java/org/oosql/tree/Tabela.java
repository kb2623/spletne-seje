package org.oosql.tree;

import org.oosql.Util;
import org.oosql.TableClass;
import org.oosql.EntryClass;
import org.oosql.annotation.ColumnClass;
import org.oosql.exception.OosqlException;
import org.oosql.exception.ColumnAnnotationException;
import org.oosql.exception.TableAnnotationException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class Tabela {

	private String tableName;
	private List<Vrstica> columns;

	public Tabela(Object in, SqlDataType types) throws OosqlException, NullPointerException {
		if (types == null)
			throw new NullPointerException("Null pointer for types not allowed!!!");
		TableClass table = Util.getTableAnnotation(in);
		if (table == null)
			throw new TableAnnotationException("Table annotation missing in \"" + in.getClass().getName() + "\"!!!");
		else
			tableName = table.getName();
		Map<String, EntryClass> entrys = Util.getEntryAnnotations(in);
		if (entrys == null)
			throw new ColumnAnnotationException("Column annotations missing in \"" + in.getClass().getName() + "\"!!!");
		else
			columns = new LinkedList<>();
		if (table.getAnno().autoId())
			columns.add(new VrsticaNavadna(tableName, types.makeDataType(Integer.class), new ColumnClass(true)));
		entrys.values().forEach(e -> procesEntry(e, types));
	}

	private Tabela(String tableName, SqlDataType types) {
		this.tableName = tableName;
		columns = new LinkedList<>();
		columns.add(new VrsticaNavadna(tableName + "id", types.makeDataType(Integer.class), new ColumnClass(true)));
		columns.add(new VrsticaNavadna(tableName, types.makeDataType(String.class), new ColumnClass(false)));
	}

	private void procesEntry(EntryClass e, SqlDataType types) {
		String type = types.makeDataType(e.getType());
		if (type != null) {
			columns.add(new VrsticaNavadna(e.getName(0), type, e.columnAnno()));
		} else if (e.getType().isEnum()) {
			TableClass table = Util.getTableAnnotation(e.get());
			if (table == null) {
				columns.add(new VrsticaNavadna(e.getName(0), types.makeDataType(String.class), e.columnAnno()));
			} else {
				List<VrsticaNavadna> list = new LinkedList<>();
				list.add(new VrsticaNavadna(e.getName(0), types.makeDataType(Integer.class), e.columnAnno()));
				columns.add(new VrsticaReferenca(list, new Tabela(table.getName(), types)));
			}
		} else if (e.getType().isArray()) {
			// Imamo tabelo
		} else if (Collection.class.isAssignableFrom(e.getType())) {
			// Imamo seznam
		} else if (Map.class.isAssignableFrom(e.getType())) {
			// Imamo slovar
		} else {
			// Imamo nov objekt
			// nazaj dobimo nov objekt tipa SqlTable, po katerem se lahko sprehodimo da dobimo nove vrstice v tabeli, ter jih povežemo
		}
	}

	public String getTableName() {
		return tableName;
	}

	public String getReferences() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// TODO
		return builder.toString();
	}
}
