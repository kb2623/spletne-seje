package org.spletneseje.datastruct;

public class LinkedQueue<E> {

    class QueueNode {

        private E data;
        private QueueNode prev;

        public QueueNode(E data, QueueNode prev) {
            this.data = data;
            this.prev = prev;
        }
    }

    private QueueNode first;
    private QueueNode last;

    public LinkedQueue() {
        first = null;
        last = null;
    }
    /**
     * Inserts the specified element into this queue if it is possible to do so immediately without violating capacity restrictions, returning true upon success.
     *
     * @param e the element to add
     * @return
     *      True -> element added
     *      False -> element can't be added
     */
    public boolean add(E e) {
        try {
            if (isEmpty()) {
                first = last = new QueueNode(e, null);
            } else if (first == last) {
                last = new QueueNode(e, null);
                first.prev = last;
            } else {
                QueueNode tmp = last;
                last = new QueueNode(e, null);
                tmp.prev = last;
            }
            return true;
        } catch (Exception err) {
            return false;
        }
    }
    /**
     * Inserts the specified element into this queue if it is possible to do so immediately without violating capacity restrictions, returning true upon success.
     *
     * @param e the element to add
     * @return
     *      True -> element added
     *      False -> element can't be added
     */
    public boolean offer(E e) {
        return add(e);
    }
    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    public E peek() {
        if (isEmpty()) return null;
        return first.data;
    }
    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of this queue, or null if this queue is empty
     */
    public E poll() {
        if (isEmpty()) return null;
        E tmp = first.data;
        if (last == first) {
            first = last = null;
        } else if (first.prev == last) {
            first = last;
        } else {
            first = first.prev;
        }
        return tmp;
    }
    /**
     * Metoda pove ali je vrsta prazna
     *
     * @return True če je vrsta prazna, False če vrsta ni prazna
     */
    public boolean isEmpty() {
        return last == null && first == null;
    }

    public void pushBack() {
        if (isEmpty() || last == first) return;
        QueueNode tmp = first;
        first = first.prev;
        last = last.prev = tmp;
    }
}
