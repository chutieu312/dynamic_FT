# SSML Parser Complete Execution Plan

## 🎯 Input String
```xml
<speak>
  <p>This is <emphasis level="strong">very important</emphasis> text.</p>
  <break time="500ms"/>
  <p>Next paragraph</p>
</speak>
```

## 📋 Parser Initial State
```java
s = "<speak>\n  <p>This is <emphasis level=\"strong\">very important</emphasis> text.</p>\n  <break time=\"500ms\"/>\n  <p>Next paragraph</p>\n</speak>"
i = 0    // cursor position
n = 150  // string length (approximately)
```

## 🚀 Step-by-Step Execution

### PHASE 1: Main parse() Method Entry
```java
public SsmlNode parse(String input) {
    s = input; i = 0; n = s.length();
    List<SsmlNode> children = new ArrayList<>();
    
    while (i < n) {
        if (peek() == '<') {
            children.add(parseElementOrSelfClosing());
        } else {
            children.add(parseText());
        }
    }
    
    return SsmlNode.elem("root", Map.of(), children);
}
```

**State:** `i=0`, looking at `'<'`

---

### PHASE 2: Parse First Element `<speak>`

#### Step 2.1: parseElementOrSelfClosing() Entry
- **Cursor:** `i=0` at `'<'`
- **Action:** Call `parseElementOrSelfClosing()`

#### Step 2.2: Parse Opening Tag
```java
expect('<');           // i=0 → i=1, cursor now at 's'
String name = readName();  // reads "speak", i=1 → i=6
Map<String,String> attrs = readAttrs();  // no attributes, i=6
skipWs();              // skip any whitespace, i=6
expect('>');           // i=6 → i=7, cursor at '\n'
```

**State:** `name="speak"`, `attrs={}`, `i=7`

#### Step 2.3: Parse Children of `<speak>`
```java
List<SsmlNode> children = new ArrayList<>();
while (i < n && !(peek() == '<' && peek(1) == '/' && readAheadName().equals("speak"))) {
    if (peek() == '<') {
        children.add(parseElementOrSelfClosing());
    } else {
        children.add(parseText());
    }
}
```

---

### PHASE 3: Parse First Child - Whitespace Text

#### Step 3.1: parseText() for "\n  "
- **Cursor:** `i=7` at `'\n'`
- **Action:** `parseText()` reads until next `'<'`
- **Result:** `SsmlNode.text("\n  ")`
- **State:** `i=10`, cursor at `'<'` of `<p>`

**Children so far:** `[text("\n  ")]`

---

### PHASE 4: Parse `<p>` Element (RECURSION LEVEL 1)

#### Step 4.1: parseElementOrSelfClosing() for `<p>`
```java
expect('<');           // i=10 → i=11
String name = readName();  // reads "p", i=11 → i=12
Map<String,String> attrs = readAttrs();  // no attributes
expect('>');           // i=12 → i=13, cursor at 'T'
```

#### Step 4.2: Parse Children of `<p>`
- **Child 1:** `parseText()` → `SsmlNode.text("This is ")`
- **Child 2:** `parseElementOrSelfClosing()` → Parse `<emphasis>` (RECURSION LEVEL 2)

---

### PHASE 5: Parse `<emphasis>` Element (RECURSION LEVEL 2)

#### Step 5.1: Parse `<emphasis level="strong">`
```java
expect('<');           // cursor advances
String name = readName();  // reads "emphasis"
Map<String,String> attrs = readAttrs();  // calls readAttrs()
```

#### Step 5.2: readAttrs() Execution
```java
Map<String,String> attrs = new LinkedHashMap<>();
skipWs();              // skip spaces
String attrName = readName();  // reads "level"
skipWs();
expect('=');
skipWs();
char quote = peek();   // '"'
expect(quote);
String value = readUntilQuote(quote);  // reads "strong"
expect(quote);
attrs.put("level", "strong");
return attrs;          // {level="strong"}
```

#### Step 5.3: Parse Children of `<emphasis>`
- **Child:** `parseText()` → `SsmlNode.text("very important")`
- **End tag:** Parse `</emphasis>`

**Result:** `SsmlNode.elem("emphasis", {level="strong"}, [text("very important")])`

---

### PHASE 6: Continue Parsing `<p>` Children

#### Step 6.1: Back to `<p>` parsing (RECURSION LEVEL 1)
**Children so far:** 
```java
[
    text("This is "),
    elem("emphasis", {level="strong"}, [text("very important")])
]
```

#### Step 6.2: Parse remaining text
- **Child 3:** `parseText()` → `SsmlNode.text(" text.")`
- **End tag:** Parse `</p>`

**Result:** `SsmlNode.elem("p", {}, [text("This is "), elem("emphasis", ...), text(" text.")])`

---

### PHASE 7: Continue Parsing `<speak>` Children

#### Step 7.1: Back to `<speak>` parsing (RECURSION LEVEL 0)
**Children so far:**
```java
[
    text("\n  "),
    elem("p", {}, [...])
]
```

