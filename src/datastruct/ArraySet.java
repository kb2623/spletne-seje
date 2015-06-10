package datastruct;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ArraySet<E> implements Set<E> {

    private Object[] array;
    private int size;

    public ArraySet() {
        array = null;
        size = 0;
    }

    public ArraySet(int capacity) {
        array = new Object[capacity];
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
            // FIXME !!! Nekaj je narobe pri razsirjanju tabele
            System.arraycopy(array, insertPoint, array, insertPoint + 1, size - insertPoint);
            array[insertPoint] = ele;
            size++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) throws ClassCastException, NullPointerException {
        if (o == null) throw new NullPointerException();
        int eleIndex = Arrays.binarySearch(array, o);
        if (eleIndex >= 0) {
            array[eleIndex] = null;
            array = Arrays.copyOf(array, --size);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) throws NullPointerException {
        if (collection == null) throw new NullPointerException();
        for (Object o : collection) if (!contains(o)) return false;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) throws NullPointerException, IllegalStateException, IllegalArgumentException {
        if (collection == null) throw new NullPointerException();
        collection.forEach(ele -> {
            try {
                if (!add(ele)) {
                    throw new IllegalStateException();
                }
            } catch (NullPointerException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        });
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) throws NullPointerException, ClassCastException {
        if (collection == null) throw new NullPointerException();
        collection.stream().filter(this::contains).forEach(this::remove);
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
    public boolean contains(Object o) {
        return Arrays.binarySearch(array, o) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int i;

            {
                i = 0;
            }

            @Override
            public boolean hasNext() {
                try {
                    return array[i] != null;
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }

            @Override
            public E next() {
                try {
                    return (E) array[i++];
                } catch (ArrayIndexOutOfBoundsException e) {
                    return null;
                }
            }
        };
    }

    @Override
    public Object[] toArray() {
        return isEmpty() ? null : Arrays.copyOf(array, size);
    }

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
}
