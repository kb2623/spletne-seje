package org.oosqljet;

import org.oosqljet.exception.EntryAnnotationException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ForeignKey {

	private class ListEntry {

		private class Name {

			private String name;
			private Name next;

			public Name(String name) {
				this.name = name;
				next = null;
			}

			public void shift() {
				if (next != null) {
					name = next.name;
					next = next.next;
				}
			}

			public void setNext(Name next) {
				if (this.next != null) {
					Name tmp = this.next;
					while (tmp.next != null) tmp = tmp.next;
					tmp.next = next;
				} else {
					this.next = next;
				}
			}

		}

		private Class type;
		private Name name;

		public ListEntry(Class type, String name) {
			this.type = type;
			this.name = new Name(name);
		}

		public Class type() {
			return type;
		}

		public String typeName() {
			return type.getSimpleName();
		}

		public String getName() {
			return name.name;
		}

		public void setName(String name, boolean shift) {
			if (shift) this.name.setNext(new Name(name));
			else this.name.name = name;
		}

		@Override
		public String toString() {
			return type.getSimpleName() + " > " + name.name;
		}
	}

	TableClass table;
	List<ListEntry> keys;

	public ForeignKey(TableClass table, List<ListEntry> keys) {
		this.table = table;
		this.keys = keys;
	}

	public ForeignKey(TableClass table) {
		this(table, new LinkedList<>());
	}

	public String getEntryQuery(EntryClass classEntry, Map<Class, SqlMapping> mappings) throws EntryAnnotationException, NoSuchMethodException {
		StringBuilder builder = new StringBuilder();
		String entryName;
		int i = 0;
		if (table.getAnnotation().autoId()) {
			entryName = classEntry.getName(i);
			if (entryName != null) {
				builder.append(entryName + " INTEGER,");
				i++;
			} else {
				return null;
			}
		}
		for (ListEntry e : keys) {
			if ((entryName = classEntry.getName(i)) != null) {
				SQLightDataType type = SQLightDataType.makeDataType(e.typeName());
				if (type == null) {
					if (e.type().isArray() || Collection.class.isAssignableFrom(e.type()) || Map.class.isAssignableFrom(e.type()))
						type = SQLightDataType.INTEGER;
					else if (e.type().isEnum()) {
						if (Util.getTableAnnotation(e.type()) != null)
							type = SQLightDataType.INTEGER;
						else
							type = SQLightDataType.TEXT;
					} else if (mappings.containsKey(e.type()))
						type = SQLightDataType.makeDataType(mappings.get(e.type()).getReturnType(e.type()).getSimpleName());
					else
						throw new EntryAnnotationException(e.typeName() + " >> " + e.getName() + " | " + e.toString());
				}
				builder.append(entryName + " " + type.toString() + ",");
				i++;
			} else {
				break;
			}
		}
		return builder.length() > 0 ?builder.deleteCharAt(builder.length() - 1).toString() : "";
	}

	public String getPrimaryKeys(EntryClass classEntry) {
		if (!classEntry.getAnnotation().primaryKey()) return "";
		StringBuilder builder = new StringBuilder();
		String entryName;
		int i = 0;
		if (table.getAnnotation().autoId()) {
			entryName = classEntry.getName(i);
			if (entryName != null)
				builder.append(table.getName() + "_id,");
			else
				return null;
		}
		for (; i < keys.size(); i++) {
			entryName = classEntry.getName(i);
			if (entryName != null)
				builder.append(entryName + ",");
			else
				break;
		}
		return builder.length() > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : "";
	}

	public String getRefQuery(EntryClass classEntry) {
		StringBuilder[] builder = {new StringBuilder(), new StringBuilder()};
		String entryName;
		int i = 0;
		if (table.getAnnotation().autoId()) {
			entryName = classEntry.getName(i);
			if (entryName != null) {
				builder[0].append(entryName + ",");
				builder[1].append(table.getName() + "_id,");
				i++;
			} else {
				return null;
			}
		}
		for (ListEntry e : keys) {
			entryName = classEntry.getName(i);
			if (entryName == null) break;
			builder[0].append(entryName + ",");
			builder[1].append(e.getName() + ",");
			i++;
		}
		for (StringBuilder b : builder) if (b.length() > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		return builder[0].length() > 0 ? "FOREIGN KEY(" + builder[0].toString() + ") REFERENCES " + table.getName() + "(" + builder[1].toString() + ")" : "";
	}

	public boolean add(EntryClass e) {
		return keys.add(new ListEntry(e.type(), e.getName(0)));
	}

	protected List<ListEntry> getList() {
		return keys;
	}

	public boolean addAll(ForeignKey list, EntryClass inEntry, boolean shift) {
		String entryName;
		int i = 0;
		for (ListEntry e : list.getList()) {
			entryName = inEntry.getName(i);
			if (entryName == null) return false;
			e.setName(entryName, shift);
			keys.add(e);
			i++;
		}
		return true;
	}

	public ForeignKey shiftAll() {
		keys.forEach(e -> e.name.shift());
		return this;
	}
}
