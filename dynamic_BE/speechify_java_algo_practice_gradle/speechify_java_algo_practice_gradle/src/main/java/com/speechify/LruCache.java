package com.speechify;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU Cache using HashMap + Doubly Linked List (no LinkedHashMap).
 * Invariants:
 *  - head <-> ... <-> tail  (sentinels; head.next is MRU, tail.prev is LRU)
 *  - map contains all nodes currently in the list
 *
 * TODOs are intentionally left for practice. Replace UnsupportedOperationException
 * by real code until all tests pass.
 */
public class LruCache<K, V> {

    /** Node in the doubly linked list. */
    static final class Node<K,V> {
        K k; V v;
        Node<K,V> prev, next;
        Node(K k, V v) { this.k = k; this.v = v; }
    }

    private final int capacity;
    private final Map<K, Node<K,V>> map = new HashMap<>();
    private final Node<K,V> head = new Node<>(null, null);
    private final Node<K,V> tail = new Node<>(null, null);

    /**
     * @param capacity >= 0
     */
    public LruCache(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("capacity must be >= 0");
        this.capacity = capacity;
        head.next = tail; tail.prev = head;
    }

    /**
     * Return value for key, or null if not present.
     * Side effect: promotes the node to MRU position.
     */
    public V get(K key) {
        // done: look up node in map; if null return null; else move to front and return value.
        Node<K,V> node = map.get(key);
        if (node == null) {
            return null;
        }
        moveToFront(node);
        return node.v;
    }

    /**
     * Insert or update key with value.
     * If capacity is 0, do nothing.
     * If inserting new key and cache is full, evict LRU (node before tail).
     * Side effect: new/updated node becomes MRU.
     */
    public void set(K key, V value) {
        // done: implement set() per rules above.
        if (capacity == 0) {
            return;
        }
        Node<K,V> existing = map.get(key);
        if (existing == null) {
            if (map.size() >= capacity) {
                Node<K,V> evicted = evictLRU();
                map.remove(evicted.k);
            }
            
            Node<K,V> newNode = new Node<>(key, value);
            map.put(key, newNode);
            insertAfterHead(newNode);

        }
        else {
            existing.v = value;
            moveToFront(existing);
        }

    }

    /* ===== Helpers you should implement ===== */

    /** Insert n right after head (as MRU). */
    private void insertAfterHead(Node<K,V> n) {
        // done: pointer surgery: head <-> n <-> head.next
        Node<K,V> oldMRU = head.next;
        head.next = n;
        n.prev = head;
        n.next = oldMRU;
        oldMRU.prev = n;
    }

    /** Remove n from its current position. */
    private void detach(Node<K,V> n) {
        // done: pointer surgery to unlink n
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    /** Move n to MRU position. */
    private void moveToFront(Node<K,V> n) {
        // done: detach then insertAfterHead
        detach(n);
        insertAfterHead(n);
    }

    /** Remove LRU node (just before tail) and return it. Assumes size > 0. */
    private Node<K,V> evictLRU() {
        // done: remove tail.prev from list and return it
        Node<K,V> lruNode = tail.prev;
        detach(lruNode);
        return lruNode;
    }

    /* ==== visible-for-tests convenience (not required in interview) ==== */
    int sizeUnsafe() { return map.size(); }
    
    // Quick test to verify LRU cache logic during development
    public static void main(String[] args) {
        System.out.println("=== LRU Cache Simple Tests ===");
        
        // Test 1: Basic get/set
        System.out.println("\n1. Basic Test:");
        var cache1 = new LruCache<String, Integer>(2);
        cache1.set("a", 1);
        cache1.set("b", 2);
        System.out.println("get('a') = " + cache1.get("a")); // Should be 1
        System.out.println("get('x') = " + cache1.get("x")); // Should be null
        
        // Test 2: Eviction test  
        System.out.println("\n2. Eviction Test:");
        var cache2 = new LruCache<String, Integer>(2);
        cache2.set("a", 1);
        cache2.set("b", 2);
        cache2.set("c", 3); // Should evict 'a'
        System.out.println("get('a') = " + cache2.get("a")); // Should be null (evicted)
        System.out.println("get('b') = " + cache2.get("b")); // Should be 2
        System.out.println("get('c') = " + cache2.get("c")); // Should be 3
        
        // Test 3: Update test
        System.out.println("\n3. Update Test:");
        var cache3 = new LruCache<String, Integer>(2);
        cache3.set("x", 10);
        cache3.set("x", 20); // Update existing
        System.out.println("get('x') = " + cache3.get("x")); // Should be 20
    }
}
