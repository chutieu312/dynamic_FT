# SSML Parser Definition & Examples

## 📋 SSML Parser Overview

**SSML (Speech Synthesis Markup Language)** is XML-like markup that controls text-to-speech synthesis. Our parser converts SSML strings into a tree of `SsmlNode` objects without using external XML libraries.

## 🌳 Example 1: Simple SSML

**Input SSML:**
```xml
<speak>Hello <break time="1s"/> world!</speak>
```

**Resulting Node Tree:**
```
root
└── speak (element)
    ├── "Hello " (text)
    ├── break (self-closing element, attrs: {time="1s"})
    └── " world!" (text)
```

**Java Object Structure:**
```java
SsmlNode root = SsmlNode.elem("root", Map.of(), List.of(
    SsmlNode.elem("speak", Map.of(), List.of(
        SsmlNode.text("Hello "),
        SsmlNode.elem("break", Map.of("time", "1s"), List.of()),
        SsmlNode.text(" world!")
    ))
));
```

## 🌳 Example 2: Nested Elements

**Input SSML:**
```xml
<speak>
  <p>This is <emphasis level="strong">very important</emphasis> text.</p>
  <break time="500ms"/>
  <p>Next paragraph</p>
</speak>
```

**Resulting Node Tree:**
```
root
└── speak
    ├── "\n  " (text - whitespace)
    ├── p (element)
    │   ├── "This is " (text)
    │   ├── emphasis (element, attrs: {level="strong"})
    │   │   └── "very important" (text)
    │   └── " text." (text)
    ├── "\n  " (text - whitespace)
    ├── break (self-closing, attrs: {time="500ms"})
    ├── "\n  " (text - whitespace)
    ├── p (element)
    │   └── "Next paragraph" (text)
    └── "\n" (text - whitespace)
```

## 🌳 Example 3: Multiple Attributes

**Input SSML:**
```xml
<audio src="bell.wav" volume="loud" speed="slow">Fallback text</audio>
```

**Resulting Node Tree:**
```
root
└── audio (element, attrs: {src="bell.wav", volume="loud", speed="slow"})
    └── "Fallback text" (text)
```

**Java Object Structure:**
```java
SsmlNode root = SsmlNode.elem("root", Map.of(), List.of(
    SsmlNode.elem("audio", Map.of(
        "src", "bell.wav",
        "volume", "loud", 
        "speed", "slow"
    ), List.of(
        SsmlNode.text("Fallback text")
    ))
));
```

## 🔧 Key Parser Concepts

### 1. Node Types
- **Text Nodes:** `SsmlNode.text("content")` - actual text content
- **Element Nodes:** `SsmlNode.elem("name", attrs, children)` - XML elements

### 2. Children List (`List<SsmlNode>`)
**CRITICAL: Order is preserved and essential!**

The `children` parameter can contain:
- ✅ **Element nodes** (`SsmlNode.elem(...)`)
- ✅ **Text nodes** (`SsmlNode.text(...)`) 
- ✅ **Mixed combinations** in exact sequence

**Example:** `<p>Hello <em>world</em>!</p>` produces children in precise order:
```java
List<SsmlNode> children = List.of(
    SsmlNode.text("Hello "),        // Index 0: first text
    SsmlNode.elem("em", Map.of(), List.of(  // Index 1: nested element
        SsmlNode.text("world")
    )),
    SsmlNode.text("!")              // Index 2: final text
);
```

**Why order matters:** Wrong order would produce `"!Hello world"` instead of `"Hello world!"`

**Parser requirement:** Must process SSML left-to-right and add children sequentially.

### 3. Element Forms
- **Container:** `<speak>content</speak>` - has children between open/close tags
- **Self-closing:** `<break time="1s"/>` - no children, ends with `/>`

### 4. Attributes
- **Double quotes:** `time="1s"`
- **Single quotes:** `level='strong'`
- **Multiple:** `<audio src="file.wav" volume="loud"/>`

### 5. Parsing Strategy
- **Cursor-based:** Move index `i` through string `s`
- **Recursive descent:** Parse nested elements by calling parser recursively
- **State machine:** Track whether we're in text, start tag, end tag, etc.

## 🎯 Parser Requirements

### ✅ Must Handle
- Start tags: `<speak>`, `<p class="intro">`
- End tags: `</speak>`, `</p>`
- Self-closing: `<break/>`, `<audio src="file.wav"/>`
- Text content: `Hello world`
- Quoted attributes: `time="1s"` and `level='strong'`
- Nested elements with proper hierarchy
- Whitespace preservation in text nodes

