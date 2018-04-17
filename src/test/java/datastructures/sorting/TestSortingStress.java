package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


import java.util.ArrayList;
import java.util.List;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {

    @Test(timeout=10*SECOND)
    public void testTopKSortStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 400000; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5000, list);
        assertEquals(5000, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(395000 + i, top.get(i));
        }
    }
    
    @Test(timeout=5*SECOND)
    public void testHeapOrderStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 500000; i++) {
            list.add(i);
        }
        for (int i = 0; i < 500000; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 10; i++) {
            int num = list.get(i);
            assertEquals(num, heap.removeMin());
        }
    }

    @Test(timeout=10*SECOND)
    public void testHeapManyValues() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int cap = 400000;
        for (int i = 0; i < cap; i++) {
            heap.insert(i ^2);
        }
        assertEquals(cap, heap.size());
        for (int i = 0; i < cap; i++) {
            heap.removeMin();
        }
    }

    @Test(timeout=10*SECOND)
    public void testRemoveMinStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 0; i < 400000; i++) {
            heap.insert((int) (Math.random() * 150));
        }
        for (int i = 0; i < 300000; i++) {
            int value = heap.peekMin();
            int min = heap.removeMin();
            assertEquals(value, min);
            assertEquals(400000 - (i + 1), heap.size());
        }
    }
}
