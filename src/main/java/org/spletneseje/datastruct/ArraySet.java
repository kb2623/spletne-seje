package org.spletneseje.datastruct;

import java.util.*;

public class ArraySet<E> implements Set<E> {

    private Object[] array;
    private int size;

    public ArraySet() {
        array = null;
        size = 0;
    }

    public ArraySet(Collection<? extends E> collection) {
        addAll(collection);
    }

    @Override
    public boolean add(E ele) throws NullPointerException {
        if (ele == null) throw new NullPointerException();
        if (isEmpty()) {
            array = new Object[++size];
            array[0] = ele;
            return true;
        }
        int insertPoint = Arrays.binarySearch(array, 0, size, ele);
        if (insertPoint < 0) {
            insertPoint = - insertPoint - 1;
            array = Arrays.copyOf(array, ++size);
            System.arraycopy(array, insertPoint, array, insertPoint + 1, size - 1 - insertPoint);
            array[insertPoint] = ele;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) throws ClassCastException, NullPointerException {
        if (o == null) throw new NullPointerException();
        int eleIndex = Arrays.binarySearch(array, o);
        if (eleIndex == 0) {
            array = Arrays.copyOfRange(array, 1, size--);
            return true;
        } else if (eleIndex > 0) {
            array[eleIndex] = null;
            array = copyOf(array, --size);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) throws NullPointerException {
        if (collection == null) throw new NullPointerException();
        return collection.stream().filter(e -> !contains(e)).count() == 0;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) throws NullPointerException, IllegalStateException {
        if (collection == null) throw new NullPointerException();
        collection.forEach(ele -> {
            if (!add(ele)) throw new IllegalStateException();
        });
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) throws NullPointerException, ClassCastException {
        if (collection == null) throw new NullPointerException();
        boolean fix = false;
        for (int i = 0; i < size; i++) if (!collection.contains(array[i])) {
            fix = true;
            array[i] = null;
        }
        if (fix) array = copyOf(array, 0);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> collection) throws NullPointerException, ClassCastException {
        if (collection == null) throw new NullPointerException();
        collection.forEach(this::remove);
        return true;
    }

    @Override
    public void clear() {
        array = null;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public E get(int position) {
        return (E) array[position];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) throws NullPointerException, ClassCastException {
        if (o == null) throw new NullPointerException();
        return Arrays.binarySearch(array, o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int i = 0;

            @Override
            public boolean hasNext() {
                try {
                    return array[i] != null;
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() throws NoSuchElementException {
                try {
                    return (E) array[i++];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    @Override
    public Object[] toArray() {
        return !isEmpty() ? Arrays.copyOf(array, size) : new Object[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] t) throws NullPointerException, ArrayStoreException {
        if (t == null) throw new NullPointerException();
        if (t.length == size) {
            System.arraycopy(array, 0, t, 0, size);
            return t;
        } else {
            try {
                return (T[]) Arrays.copyOf(array, size);
            } catch (ClassCastException e) {
                throw new ArrayStoreException(e.getMessage());
            }
        }
    }
    /**
     * Metoda, ki ustvari novo tabelo z novo velikostjo <code>newLength</code>. Metoda preskoci vese <code>null</code> objekte. Ce ima stara tabela vec elementov, kot pa je verednost <code>newLength</code>, potem se prekopirajo samo elementi od indeksa <code>0</code> pa do <code>newLength - 1 + numberOfSkippedNullElements</code>. V primeru ko je <code>newLength > array.length</code> se uposteva <code>array.length</code>.
     * Če <code>newLength == 0</code> potem <code>array</code> vebuje null vrednost, ki jih bi radi izbrisali iz tabele in ohranili ne <code>null</code> versnosti.
     *
     * @param array Tabela s katere prepisujemo elemente
     * @param newLength Velikost nove tabele
     * @return Nova tabela, ki ne vesebuje <code>null</code> elementov
     */
    private Object[] copyOf(Object[] array, int newLength) {
        if (newLength == 0) {
            LinkedList<Object> list = new LinkedList<>();
            for (Object o : array) {
                if (o != null) list.add(o);
            }
            return list.toArray();
        } else {
            Object[] nArray = new Object[newLength];
            for (int i = 0, j = 0; j < newLength && i < array.length && j < array.length; j++, i++) {
                while (array[i] == null) i++;
                nArray[j] = array[i];
            }
            return nArray;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder().append('[');
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            builder.append(it.next());
            if (it.hasNext()) builder.append(',').append(' ');
        }
        return builder.append(']').toString();
    }
}