### ❌ Don't Need to Handle
- XML entities (`&lt;`, `&amp;`)
- CDATA sections
- Comments (`<!-- -->`)
- Namespaces
- DOCTYPE declarations

## 🚀 Implementation Order

1. **Helper methods** (`skipWs`, `expect`, `readName`)
2. **Basic parsing** (`parseText`)
3. **Attribute parsing** (`readAttrs`)  
4. **Element parsing** (`parseElementOrSelfClosing`)
5. **Main parser** (`parse`)

## 📝 SsmlNode Structure

```java
public class SsmlNode {
    public final String name;              // null for text nodes
    public final String text;              // only for text nodes
    public final Map<String,String> attrs; // empty for text nodes
    public final List<SsmlNode> children;  // empty for text nodes
    
    // Factory methods:
    public static SsmlNode text(String t);
    public static SsmlNode elem(String name, Map<String,String> attrs, List<SsmlNode> children);
    public boolean isText();
}
```

## 🔍 Parser State Management

The parser maintains:
- `String s` - input SSML string
- `int i` - current cursor position
- `int n` - string length

Key methods:
- `char peek()` - look at current character without advancing
- `char peek(int k)` - look ahead k positions
- Cursor advances as we consume characters

## 🏗️ Complete SSML Parser System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                           INPUT PHASE                               │
│  Raw SSML String: "<speak><p>Hello <em>world</em></p></speak>"      │
└─────────────────────┬───────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        INITIALIZATION                               │
│  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓  │
│  ┃ Parser State: s="<speak>...", i=0, n=length                 ┃  │
│  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛  │
└─────────────────────┬───────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                               MAIN PARSING LOOP                                    │
│  ╔═══════════════════════════════════════════════════════════════════════════════╗  │
│  ║  while (i < n) {                                                              ║  │
│  ║    if (peek() == '<') → parseElementOrSelfClosing()                          ║  │
│  ║    else              → parseText()                                           ║  │
│  ║  }                                                                            ║  │
│  ╚═══════════════════════════════════════════════════════════════════════════════╝  │
└─────────────────────────────┬───────────────────────────────┬─────────────────────────┘
                              │                               │
                              ▼                               ▼
┌──────────────────────────────────────┐    ┌──────────────────────────────────────┐
│          TEXT PARSING PATH           │    │        ELEMENT PARSING PATH          │
│  ┌──────────────────────────────────┐│    │  ┌──────────────────────────────────┐│
│  │         parseText()              ││    │  │    parseElementOrSelfClosing()   ││
│  │                                  ││    │  │                                  ││
│  │ • Read until '<'                 ││    │  │ • Parse opening tag              ││
│  │ • Create text node               ││    │  │ • Parse attributes               ││
│  │ • Advance cursor                 ││    │  │ • Parse content                  ││
│  └──────────────────────────────────┘│    │  │ • Parse closing tag              ││
└──────────────────────────────────────┘    │  └──────────────────────────────────┘│
                                            └──────────────────────────────────────┘
                                                │
                                                ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                           ELEMENT PARSING BREAKDOWN                                │
