package com.speechify;

public class NodeText {

    /** Convert the node tree to normalized plain text. */
    public static String toText(SsmlNode node) {
        // done: StringBuilder sb; call walk(node, sb); normalize spaces; trim; return
        StringBuilder sb = new StringBuilder();
        walk(node, sb);
        return sb.toString().replaceAll("\\s+", " ").trim();
    }

    private static void walk(SsmlNode n, StringBuilder sb) {
        // done: if text -> append; else switch by tag name (lowercased), visit children,
        // and add spaces for <break>, trailing space for <p>/<s>.
        // The Logic:

        // If text node → append text
        // If element → handle special tags + process children
        // Special SSML tags from tests:

        // <break> → add space
        // <p> (paragraph) → process children + add trailing space
        // <s> (sentence) → process children + add trailing space
        // All others → just process children


        if (n.isText()) {
            sb.append(n.text);
        }
        else {
            String tagName = n.name.toLowerCase();
            switch (tagName) {
                case "break":
                    sb.append(" ");
                    break;
            
                default:
                    for (SsmlNode chilNode : n.children) {
                        walk(chilNode, sb);
                    }
                    if (tagName.equals("p") || tagName.equals("s")) {
                        sb.append(" ");
                    }
                    break;
            }
        }
    }
}
