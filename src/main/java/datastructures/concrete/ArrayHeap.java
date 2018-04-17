package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int totItems;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.totItems = 0;
        this.heap = this.makeArrayOfT(50);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (!this.hasItem()) {
            throw new EmptyContainerException();
        }
        T item = this.heap[0];
        this.heap[0] = this.heap[totItems - 1];
        this.percolateDown(0);
        this.totItems--;
        return item;
    }

    @Override
    public T peekMin() {
        if (!this.hasItem()) {
            throw new EmptyContainerException();
        }
        return this.heap[0];
    }

    @Override
    public void insert(T item) {
    		if (item == null) {
    			throw new IllegalArgumentException();
    		}
        this.checkResize();
        this.totItems++;
        this.heap[this.totItems - 1] = item;
        this.percolateUp(this.totItems - 1);
    }
    
    public boolean hasItem() {
        return this.totItems != 0;
    }
    
    private void checkResize() {
        if (this.heap.length == this.totItems) {
            T[] tempArray = this.makeArrayOfT(this.heap.length * 2);
            for (int i = 0; i < this.heap.length; i++) {
                tempArray[i] = this.heap[i];
            }
            this.heap = tempArray;
        }
    }
    
    private void percolateUp(int index) {
        boolean finished = false;
        while (!finished && index > 0) {
            int parentIndex = (index - 1) / NUM_CHILDREN;
            if (this.heap[index].compareTo(this.heap[parentIndex]) < 0) {
                T temp = this.heap[index];
                this.heap[index] = this.heap[parentIndex];
                this.heap[parentIndex] = temp;
                index = parentIndex;
            } else {
                finished = true;
            }
        }
    }
    
    private void percolateDown(int index) {
        boolean finished = false;
        while (!finished) {
            int childIndex = (index * NUM_CHILDREN) + 1;
            int minItemIndex = index;
            while (childIndex <= this.totItems - 1 && childIndex <= (index * NUM_CHILDREN) + NUM_CHILDREN) {
                if (this.heap[childIndex].compareTo(this.heap[minItemIndex]) < 0) {
                    minItemIndex = childIndex;
                }
                childIndex++;
            }
            if (this.heap[index].compareTo(this.heap[minItemIndex]) > 0) {
                T minWork = this.heap[minItemIndex];
                this.heap[minItemIndex] = this.heap[index];
                this.heap[index] = minWork;
                index = minItemIndex;
            } else {
                finished = true;
            }
        }
    }
    
    @Override
    public int size() {
        return this.totItems;
    }
}