│                                                                                     │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐ │
│  │     Step 1      │  │     Step 2      │  │     Step 3      │  │     Step 4      │ │
│  │  OPENING TAG    │  │   ATTRIBUTES    │  │    DECISION     │  │ CONTENT/CLOSE   │ │
│  │                 │  │                 │  │                 │  │                 │ │
│  │ expect('<')     │  │ readAttrs()     │  │ 🎯 CRITICAL     │  │ Container:      │ │
│  │ readName()      │  │                 │  │ DECISION        │  │ • RECURSION!    │ │
│  │ ↓returns        │  │ while(...){     │  │                 │  │ • while(...){   │ │
│  │ "speak"         │  │  readName()     │  │ skipWs();       │  │   if('<')       │ │
│  │ "p"             │  │  expect('=')    │  │ if('/')         │  │    →parseEl     │ │
│  │ "em"            │  │  readValue()    │  │  →SELF-CLOSE    │  │   else          │ │
│  │                 │  │ }               │  │ else('>')       │  │    →parseText   │ │
│  │                 │  │                 │  │  →CONTAINER     │  │ • Parse </>     │ │
│  │                 │  │                 │  │                 │  │                 │ │
│  │                 │  │                 │  │                 │  │ Self-Close:     │ │
│  │                 │  │                 │  │                 │  │ • expect('/')   │ │
│  │                 │  │                 │  │                 │  │ • expect('>')   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                           │
                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                           🔀 PARSER DECISION FLOW                                  │
│                                                                                     │
│              After parsing: <tagName attr="value"                                  │
│                                       ↓                                             │
│                          What comes next?                                          │
│                                       │                                             │
│       ┌───────────────────────────────┴───────────────────────────────┐           │
│       │                                                               │           │
│       ▼                                                               ▼           │
│  ┌─────────────────────────────────┐         ┌─────────────────────────────────┐  │
│  │         peek() == '/'           │         │         peek() == '>'           │  │
│  │                                 │         │                                 │  │
│  │       🔒 SELF-CLOSING           │         │         📦 CONTAINER           │  │
│  │                                 │         │                                 │  │
│  │ • expect('/');                  │         │ • expect('>');                  │  │
│  │ • expect('>');                  │         │ • RECURSION:                    │  │
│  │ • return elem()                 │         │   while(...) {                  │  │
│  │   with empty                    │         │    if('<')                      │  │
│  │   children: []                  │         │     →parseEl                    │  │
│  │                                 │         │    else                         │  │
│  │ Examples:                       │         │     →parseText                  │  │
│  │ • <break time="1s"/>            │         │   }                             │  │
│  │ • <audio src="..."/>            │         │ • expect("</")                  │  │
│  │                                 │         │ • readName()                    │  │
│  │                                 │         │ • expect('>')                   │  │
│  │                                 │         │ • return elem()                 │  │
│  │                                 │         │   with children                 │  │
│  │                                 │         │                                 │  │
│  │                                 │         │ Examples:                       │  │
│  │                                 │         │ • <speak>...</>                 │  │
│  │                                 │         │ • <p>text</p>                   │  │
│  └─────────────────────────────────┘         └─────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────────────┘
                                           │
                                           ▼
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                               HELPER METHODS LAYER                                 │
│                                                                                     │
│  ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ │
│  │    skipWs()     │ │    expect()     │ │   readName()    │ │   readValue()   │ │
│  │                 │ │                 │ │                 │ │                 │ │
│  │ Skip spaces,    │ │ Validate +      │ │ Read valid      │ │ Read quoted     │ │
│  │ tabs, \n, \r    │ │ advance         │ │ name chars      │ │ attr values     │ │  
│  │                 │ │ cursor          │ │                 │ │                 │ │
│  └─────────────────┘ └─────────────────┘ └─────────────────┘ └─────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────────┘

## 🛠️ Helper Methods Detailed Architecture & Flow

### 1. **skipWs()** - Whitespace Skipper

```
                          ┌─ EVERY TIME WE SEE: ─┐
                          │  • Before parsing    │
                          │  • After expect()    │  
                          │  • Between tokens    │
                          │  • Around attributes │
                          └─────────┬─────────────┘
                                    │
                                    ▼
                          ┌─────────────────────┐
                          │     skipWs()        │
                          │                     │
                          │ 🧹 Space Cleaner    │
                          │ Consumes: ' '       │  
                          │          \t         │
                          │          \n         │
                          │          \r         │
                          │ Stops at: any other │
                          │          character  │
                          └─────────────────────┘
```

### 2. **expect(char c)** - Character Validator

```
                    ┌─ EVERY TIME WE SEE: ─┐
                    │  • expect('<')       │
                    │  • expect('>')       │  
                    │  • expect('=')       │
                    │  • expect('/')       │
                    └─────────┬─────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │    expect(char)     │
                    │                     │
                    │ ✅ Strict Enforcer  │
                    │ Check: current==c   │  
                    │ Success: i++        │
                    │ Failure: throw      │
                    │         error       │
                    └─────────────────────┘
```

### 3. **readName()** - Name Extractor

```
                    ┌─ EVERY TIME WE SEE: ─┐
                    │  • <tagname          │
                    │  • attrname=         │  
                    │  • </tagname         │
                    └─────────┬─────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │    readName()       │
                    │                     │
                    │ 🔤 Character Eater  │
                    │ Consumes: a-z A-Z   │  
                    │          0-9 - _ :  │
                    │ Stops at: space     │
                    │          > = < /    │
                    └─────────────────────┘
```

### 4. **readValue()** - Attribute Value Parser

```
                    ┌─ EVERY TIME WE SEE: ─┐
                    │  • attr="value"      │
                    │  • attr='value'      │  
                    │  • time="500ms"      │
                    └─────────┬─────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │   readValue()       │
                    │                     │
                    │ 📝 Quote Handler    │
                    │ Expects: " or '     │  
                    │ Consumes: content   │
                    │ Until: matching     │
                    │        quote        │
                    └─────────────────────┘
```

### 5. **readAttrs()** - Attribute Map Builder

```
                    ┌─ EVERY TIME WE SEE: ─┐
                    │  • <tag attr="val">  │
                    │  • <break time="1s"/> │  
                    │  • <audio src="..."> │
                    └─────────┬─────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │   readAttrs()       │
                    │                     │
                    │ 🗂️ Map Builder       │
                    │ Loop:               │  
                    │  skipWs()           │
                    │  readName() → key   │
                    │  expect('=')        │
                    │  readValue() → val  │
                    │ Until: '>' or '/>'  │
                    └─────────────────────┘
```

### 6. **parseText()** - Text Content Extractor

```
                    ┌─ EVERY TIME WE SEE: ─┐
                    │  • Hello world       │
                    │  • text between tags │  
                    │  • whitespace        │
                    └─────────┬─────────────┘
                              │
                              ▼
                    ┌─────────────────────┐
                    │   parseText()       │
                    │                     │
                    │ 📖 Text Collector   │
                    │ Consumes: any char  │  
                    │ Stops at: '<'       │
                    │ Returns: SsmlNode   │
                    │         .text()     │
                    └─────────────────────┘
```

## 🔄 **Helper Method Call Chain:**

```
parseElementOrSelfClosing()
    │
    ├─ expect('<')           ← expect() validates
    ├─ readName()            ← readName() gets tag
    ├─ readAttrs()           ← readAttrs() builds map
    │   ├─ skipWs()          ← skipWs() cleans spaces
    │   ├─ readName()        ← readName() gets attr name
    │   ├─ expect('=')       ← expect() validates
    │   ├─ readValue()       ← readValue() gets attr value
    │   └─ (loop until '>')
    ├─ Content parsing...    ← parseText() or recursion
    ├─ expect('<')           ← expect() for closing
    ├─ expect('/')           ← expect() validates
    ├─ readName()            ← readName() gets closing tag
    └─ expect('>')           ← expect() validates
```

## 🎯 **Helper Methods Interaction Pattern:**

```
                     ┌─────────────────┐
                     │  Main Parser    │
                     │   Methods       │
                     └─────────┬───────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
┌─────────────┐      ┌─────────────┐        ┌─────────────┐
│ parseText() │      │parseElement │        │    parse()  │
│             │      │OrSelfClosing│        │             │
└─────┬───────┘      └─────┬───────┘        └─────┬───────┘
      │                    │                      │
      │      ┌─────────────┼─────────────┐        │
      │      │             │             │        │
      ▼      ▼             ▼             ▼        ▼
┌──────────────────────────────────────────────────────┐
│                HELPER LAYER                          │
│  skipWs() ↔ expect() ↔ readName() ↔ readValue()     │
│                    ↕                                 │
│                readAttrs()                           │
└──────────────────────────────────────────────────────┘
```
                                           │
                                           ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       OUTPUT PHASE                                 │
│  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓  │
│  ┃                    SsmlNode Tree                             ┃  │
│  ┃  root                                                        ┃  │
│  ┃   └── speak                                                  ┃  │
│  ┃        └── p                                                 ┃  │
│  ┃             ├── text("Hello ")                               ┃  │
│  ┃             ├── em                                           ┃  │
│  ┃             │    └── text("world")                           ┃  │
│  ┃             └── text("")                                     ┃  │
│  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛  │
└─────────────────────────────────────────────────────────────────────┘
```

## 🔄 Recursive Flow Pattern

```
parse() 
  → parseElementOrSelfClosing() 
    → parseElementOrSelfClosing() (nested elements)
      → parseText() (leaf content)
    → parseElementOrSelfClosing() (more nested)
  → parseText() (text between elements)
→ FINAL TREE
```

## 🎯 Key System Properties

- **Recursive Descent**: Each element can contain other elements
- **State Machine**: Cursor position (`i`) drives all decisions  
- **Cursor-Based**: Single pass through string, no backtracking
- **Tree Builder**: Constructs nested SsmlNode structure
- **Error Recovery**: `expect()` provides clear error messages

## 🧪 Test Cases Overview

1. **Simple text only:** `"Hello world"` → root with one text child
2. **Single element:** `<speak>Hello</speak>` → nested structure
3. **Nested elements:** Complex hierarchy with multiple levels
4. **Attributes:** Both single and double quoted values
5. **Self-closing tags:** Elements that end with `/>`
6. **Mixed content:** Elements with both text and child elements
7. **Error handling:** Mismatched tags should throw exceptions