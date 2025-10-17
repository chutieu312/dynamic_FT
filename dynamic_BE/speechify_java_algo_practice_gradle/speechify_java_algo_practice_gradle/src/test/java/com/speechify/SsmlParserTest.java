package com.speechify;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SsmlParserTest {

  @Test void simpleTextOnly() {
    var p = new SsmlParser();
    var root = p.parse("Hello world");
    assertEquals("root", root.name);
    assertTrue(root.children.get(0).isText());
  }

  @Test void singleElementNoAttrs() {
    var p = new SsmlParser();
    var root = p.parse("<speak>Hello</speak>");
    assertEquals("speak", root.children.get(0).name);
    assertEquals(1, root.children.get(0).children.size());
  }

  @Test void nestedElements() {
    var p = new SsmlParser();
    var root = p.parse("<speak><p>Hello <s>world</s>!</p></speak>");
    var speak = root.children.get(0);
    assertEquals("p", speak.children.get(0).name);
    assertTrue(speak.children.get(0).children.get(1).isText()); // space + "!"
  }

  @Test void attributesQuoted() {
    var p = new SsmlParser();
    var root = p.parse("<break time=\"200ms\" strength='x-weak'/>");
    var br = root.children.get(0);
    assertEquals("break", br.name);
    assertEquals("200ms", br.attrs.get("time"));
    assertEquals("x-weak", br.attrs.get("strength"));
  }

  @Test void selfClosingAndNormalTogether() {
    var p = new SsmlParser();
    var root = p.parse("<speak><break time=\"100ms\"/><p>Hi</p></speak>");
    assertEquals(2, root.children.get(0).children.size());
  }

  @Test void mismatchedEndTagThrows() {
    var p = new SsmlParser();
    assertThrows(RuntimeException.class, () -> p.parse("<a><b></a></b>"));
  }

  @Test void whitespaceAroundAttributes() {
    var p = new SsmlParser();
    var root = p.parse("<tag  a = \"1\"   b= '2'   />");
    var t = root.children.get(0);
    assertEquals("1", t.attrs.get("a"));
    assertEquals("2", t.attrs.get("b"));
  }
}
