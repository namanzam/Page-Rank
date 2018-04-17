// Naman Mehra & Jonah Au FINAL
package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */

//test
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (this.size == 0) {
                this.front = new Node<T>(item);
                this.back = this.front;
        }else {
                this.back.next = new Node<T>(this.back, item, null);
                this.back = this.back.next;
        }
        this.size++;
        
    }

    @Override
    public T remove() {
        if (this.size == 0) {
                throw new EmptyContainerException();
        }
        T tempData = this.back.data;
        if (this.size == 1) {
                this.front = null;
                this.back = null;
        }else {
                this.back = this.back.prev;
                this.back.next = null;
        }
        this.size--;
        return tempData;
    }
    
    private void throwException(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public T get(int index) {
        this.throwException(index);
        return this.finder(index).data;
    }

    @Override
    public void set(int index, T item) {
            this.throwException(index);
            if (index == 0) {
                if (this.size() == 1) {
                    this.front = new Node<T>(null, item, null);
                }else {
                this.front = new Node<T>(null, item, this.front.next);
                this.front.next.prev = this.front;
                }   
            }else if (index == this.size - 1) {
                this.back = new Node<>(this.back.prev, item, null);
                this.back.prev.next = this.back;
            } else {
                Node<T> temp = this.finder(index);
                Node<T> newNode = new Node<>(temp.prev, item, temp.next);
                temp.prev.next = newNode;
                temp.next.prev = newNode;
            }
    }
    
    // returns the node at the given index
    private Node<T> finder(int index) {
        Node<T> temp;
        int dif = this.size() - index;
        if (dif > (this.size() / 2)) {
            temp = this.front;
            for (int i = 0; i < index; i++) {
                    temp = temp.next;
            }
        }else {
                temp = this.back;
                for (int i = this.size() - 1; i > index; i--) {
                    temp = temp.prev;
                }
        }
        return temp;
    }

    @Override
    public void insert(int index, T item) {
            if (index < 0 || index >= this.size + 1) {
            throw new IndexOutOfBoundsException();
        }
            if (index == 0) {
                if (this.front == null) {
                    add(item);
                }else {
                    this.front = new Node<T>(null, item, this.front);
                    this.front.next.prev = this.front;
                    this.size++;
                }
            }else if (index == this.size) {
                this.add(item);
            }else {
                Node<T> temp = this.finder(index - 1);
                Node<T> newNode = new Node<T>(temp, item, temp.next);
                temp.next.prev = newNode;
                temp.next = newNode;
                this.size++;
            }
            
    }

    @Override
    public T delete(int index) {
        this.throwException(index);
        T value;
        if (index == 0) {
                value = this.front.data;
                if (this.size() == 1) {
                    this.remove();
                }else {
                    this.front = this.front.next;
                    this.front.prev = null;
                    this.size--;
                }
        }else if (index == this.size() - 1) {
                value = this.back.data;
                this.remove();
        }else {
                Node<T> temp = this.finder(index);
                value = temp.data;
                temp.prev.next = temp.next;
                temp.next.prev = temp.prev;
                this.size--;
        }
        return value;
    }
    
    @Override
    public int indexOf(T item) {
        Node<T> temp = this.front;
        for (int i = 0; i < this.size(); i++) {
                if (temp.data == item || temp.data.equals(item)) {
                    return i;
                }
                temp = temp.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
            if (this.indexOf(other) > -1) {
                return true;
            }
            return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
            T temp = current.data;
            current = current.next;
            return temp;
        }
    }
}
