package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinBasic() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 0; i < 700; i++) {
    			heap.insert(i);
    			assertEquals(0, heap.peekMin());
    		}
    		assertEquals(0, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok: do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 500; i >= 1; i--) {
    			heap.insert(i);
    		}
    		for (int i = 1; i < 500; i++) {
    			assertEquals(i, heap.removeMin());
    		}
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // This is ok: do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertThrowsException() {
        IPriorityQueue<String> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // This is ok: do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertEmptyString() {
        IPriorityQueue<String> heap = this.makeInstance();
        heap.insert("hello");
        heap.insert("world");
        heap.insert("");
        heap.insert("byebye");
        assertEquals(4, heap.size());
        assertEquals("", heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinManyAndNegativeValues() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = -500; i < 500; i++) {
    			heap.insert(i);
    		}
    		for (int i = -500; i < 500; i++) {
        		heap.removeMin();
    		}
    		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testInsertSameValues() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int insertSize = 800;
        for (int i = 0; i < insertSize; i++) {
            heap.insert(50);
        }
        assertEquals(insertSize, heap.size());
        assertEquals(50, heap.peekMin());
        
        IPriorityQueue<String> heapString = this.makeInstance();
        for (int i = 0; i < insertSize; i++) {
            heapString.insert("hello world");
        }
        assertEquals(insertSize, heapString.size());
        assertEquals("hello world", heapString.peekMin());
        
    }
    
    @Test(timeout=SECOND)
    public void testAleternateInsertAndRemoveMin() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 500; i++) {
        		if (i % 2 == 0) {
        			heap.insert(i);
        		}else {
        			heap.removeMin();
        		}
        }
        assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testHeapOrderBasic() {
    		IPriorityQueue<String> heap = this.makeInstance();
    		heap.insert("a");
    		heap.insert("c");
    		heap.insert("b");
    		assertEquals("a", heap.removeMin());
    		assertEquals("b", heap.removeMin());
    		assertEquals("c", heap.removeMin());
    }
    
    @Test(timeout=SECOND)
    public void testResizeAfterInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 50; i++) {
            heap.insert(i);
        }
        heap.insert(-5);
        assertEquals(51, heap.size());
        assertEquals(-5, heap.peekMin());
    }
    
 }







