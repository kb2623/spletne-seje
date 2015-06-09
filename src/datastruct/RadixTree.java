package datastruct;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RadixTree<T> implements Map<String, T> {

	class RadixNode {

		private T data;
		private String key;
		private List<RadixNode> children;

		public RadixNode(T data, String key) {
			this.data = data;
			this.key = key;
			this.children = new LinkedList<>();
		}

		public RadixNode() {
			this(null, "");
		}

		public int getNumberOfMatchingCharacters(String key) {
			int numChar = 0;
			while(numChar < key.length() && numChar < this.key.length()) {
				if(key.charAt(numChar) != this.key.charAt(numChar)) {
					break;
				} else {
					numChar++;
				}
			}
			return numChar;
		}
	}

	private RadixNode rootNode;

	public RadixTree() {
		this.rootNode = new RadixNode();
	}

	public void add(T data, String key) throws NullPointerException, DuplicateKeyException {
		if(data != null && key != null) {
			this.insert(data, key, this.rootNode);
		} else {
			throw new NullPointerException();
		}
	}

	private void insert(T data, String key, RadixNode node) throws DuplicateKeyException {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(numMatchChar == 0 || (numMatchChar == node.key.length() && numMatchChar < key.length())) {
			boolean add = true;
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for(RadixNode child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					this.insert(data, ostanekKljuca, child);
					add = false;
					break;
				}
			}
			if(add) {
				node.children.add(new RadixNode(data, ostanekKljuca));
			}
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			if(node.data != null) {
				throw new DuplicateKeyException("Duplicate key: '"+key+"'");
			} else {
				node.data = data;
			}
		} else {
			RadixNode tmp1 = new RadixNode(node.data, node.key.substring(numMatchChar, node.key.length()));
			tmp1.children = node.children;
			node.key = key.substring(0, numMatchChar);
			node.children = new LinkedList<>();
			node.children.add(tmp1);
			if(numMatchChar < key.length()) {
				RadixNode tmp2 = new RadixNode(data, key.substring(numMatchChar, key.length()));
				node.data = null;
				node.children.add(tmp2);
			} else {
				node.data = data;
			}
		}
	}

	public T get(String key) {
		if(this.isEmpty()) {
			return null;
		} else {
			return this.findNode(key, this.rootNode);
		}
	}

	private T findNode(String key, RadixNode node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			return node.data;
		} else if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar >= node.key.length())) {
			String ostanekKjuca = key.substring(numMatchChar, key.length());
			for(RadixNode child : node.children) {
				if(child.key.charAt(0) == ostanekKjuca.charAt(0)) {
					return this.findNode(ostanekKjuca, child);
				}
			}
		}
		return null;
	}

	public T remove(String key) {
		if (!isEmpty()) {
			return deleteNode(key, null, this.rootNode);
		} else {
			return null;
		}
	}

	private T deleteNode(String key, RadixNode parent, RadixNode node) {
		int numMatchChar = node.getNumberOfMatchingCharacters(key);
		if(node.key.equals("") || (numMatchChar < key.length() && numMatchChar == node.key.length())) {
			String ostanekKljuca = key.substring(numMatchChar, key.length());
			for(RadixNode child : node.children) {
				if(child.key.charAt(0) == ostanekKljuca.charAt(0)) {
					return this.deleteNode(ostanekKljuca, node, child);
				}
			}
		} else if(numMatchChar == key.length() && numMatchChar == node.key.length()) {
			RadixNode tmp = null;
			if(node.data != null) {
				if(node.children.isEmpty()) {
					Iterator<RadixNode> it = parent.children.iterator();
					while(it.hasNext()) {
						tmp = it.next();
						if(tmp.key.equals(node.key)) {
							it.remove();
							break;
						}
					}
					if(parent.children.size() == 1 && parent.data == null && !parent.key.equals("")) {
						this.mergeNodes(parent, parent.children.get(0));
					}
				} else if(node.children.size() == 1) {
					this.mergeNodes(node, node.children.get(0));
				} else {
					node.data = null;
				}
				return tmp != null ? tmp.data : null;
			}
		}
		return null;
	}

	private void mergeNodes(RadixNode parent, RadixNode child) {
		parent.key += child.key;
		parent.data = child.data;
		parent.children = child.children;
	}
	/**
	 * Returns the number of elements in this collection. If this collection contains more than Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
	 *
	 * @return the number of elements in this map
	 */
	@Override
	public int size() {
		return count();
	}

	public boolean isEmpty() {
		return this.rootNode.children.isEmpty();
	}
	/**
	 * Returns true if this map contains a mapping for the specified key. More formally, returns true if and only if this map contains a mapping for a key k such that (key==null ? k==null : key.equals(k)). (There can be at most one such mapping.)
	 *
	 * @param key key whose presence in this map is to be tested
	 * @return true if this map contains a mapping for the specified key
	 */
	@Override
	public boolean containsKey(Object key) {
		return (key instanceof String ? get((String) key) : null) != null;
	}
	/**
	 * Returns true if this map maps one or more keys to the specified value. More formally, returns true if and only if this map contains at least one mapping to a value v such that (value==null ? v==null : value.equals(v)). This operation will probably require time linear in the map size for most implementations of the Map interface.
	 *
	 * @param value value whose presence in this map is to be tested
	 * @return
	 */
	@Override
	public boolean containsValue(Object value) {
		return false;
	}
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * More formally, if this map contains a mapping from a key k to a value v such that (key==null ? k==null : key.equals(k)), then this method returns v; otherwise it returns null. (There can be at most one such mapping.)
	 * If this map permits null values, then a return value of null does not necessarily indicate that the map contains no mapping for the key; it's also possible that the map explicitly maps the key to null. The containsKey operation may be used to distinguish these two cases.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
	 */
	@Override
	public T get(Object key) {
		return key instanceof String ? get((String) key) : null;
	}
	/**
	 * Associates the specified value with the specified key in this map.
	 *
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return new value associated with key, or null if mapping exists or one of parameters where null
	 */
	@Override
	public T put(String key, T value) {
		try {
			add(key, value);
			return t;
		} catch (NullPointerException | DuplicateKeyException e) {
			return null;
		}
	}
	/**
	 * Removes the mapping for a key from this map if it is present. More formally, if this map contains a mapping from key k to value v such that (key==null ? k==null : key.equals(k)), that mapping is removed. (The map can contain at most one such mapping.)
	 * Returns the value to which this map previously associated the key, or null if the map contained no mapping for the key.
	 *  If this map permits null values, then a return value of null does not necessarily indicate that the map contained no mapping for the key; it's also possible that the map explicitly mapped the key to null.
	 *  The map will not contain a mapping for the specified key once the call returns.
	 *
	 * @param key key whose mapping is to be removed from the map
	 * @return the previous value associated with key, or null if there was no mapping for key
	 */
	@Override
	public T remove(Object key) {
		return key instanceof String ? remove((String) key) : null;
	}
	/**
	 * Copies all of the mappings from the specified map to this map (optional operation). The effect of this call is equivalent to that of calling put(k, v) on this map once for each mapping from key k to value v in the specified map. The behavior of this operation is undefined if the specified map is modified while the operation is in progress.
	 *
	 * @param map mappings to be stored in this map
	 */
	@Override
	public void putAll(Map<? extends String, ? extends T> map) {

	}
	/**
	 * Removes all of the mappings from this map (optional operation). The map will be empty after this call returns.
	 */
	@Override
	public void clear() {
		// FIXME remove all data from map
	}
	/**
	 * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll, and clear operations. It does not support the add or addAll operations.
	 *
	 * @return a set view of the keys contained in this map
	 */
	@Override
	public Set<String> keySet() {
		return null;
	}
	/**
	 * Returns a Collection view of the values contained in this map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa. If the map is modified while an iteration over the collection is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The collection supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Collection.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.
	 *
	 * @return a collection view of the values contained in this map
	 */
	@Override
	public Collection<T> values() {
		return null;
	}
	/**
	 * Returns a Set view of the mappings contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own remove operation, or through the setValue operation on a map entry returned by the iterator) the results of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.
	 *
	 * @return a set view of the mappings contained in this map
	 */
	@Override
	public Set<Entry<String, T>> entrySet() {
		return null;
	}
	/**
	 * Returns the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key.
	 *
	 * @param key the key whose associated value is to be returned
	 * @param value the default mapping of the key
	 * @return the value to which the specified key is mapped, or defaultValue if this map contains no mapping for the key
	 */
	@Override
	public T getOrDefault(Object key, T value) {
		return null;
	}
	/**
	 * Performs the given action for each entry in this map until all entries have been processed or the action throws an exception. Unless otherwise specified by the implementing class, actions are performed in the order of entry set iteration (if an iteration order is specified.) Exceptions thrown by the action are relayed to the caller.
	 *
	 * @param action The action to be performed for each entry
	 */
	@Override
	public void forEach(BiConsumer<? super String, ? super T> action) {

	}
	/**
	 * Replaces each entry's value with the result of invoking the given function on that entry until all entries have been processed or the function throws an exception. Exceptions thrown by the function are relayed to the caller.
	 *
	 * @param biFunction the function to apply to each entry
	 */
	@Override
	public void replaceAll(BiFunction<? super String, ? super T, ? extends T> biFunction) {

	}
	/**
	 * If the specified key is not already associated with a value (or is mapped to null) associates it with the given value and returns null, else returns the current value.
	 *
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping for the key. (A null return can also indicate that the map previously associated null with the key, if the implementation supports null values.)
	 */
	@Override
	public T putIfAbsent(String key, T value) {
		return null;
	}
	/**
	 * Removes the entry for the specified key only if it is currently mapped to the specified value.
	 *
	 * @param key key with which the specified value is associated
	 * @param value value expected to be associated with the specified key
	 * @return true if the value was removed
	 */
	@Override
	public boolean remove(Object key, Object value) {
		if (value != null && key != null && key instanceof String) {
			if (get(key).equals(value)) {
				remove(value);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	/**
	 * Replaces the entry for the specified key only if currently mapped to the specified value.
	 * @param key key with which the specified value is associated
	 * @param oldValue value expected to be associated with the specified key
	 * @param newValue value to be associated with the specified key
	 * @return true if the value was replaced
	 */
	@Override
	public boolean replace(String key, T oldValue, T newValue) {
		return false;
	}
	/**
	 * Replaces the entry for the specified key only if it is currently mapped to some value.
	 *
	 * @param key key with which the specified value is associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with the specified key, or null if there was no mapping for the key. (A null return can also indicate that the map previously associated null with the key, if the implementation supports null values.)
	 */
	@Override
	public T replace(String key, T value) {
		return null;
	}
	/**
	 * If the specified key is not already associated with a value (or is mapped to null), attempts to compute its value using the given mapping function and enters it into this map unless null.
	 * If the function returns null no mapping is recorded. If the function itself throws an (unchecked) exception, the exception is rethrown, and no mapping is recorded. The most common usage is to construct a new object serving as an initial mapped value or memoized result, as in:
	 * <code>map.computeIfAbsent(key, k -> new Value(f(k)));</code>
	 * Or to implement a multi-value map, <code>Map<K,Collection<V>></code>, supporting multiple values per key:
	 * <code>map.computeIfAbsent(key, k -> new HashSet<V>()).add(v);</code>
	 *
	 * @param key key with which the specified value is to be associated
	 * @param mappingFunction the function to compute a value
	 * @return the current (existing or computed) value associated with the specified key, or null if the computed value is null
	 */
	@Override
	public T computeIfAbsent(String key, Function<? super String, ? extends T> mappingFunction) {
		return null;
	}
	/**
	 * If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
	 * If the function returns null, the mapping is removed. If the function itself throws an (unchecked) exception, the exception is rethrown, and the current mapping is left unchanged.
	 *
	 * @param key key with which the specified value is to be associated
	 * @param remappingFunction the function to compute a value
	 * @return the new value associated with the specified key, or null if none
	 */
	@Override
	public T computeIfPresent(String key, BiFunction<? super String, ? super T, ? extends T> remappingFunction) {
		return null;
	}
	/**
	 * Attempts to compute a mapping for the specified key and its current mapped value (or null if there is no current mapping). For example, to either create or append a String msg to a value mapping:
	 * <code>map.compute(key, (k, v) -> (v == null) ? msg : v.concat(msg))</code>
	 * (Method merge() is often simpler to use for such purposes.)
	 * If the function returns null, the mapping is removed (or remains absent if initially absent). If the function itself throws an (unchecked) exception, the exception is rethrown, and the current mapping is left unchanged.
	 *
	 * @param key key with which the specified value is to be associated
	 * @param remappingFunction the function to compute a value
	 * @return
	 */
	@Override
	public T compute(String key, BiFunction<? super String, ? super T, ? extends T> remappingFunction) {
		return null;
	}
	/**
	 * If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value. Otherwise, replaces the associated value with the results of the given remapping function, or removes if the result is null. This method may be of use when combining multiple mapped values for a key. For example, to either create or append a String msg to a value mapping:
	 * <code>map.merge(key, msg, String::concat)</code>
	 * If the function returns null the mapping is removed. If the function itself throws an (unchecked) exception, the exception is rethrown, and the current mapping is left unchanged.
	 *
	 * @param key key with which the resulting value is to be associated
	 * @param value the non-null value to be merged with the existing value associated with the key or, if no existing value or a null value is associated with the key, to be associated with the key
	 * @param remappingFunction the function to recompute a value if present
	 * @return
	 */
	@Override
	public T merge(String key, T value, BiFunction<? super T, ? super T, ? extends T> remappingFunction) {
		return null;
	}

	public int count() {
		return this.count(this.rootNode);
	}

	private int count(RadixNode node) {
		int size = 0;
		if(node.data != null) {
			size++;
		}
		for(RadixNode child : node.children) {
			size += this.count(child);
		}
		return size;
	}

	public List<T> asList() {
		return this.asList(this.rootNode, new ArrayList<>());
	}

	private List<T> asList(RadixNode node, List<T> list) {
		for(RadixNode child : node.children) {
			this.asList(child, list);
		}
		if(node.data != null) {
			list.add(node.data);
		}
		return list;
	}

	@Deprecated
	public void printTree() {
		this.printTree(0, this.rootNode);
	}

	@Deprecated
	private void printTree(int len, RadixNode node) {
		System.out.print("|");
		for (int i = 0; i < len; i++) {
			System.out.print("_");
		}
		if (node.data != null) {
			System.out.printf("%s => [%s]%n", node.key, node.data.toString());
		} else {
			System.out.printf("%s%n", node.key);
		}
		for (RadixNode child : node.children) {
			this.printTree(len + node.key.length(), child);
		}
	}

	public Iterator<T> iterator() {
		return new RadixTree<T>.MyIterator();
	}

	private class MyIterator implements Iterator<T> {

		private final Stack<Iterator<RadixNode>> stackIt;
		private RadixNode next;

		public MyIterator() {
			this.stackIt = new Stack<>();
			this.stackIt.push(rootNode.children.iterator());
			if(!this.stackIt.peek().hasNext()) {
				this.next = null;
			} else {
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			}
		}

		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public T next() {
			if(!this.hasNext()) {
				return null;
			}
			T tmp = this.next.data;
			if(!this.next.children.isEmpty()) {
				this.stackIt.push(this.next.children.iterator());
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			} else if(this.stackIt.peek().hasNext()) {
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			} else {
				do{
					this.stackIt.pop();
					if(this.stackIt.isEmpty()) {
						this.next = null;
						return tmp;
					}
				} while(!this.stackIt.peek().hasNext());
				RadixNode tmpNode = this.stackIt.peek().next();
				while(tmpNode.data == null) {
					this.stackIt.push(tmpNode.children.iterator());
					tmpNode = this.stackIt.peek().next();
				}
				this.next = tmpNode;
			}
			return tmp;
		}
	}
}
