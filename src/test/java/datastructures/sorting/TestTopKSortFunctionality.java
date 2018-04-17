package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testKIsNegativeThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 500; i++) {
            list.add(i);
        }

        try {
            Searcher.topKSort(-2, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {  
            //Do nothing
        }
    } 
    
    @Test(timeout=SECOND)
    public void testKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 700; i++) {
            list.add(i ^ 2);
        }
        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    }
    
    @Test(timeout=SECOND)
    public void testKIsLargerThanInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 800; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(1000, list);
        assertEquals(800, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testSortNegativeValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 1; i < 5000; i++) {
            list.add(i * -1);
        }
        IList<Integer> top = Searcher.topKSort(50, list);
        assertEquals(50, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(-50 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testOnlyOnetValue() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(500);
        IList<Integer> top = Searcher.topKSort(50, list);
        assertEquals(1, top.size());   
        assertEquals(500, top.get(0));
    }
    
    @Test(timeout=SECOND)
    public void testInputisUnsorted() {
        IList<Integer> list = new DoubleLinkedList<>();
        int value = 1;
        for (int i = 0; i < 100; i++) {
            list.add(i * value);
            value = value * -1;
        }
        IList<Integer> top = Searcher.topKSort(10, list);
        assertEquals(10, top.size());
        assertEquals(80, top.get(0));
        for (int i = 0; i < 10; i++) {
            assertEquals(80 + (i * 2), top.get(i));
        }
    }   
}
