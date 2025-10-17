package com.speechify;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class LruCacheTest {

  @Test void capacityZero_setIgnored_getNull() {
    var c = new LruCache<Integer, Integer>(0);
    c.set(1,1);
    assertNull(c.get(1));
  }

  @Test void basicPutGet() {
    var c = new LruCache<Integer, String>(2);
    c.set(1,"a"); c.set(2,"b");
    assertEquals("a", c.get(1));
    assertEquals("b", c.get(2));
  }

  @Test void getPromotesToMRU_affectsEviction() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(2,2);
    assertEquals(1, c.get(1));   // 1 becomes MRU, 2 is LRU
    c.set(3,3);                  // evict 2
    assertNull(c.get(2));
    assertEquals(1, c.get(1));
    assertEquals(3, c.get(3));
  }

  @Test void overwriteUpdatesAndPromotes() {
    var c = new LruCache<Integer,String>(2);
    c.set(1,"a"); c.set(2,"b");
    c.set(1,"A");                // update + promote 1
    c.set(3,"c");                // should evict key 2
    assertNull(c.get(2));
    assertEquals("A", c.get(1));
    assertEquals("c", c.get(3));
  }

  @Test void evictOrder_multipleCycles() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(2,2); // LRU=1, MRU=2
    c.set(3,3);             // evict 1; cache {2,3}
    c.set(4,4);             // evict 2; cache {3,4}
    assertNull(c.get(1));
    assertNull(c.get(2));
    assertEquals(3, c.get(3));
    assertEquals(4, c.get(4));
  }

  @Test void readOnlyMissDoesNotChangeSize() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(2,2);
    assertNull(c.get(9));       // miss
    assertEquals(2, c.sizeUnsafe());
  }

  @Test void singleEntryRepeatedGets() {
    var c = new LruCache<Integer,Integer>(1);
    c.set(10,100);
    assertEquals(100, c.get(10));
    c.set(11,110);              // evict 10
    assertNull(c.get(10));
    assertEquals(110, c.get(11));
  }

  @Test void nullValueAllowed() {
    var c = new LruCache<Integer,String>(2);
    c.set(5, null);
    assertNull(c.get(5));       // present but value null → get returns null (OK)
    c.set(6,"x");
    c.set(7,"y");               // evicts key 5
    assertNull(c.get(5));       // now absent
  }

  @Test void manyPromotionsNoEvictionBeyondCapacity() {
    var c = new LruCache<Integer,Integer>(3);
    c.set(1,1); c.set(2,2); c.set(3,3);
    c.get(1); c.get(2); c.get(3); c.get(1);
    assertEquals(3, c.sizeUnsafe());
  }

  @Test void updateValueDoesNotGrowSize() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(2,2);
    c.set(1,111);
    assertEquals(2, c.sizeUnsafe());
    assertEquals(111, c.get(1));
  }

  @Test void sequenceEvictionCheck() {
    var c = new LruCache<Integer,Integer>(3);
    c.set(1,1); c.set(2,2); c.set(3,3);  // LRU=1
    c.get(1);                             // LRU=2
    c.set(4,4);                           // evict 2
    assertNull(c.get(2));
    c.set(5,5);                           // LRU was 3, evict 3
    assertNull(c.get(3));
    assertEquals(1, c.get(1));
    assertEquals(4, c.get(4));
    assertEquals(5, c.get(5));
  }

  @Test void capacityOnekeepsOnlyMRU() {
    var c = new LruCache<Integer,Integer>(1);
    c.set(1,1);
    c.set(2,2); // evict 1
    assertNull(c.get(1));
    assertEquals(2, c.get(2));
  }

  @Test void interleaveGetSet() {
    var c = new LruCache<Integer,String>(2);
    c.set(1,"a"); c.set(2,"b");
    c.get(1);             // 2 is LRU
    c.set(3,"c");         // evict 2
    c.get(3);             // 1 is LRU
    c.set(4,"d");         // evict 1
    assertNull(c.get(1));
    assertNull(c.get(2));
    assertEquals("c", c.get(3));
    assertEquals("d", c.get(4));
  }

  @Test void heavyChurn() {
    var c = new LruCache<Integer,Integer>(2);
    for (int i=0;i<100;i++) {
      c.set(1,i);
      c.set(2,i);
      assertNotNull(c.get(1));
      c.set(3,i); // evict 2
      assertNull(c.get(2));
      assertNotNull(c.get(1));
      assertNotNull(c.get(3));
    }
  }

  @Test void getOnMissingStaysMissing() {
    var c = new LruCache<Integer,Integer>(2);
    assertNull(c.get(123));
    c.set(1,1); c.set(2,2);
    assertNull(c.get(123));
  }

  @Test void evictAfterOverwritePromotion() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(2,2);
    c.set(1,10);            // promote 1, LRU=2
    c.set(3,3);             // evict 2
    assertNull(c.get(2));
  }

  @Test void setSameKeyManyTimes() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(1,2); c.set(1,3);
    assertEquals(3, c.get(1));
    c.set(2,2);
    c.set(3,3);             // evict LRU=2 (not 1)
    assertNull(c.get(1));
    assertEquals(2, c.get(2));
  }

  @Test void mruIsLastTouched() {
    var c = new LruCache<Integer,Integer>(3);
    c.set(1,1); c.set(2,2); c.set(3,3);
    c.get(2);
    c.set(4,4); // should evict 1
    assertNull(c.get(1));
    assertEquals(2, c.get(2));
    assertEquals(3, c.get(3));
    assertEquals(4, c.get(4));
  }

  @Test void fillAndEvictBackToBack() {
    var c = new LruCache<Integer,Integer>(2);
    c.set(1,1); c.set(2,2); c.set(3,3);
    assertNull(c.get(1));
    c.set(4,4);
    assertNull(c.get(2));
  }
}
