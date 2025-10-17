package com.speechify;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SsmlNode {
    public final String name;              // null for text
    public final String text;              // only for text
    public final Map<String,String> attrs; // empty for text
    public final List<SsmlNode> children;  // empty for text

    private SsmlNode(String name, String text, Map<String,String> attrs, List<SsmlNode> children) {
        this.name = name;
        this.text = text;
        this.attrs = attrs == null ? Map.of() : Collections.unmodifiableMap(attrs);
        this.children = children == null ? List.of() : List.copyOf(children);
    }

    public static SsmlNode text(String t) { return new SsmlNode(null, t, Map.of(), List.of()); }
    public static SsmlNode elem(String name, Map<String,String> attrs, List<SsmlNode> children) {
        return new SsmlNode(name, null, attrs, children);
    }
    public boolean isText() { return name == null; }
}
