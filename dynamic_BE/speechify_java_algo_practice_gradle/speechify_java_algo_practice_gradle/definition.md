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

## 🧪 Test Cases Overview

1. **Simple text only:** `"Hello world"` → root with one text child
2. **Single element:** `<speak>Hello</speak>` → nested structure
3. **Nested elements:** Complex hierarchy with multiple levels
4. **Attributes:** Both single and double quoted values
5. **Self-closing tags:** Elements that end with `/>`
6. **Mixed content:** Elements with both text and child elements
7. **Error handling:** Mismatched tags should throw exceptions