package com.speechify;

import java.util.*;

/**
 * Minimal SSML parser (no XML libraries).
 * Handles: start tags, end tags, self-closing tags, attributes (quoted), text nodes.
 * Does NOT handle: entities, CDATA, comments, DOCTYPE, namespaces (beyond name chars).
 *
 * TODOs are intentionally left for practice. Replace UnsupportedOperationException
 * by real code until all tests pass.
 */
public class SsmlParser {
    private String s;     // input
    private int i, n;     // cursor and length

    /** Parse a full document/string into a synthetic "root" with its children. */
    public SsmlNode parse(String input) {
        // TODO: initialize fields; loop until end and collect children; return SsmlNode.elem("root", Map.of(), children)
        throw new UnsupportedOperationException("TODO: implement parse()");
    }

    /* ===== element parsing ===== */

    /** Parse an element which may be start+children+end OR self-closing. Cursor is on '<'. */
    private SsmlNode parseElementOrSelfClosing() {
        // TODO: read '<' name attrs; if '/>' then return self-closing; else read '>', then children until matching '</name>'
        throw new UnsupportedOperationException("TODO: implement parseElementOrSelfClosing()");
    }

    /** Read zero or more attributes: key="value" or key='value'. Cursor is after name. */
    private Map<String,String> readAttrs() {
        // TODO: skipWs; while not '>' or '/>' read name, '=', quoted value; put into LinkedHashMap
        throw new UnsupportedOperationException("TODO: implement readAttrs()");
    }

    /** Parse text node (read until next '<'). */
    private SsmlNode parseText() {
        // TODO: slice s[start..i) where we stop at '<'
        throw new UnsupportedOperationException("TODO: implement parseText()");
    }

    /* ===== helpers ===== */

    private String readName() {
        // TODO: allow letters, digits, '-', '_', ':'
        throw new UnsupportedOperationException("TODO: implement readName()");
    }

    private void skipWs() {
        // TODO: i++ while Character.isWhitespace
        throw new UnsupportedOperationException("TODO: implement skipWs()");
    }

    private void expect(char c) {
        // TODO: throw if peek() != c; else i++
        throw new UnsupportedOperationException("TODO: implement expect()");
    }

    private char peek() { return i < n ? s.charAt(i) : '\0'; }
    private char peek(int k) { return i + k < n ? s.charAt(i + k) : '\0'; }
}