#### Step 7.2: Parse whitespace
- **Child 3:** `parseText()` → `SsmlNode.text("\n  ")`

#### Step 7.3: Parse `<break time="500ms"/>`
```java
expect('<');
String name = readName();  // "break"
Map<String,String> attrs = readAttrs();  // {time="500ms"}
skipWs();
expect('/');           // self-closing detected!
expect('>');
return SsmlNode.elem("break", {time="500ms"}, List.of());
```

#### Step 7.4: Continue parsing remaining children
- **Child 5:** `parseText()` → `SsmlNode.text("\n  ")`
- **Child 6:** `parseElementOrSelfClosing()` → Parse second `<p>Next paragraph</p>`
- **Child 7:** `parseText()` → `SsmlNode.text("\n")`

---

### PHASE 8: Complete Parse Tree

**Final Result:**
```java
SsmlNode.elem("root", Map.of(), List.of(
    SsmlNode.elem("speak", Map.of(), List.of(
        SsmlNode.text("\n  "),
        SsmlNode.elem("p", Map.of(), List.of(
            SsmlNode.text("This is "),
            SsmlNode.elem("emphasis", Map.of("level", "strong"), List.of(
                SsmlNode.text("very important")
            )),
            SsmlNode.text(" text.")
        )),
        SsmlNode.text("\n  "),
        SsmlNode.elem("break", Map.of("time", "500ms"), List.of()),
        SsmlNode.text("\n  "),
        SsmlNode.elem("p", Map.of(), List.of(
            SsmlNode.text("Next paragraph")
        )),
        SsmlNode.text("\n")
    ))
))
```

## 🔄 Recursion Stack Visualization

```
Level 0: parse() main method
├─ parseElementOrSelfClosing() → <speak>
   ├─ parseText() → "\n  "
   ├─ parseElementOrSelfClosing() → <p>     [RECURSIVE CALL - Level 1]
   │  ├─ parseText() → "This is "
   │  ├─ parseElementOrSelfClosing() → <emphasis>  [RECURSIVE CALL - Level 2]
   │  │  └─ parseText() → "very important"
   │  └─ parseText() → " text."
   ├─ parseText() → "\n  "
   ├─ parseElementOrSelfClosing() → <break/>  [RECURSIVE CALL - Level 1]
   ├─ parseText() → "\n  "
   ├─ parseElementOrSelfClosing() → <p>     [RECURSIVE CALL - Level 1]
   │  └─ parseText() → "Next paragraph"
   └─ parseText() → "\n"
```

**Key Recursion Points:**
- `parseElementOrSelfClosing()` calls itself whenever it encounters `<` in element content
- Each nested element creates a new recursion level
- Recursion unwinds when closing tags are found
- Maximum depth = maximum nesting level in SSML

## 🎯 State Machine Implementation

### **Parser States (Decision Points)**

**State 1: DOCUMENT_LEVEL**
- **Trigger:** `peek() == '<'` vs `peek() != '<'`
- **Transition:** 
  - `'<'` → ELEMENT_START state
  - Other → TEXT_CONTENT state

**State 2: ELEMENT_START**
- **Trigger:** `peek(1) == '/'` vs `peek(1) != '/'`
- **Transition:**
  - `'/'` → END_TAG state  
  - Other → START_TAG state

**State 3: START_TAG**
- **Trigger:** After reading name, `peek() == '/'` vs `peek() == '>'`
- **Transition:**
  - `'/'` → SELF_CLOSING state
  - `'>'` → ELEMENT_CONTENT state
  - Other → ATTRIBUTE_PARSING state

**State 4: ATTRIBUTE_PARSING**
- **Trigger:** `peek() == '='`, `peek() == '"'`, `peek() == '\''`
- **Transition:**
  - `'='` → ATTRIBUTE_VALUE state
  - Quote → QUOTED_VALUE state
  - Whitespace → Stay in ATTRIBUTE_PARSING
  - `'>'` or `'/>'` → Exit to ELEMENT_CONTENT or SELF_CLOSING

**State 5: ELEMENT_CONTENT**
- **Trigger:** `peek() == '<'` vs other characters
- **Transition:**
  - `'<'` → Check if end tag or nested element
  - Other → TEXT_CONTENT state

### **State Machine in Action Example:**
```
INPUT: <speak><p>Text</p></speak>

State Flow:
1. DOCUMENT_LEVEL: see '<' → ELEMENT_START
2. ELEMENT_START: see 's' (not '/') → START_TAG  
3. START_TAG: read "speak", see '>' → ELEMENT_CONTENT
4. ELEMENT_CONTENT: see '<' → ELEMENT_START (nested)
5. ELEMENT_START: see 'p' → START_TAG
6. START_TAG: read "p", see '>' → ELEMENT_CONTENT  
7. ELEMENT_CONTENT: see 'T' → TEXT_CONTENT
8. TEXT_CONTENT: read "Text" until '<' → ELEMENT_CONTENT
9. ELEMENT_CONTENT: see '</' → END_TAG
10. END_TAG: verify "</p>" → return to parent ELEMENT_CONTENT
11. ELEMENT_CONTENT: see '</' → END_TAG  
12. END_TAG: verify "</speak>" → DOCUMENT_LEVEL (done)
```

