package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import java.util.Iterator;
import java.util.NoSuchElementException;

import misc.exceptions.NoSuchKeyException;


/**
 * See IDictionary for more details on what this class should do.
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private static final int DEFAULT_SIZE= 50;
    // You're encouraged to add extra fields (and helper methods) though!
    
    public ArrayDictionary() {
        this.pairs = this.makeArrayOfPairs(DEFAULT_SIZE);
        this.size = 0;
    }
    
    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }
    
    @Override
    public V get(K key) {
        int index = this.indexFinder(key);
        if (index < 0) {
            throw new NoSuchKeyException();
        }
        return this.pairs[index].value;
    }
    
    private int indexFinder(K key) {
        for (int i = 0; i < this.size(); i++) {
            if (this.pairs[i].key == key || (this.pairs[i].key != null && this.pairs[i].key.equals(key))) {
                return i;
            } 
        }
        return -1;
    }
    
    @Override
    public void put(K key, V value) {
        if (this.size() + 1 == this.pairs.length) {
            Pair<K, V>[] newPairs = this.makeArrayOfPairs(this.pairs.length*2);
            for (int i = 0; i < this.size(); i++) {
                newPairs[i] = this.pairs[i];
            }
            this.pairs = newPairs;
        }
        int index = this.indexFinder(key);
        if (index > -1) {
            this.pairs[index] = new Pair<K, V>(key, value);
        }else {
            this.pairs[this.size()] = new Pair<K, V>(key, value);
            this.size++;
        }
    }
    
    @Override
    public V remove(K key) {
        int index = this.indexFinder(key);
        if (index < 0) {
                throw new NoSuchKeyException();
        }
        V value = this.pairs[index].value;
        for (int i = index; i < this.size; i++) {
                this.pairs[i] = this.pairs[i + 1];
        }
        this.size--;
        return value;
    }
    
    @Override
    public boolean containsKey(K key) {
        if (this.indexFinder(key) < 0) {
            return false;
        }
        return true;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    private static class Pair<K, V> {
        public K key;
        public V value;
    
    // You may add constructors and methods to this class as necessary.
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(this.pairs, this.size);
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int current = 0;
        private int size = 0;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs2, int size2) {
            // You do not need to make any changes to this constructor.
            this.pairs = pairs2;
            this.size = size2;
        }
        
        public boolean hasNext() {
            return current < this.size;
        }
        
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> newPair = new KVPair<K, V>(pairs[current].key, pairs[current].value);
            current++;
            return newPair;
        }
        
    }

}


