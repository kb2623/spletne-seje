package org.datastruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparseMatrix<Index, Value> {

	private Map<Entry, Integer> indexes;
	private List<Value> values;

	public SparseMatrix() {
		indexes = new HashMap<>();
		values = new ArrayList<>();
	}

	public void put(Index i, Index j, Value value) {
		Entry<Index> entry = new Entry(i, j);
		indexes.put(entry, values.size());
		values.add(value);
	}

	public Value get(Index i, Index j) {
		Entry<Index> entry = new Entry(i, j);
		Integer index = indexes.get(entry);
		return values.get(index);
	}

	class Entry<Index> {

		private Index i;
		private Index j;

		Entry(Index i, Index j) {
			this.i = i;
			this.j = j;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Entry)) return false;
			Entry<?> entru = (Entry<?>) o;
			if (i != null ? !i.equals(entru.i) : entru.i != null) return false;
			if (j != null ? !j.equals(entru.j) : entru.j != null) return false;
			return true;
		}

		@Override
		public int hashCode() {
			int result = i != null ? i.hashCode() : 0;
			result = 31 * result + (j != null ? j.hashCode() : 0);
			return result;
		}
	}

}
