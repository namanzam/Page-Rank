package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */ 
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int totItems;
    private int capacity;
    private static final int DEFAULTSIZE = 50;
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.chains = this.makeArrayOfChains(DEFAULTSIZE);
        this.capacity = chains.length;
        this.totItems = 0;
    }
    
    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        return (IDictionary<K, V>[]) new IDictionary[size];
    }
    
    private int getIndex(K key) {
        return this.getIndex(key, this.capacity);
    }
    
    private int getIndex(K key, int length) {
        if (key == null) { // uses index 0 as null hash code
            return 0;
        } else {
            return Math.abs(key.hashCode() % length);
        }
    }

    @Override
    public V get(K key) {
        int index = this.getIndex(key);
        if (!this.containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[index].get(key);
    }

    @Override
    public void put(K key, V value) {
        int index = this.getIndex(key);
        if (chains[index] == null) {
            IDictionary<K, V> tempArray = new ArrayDictionary<>();
            this.chains[index] = tempArray;
        }
        if (!containsKey(key)) {
            this.totItems++;
        }
        this.chains[index].put(key, value);
        if (this.totItems / this.chains.length >= 1) {
            resize();
        }
    }
    
    private void resize() {
        IDictionary<K, V>[] tempChains = makeArrayOfChains(2*chains.length);
        for (int i = 0; i < chains.length; i++) {
            if (chains[i] != null) {
                IDictionary<K, V> bucket = chains[i];
                for (KVPair<K, V> pair: bucket) {
                    int index = getIndex(pair.getKey(), tempChains.length);
                    if (tempChains[index] == null) {
                        tempChains[index] = new ArrayDictionary<>();
                    }
                    tempChains[index].put(pair.getKey(), pair.getValue());
                }
            }
        }
        this.chains = tempChains;
        this.capacity = tempChains.length;
    }

    @Override
    public V remove(K key) {
        int index = this.getIndex(key);
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        this.totItems--;
        return chains[index].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int index = this.getIndex(key);
        if (chains[index] != null) {
            return chains[index].containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return this.totItems;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 4. Think about what exactly your *invariants* are. As a 
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int current;
        private Iterator<KVPair<K, V>> iter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.current = 0;
            this.helper();
        }

        @Override
        public boolean hasNext() {
            if (iter == null) {
                return false;
            }
            return this.iter.hasNext();
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> item = this.iter.next();
            if (!hasNext()) { 
                this.current++; 
                this.helper();
            }
            return item; 
        }
        
        private void helper() {
            while (this.current < this.chains.length && this.chains[this.current] == null) {
                this.current++;
            }
            if (this.current < this.chains.length) {
                this.iter = this.chains[this.current].iterator();
            }
        }
    }
}



