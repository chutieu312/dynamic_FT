package com.speechify;

import java.time.temporal.ChronoField;
import java.util.*;

import javax.management.RuntimeErrorException;

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
        // done: initialize fields; loop until end and collect children; return SsmlNode.elem("root", Map.of(), children)
        this.s = input;
        i = 0;
        n = input.length();

        List<SsmlNode> children =  new ArrayList<>();

        while (i<n) {
            if (peek() == '<') {
                children.add(parseElementOrSelfClosing());
            }
            else
                children.add(parseText());
        }
        return SsmlNode.elem("root", Map.of(), children);
    }

    /* ===== element parsing ===== */

    /** Parse an element which may be start+children+end OR self-closing. Cursor is on '<'. */
    private SsmlNode parseElementOrSelfClosing() {
        // done: read '<' name attrs; if '/>' then return self-closing; else read '>', 
        //then children until matching '</name>'
        expect('<');
        String tagName = readName();
        Map<String,String> attrs = readAttrs();

        skipWs();
        if (peek() == '/') {
            expect('/');
            expect('>');
            return SsmlNode.elem(tagName, attrs, List.of());
        }
        else {
            expect('>');
            List<SsmlNode> children = new ArrayList<>();
            while (i<n && !(peek() == '<' && peek(1) == '/')) { 
                if (peek() == '<') {
                    SsmlNode child = parseElementOrSelfClosing();
                    children.add(child);
                }
                else {
                    SsmlNode textNode = parseText();
                    children.add(textNode); 
                }
            }
            expect('<');
            expect('/');
            String closeTagName = readName();
            if (!tagName.equals(closeTagName)) {
                throw new RuntimeException("Mismatched tags: " + tagName + " vs " + closeTagName); 
            }
            expect('>');
            return SsmlNode.elem(tagName, attrs, children);
        }

    }

    /** Read zero or more attributes: key="value" or key='value'. Cursor is after name. */
    private Map<String,String> readAttrs() {
        // done: skipWs; while not '>' or '/>' read name, '=', quoted value; put into LinkedHashMap
        Map<String,String> attrs = new LinkedHashMap<>();

        while (i<n) {
            skipWs();
            if (peek() == '>' || peek() == '/') {
                break;
            }
            String key = readName();
            skipWs();
            expect('=');
            skipWs();
            String value = readValue();
            attrs.put(key, value);
        }
        return attrs;
    }

    private String readValue() {
        char quote = peek();
        i++;
        int start = i;
        while (i<n && peek() != quote) {
            i++;
        }
        String value = s.substring(start,i);
        expect(quote);
        return value;
    }

    /** Parse text node (read until next '<'). */
    private SsmlNode parseText() {
        // done: slice s[start..i) where we stop at '<'
        int start = i;
        while (i<n && s.charAt(i) != '<') {
            i++;
        }
        return SsmlNode.text(s.substring(start,i));
    }

    /* ===== helpers ===== */

    private String readName() {
        // done: allow letters, digits, '-', '_', ':'
        int start = i;
        while (i<n && isNameChar(s.charAt(i))) {
            i++;
        }
        return s.substring(start, i);
    }

    private boolean isNameChar (char c) {
        return Character.isLetter(c) || Character.isDigit(c) || c == '-' || c == '_' || c == ':';
    }

    private void skipWs() {
        // done: i++ while Character.isWhitespace
        while (i<n && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
    }

    private void expect(char c) {
        // done: throw if peek() != c; else i++
        if (peek() != c) {
            throw new RuntimeException("Expected '" + c + "' at position " + i + ", but found '" + peek() + "'");
        }
        i++;
        
    }

    private char peek() { return i < n ? s.charAt(i) : '\0'; }
    private char peek(int k) { return i + k < n ? s.charAt(i + k) : '\0'; }
    
    // Quick test to verify logic during development
    public static void main(String[] args) {
        var p = new SsmlParser();
        
        // Test parsing
        var node1 = p.parse("<a>test</a>");
        var node2 = p.parse("<speak><p>Hello</p><p>world</p></speak>");
        var node3 = p.parse("<speak>Hello<break time=\"100ms\"/>world</speak>");
        
        System.out.println("Parsed: " + node1);
        System.out.println("Parsed: " + node2);
        System.out.println("Parsed: " + node3);
        
        // Test text conversion
        System.out.println("Text: " + NodeText.toText(node1));
        System.out.println("Text: " + NodeText.toText(node2));
        System.out.println("Text: " + NodeText.toText(node3));
    }
}
