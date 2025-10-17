package com.speechify;

public class NodeText {

    /** Convert the node tree to normalized plain text. */
    public static String toText(SsmlNode node) {
        // TODO: StringBuilder sb; call walk(node, sb); normalize spaces; trim; return
        throw new UnsupportedOperationException("TODO: implement toText()");
    }

    private static void walk(SsmlNode n, StringBuilder sb) {
        // TODO: if text -> append; else switch by tag name (lowercased), visit children,
        // and add spaces for <break>, trailing space for <p>/<s>.
        throw new UnsupportedOperationException("TODO: implement walk()");
    }
}
