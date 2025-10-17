package com.speechify;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTextTest {

  @Test void textFlattens() {
    var p = new SsmlParser();
    var root = p.parse("Hello   world");
    assertEquals("Hello world", NodeText.toText(root));
  }

  @Test void paragraphAddsSpace() {
    var p = new SsmlParser();
    var root = p.parse("<speak><p>Hello</p><p>world</p></speak>");
    assertEquals("Hello world", NodeText.toText(root));
  }

  @Test void breakAddsSpace() {
    var p = new SsmlParser();
    var root = p.parse("<speak>Hello<break time=\"100ms\"/>world</speak>");
    assertEquals("Hello world", NodeText.toText(root));
  }

  @Test void sentenceAndPunctuation() {
    var p = new SsmlParser();
    var root = p.parse("<speak><p>Hello <s>world</s>!</p></speak>");
    assertEquals("Hello world !", NodeText.toText(root));
  }
}
