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
        // TODO: look up node in map; if null return null; else move to front and return value.
        throw new UnsupportedOperationException("TODO: implement get()");
    }

    /**
     * Insert or update key with value.
     * If capacity is 0, do nothing.
     * If inserting new key and cache is full, evict LRU (node before tail).
     * Side effect: new/updated node becomes MRU.
     */
    public void set(K key, V value) {
        // TODO: implement set() per rules above.
        throw new UnsupportedOperationException("TODO: implement set()");
    }

    /* ===== Helpers you should implement ===== */

    /** Insert n right after head (as MRU). */
    private void insertAfterHead(Node<K,V> n) {
        // TODO: pointer surgery: head <-> n <-> head.next
        throw new UnsupportedOperationException("TODO: implement insertAfterHead()");
    }

    /** Remove n from its current position. */
    private void detach(Node<K,V> n) {
        // TODO: pointer surgery to unlink n
        throw new UnsupportedOperationException("TODO: implement detach()");
    }

    /** Move n to MRU position. */
    private void moveToFront(Node<K,V> n) {
        // TODO: detach then insertAfterHead
        throw new UnsupportedOperationException("TODO: implement moveToFront()");
    }

    /** Remove LRU node (just before tail) and return it. Assumes size > 0. */
    private Node<K,V> evictLRU() {
        // TODO: remove tail.prev from list and return it
        throw new UnsupportedOperationException("TODO: implement evictLRU()");
    }

    /* ==== visible-for-tests convenience (not required in interview) ==== */
    int sizeUnsafe() { return map.size(); }
}