### **State Machine Distribution Across Methods:**

**In `parse()` method:**
```java
while (i < n) {
    if (peek() == '<') {        // STATE DECISION: ELEMENT_START
        children.add(parseElementOrSelfClosing());
    } else {                    // STATE DECISION: TEXT_CONTENT
        children.add(parseText());
    }
}
```

**In `parseElementOrSelfClosing()` method:**
```java
if (peek() == '/') {           // STATE DECISION: SELF_CLOSING
    expect('/'); expect('>');
    return SsmlNode.elem(name, attrs, List.of());
} else {                       // STATE DECISION: ELEMENT_CONTENT
    expect('>');
    // Parse children with recursive calls...
}
```

**In `readAttrs()` method:**
```java
while (i < n) {
    if (peek() == '>' || peek() == '/') break;  // STATE EXIT
    
    String name = readName();     // ATTRIBUTE_PARSING state
    expect('=');                  // ATTRIBUTE_VALUE state
    char quote = peek();          // QUOTED_VALUE state
    expect(quote);
    String value = readUntilQuote(quote);
    expect(quote);
}
```

## 🎯 Method Interaction Flow

```
parse() 
  ↓ calls
parseElementOrSelfClosing()
  ↓ calls
expect('<'), readName(), readAttrs(), expect('>')
  ↓ readAttrs() calls
readName(), expect('='), expect(quote), readUntilQuote()
  ↓ back to parseElementOrSelfClosing()
parseText() OR parseElementOrSelfClosing() (recursive)
  ↓ when done with children
expect('<'), expect('/'), readName(), expect('>')
```

## 🔧 State Machine Summary

1. **TEXT_MODE:** Reading text until `<`
2. **TAG_START:** Reading `<tagname attrs>`
3. **ATTR_MODE:** Reading `name="value"` pairs
4. **TAG_END:** Reading `</tagname>`
5. **SELF_CLOSING:** Reading `<tag/>`

Each mode uses different helper methods and transitions based on characters encountered.

## 🎯 Key Implementation Points

1. **Cursor Management:** Always track `i` position carefully
2. **Recursion:** Each element can contain other elements
3. **Order Preservation:** Children added in exact parsing order
4. **Error Handling:** `expect()` provides immediate feedback
5. **State Transitions:** Clear rules for when to switch parsing modes

## 🔧 Method Responsibilities

### **expect(char c)**
- **Purpose:** Validates expected character and advances cursor
- **Role:** Safety checkpoint at critical parsing moments
- **Protects Against:** Malformed tags, missing operators, unclosed quotes
- **Called By:** Almost every parsing method for validation
- **Side Effect:** Advances cursor `i++` when successful
- **Error Examples:** 
  - `speak>` → `"Expected '<' at position 0, but found 's'"`
  - `<break time"1s"/>` → `"Expected '=' at position 11, but found '"'"`

### **skipWs()**
- **Purpose:** Skip whitespace characters (spaces, tabs, newlines)
- **Role:** Clean up between tokens for accurate parsing
- **Used Before:** Tag parsing, attribute parsing, quote detection
- **Side Effect:** Advances cursor past all consecutive whitespace

### **readName()**
- **Purpose:** Read element/attribute names (letters, digits, -, _, :)
- **Role:** Extract identifiers for tags and attributes
- **Returns:** String containing the name
- **Used For:** Tag names (`<speak>`), attribute names (`level="strong"`)

### **parseText()**
- **Purpose:** Read text content until next `<` character
- **Role:** Extract text nodes between elements
- **Returns:** `SsmlNode.text(content)`
- **Handles:** All characters except `<` (including whitespace, special chars)

### **readAttrs()**
- **Purpose:** Parse key="value" or key='value' attribute pairs
- **Role:** Extract element attributes into Map
- **Returns:** `Map<String,String>` with attribute name-value pairs
- **Handles:** Multiple attributes, both quote types, whitespace around `=`

### **parseElementOrSelfClosing()**
- **Purpose:** Parse complete element (container or self-closing)
- **Role:** Core recursive parsing logic for elements
- **Returns:** `SsmlNode.elem(name, attrs, children)`
- **Handles:** `<tag>`, `<tag/>`, `<tag attr="val">content</tag>`
- **Recursion:** Calls itself for nested elements

### **parse()**
- **Purpose:** Main orchestrator - parse entire SSML document
- **Role:** Entry point that coordinates all parsing
- **Returns:** Root `SsmlNode` containing all top-level elements
- **Creates:** Synthetic "root" element wrapping actual content