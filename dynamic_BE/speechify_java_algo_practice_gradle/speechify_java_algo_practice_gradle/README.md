# Speechify Java Algorithm Practice (Gradle)

Practice kit tailored to Speechify's first-round coding task (Java). You’ll implement:
1) **LRU Cache** (O(1) get/set + eviction)
2) **SSML Parser** (no XML libs)
3) **ssmlNodeToText** (walk node tree → plain text)

This project gives you:
- Clean skeletons with **TODOs** (you implement bodies)
- A **large test suite** (≈19 LRU tests, 11 SSML tests) to simulate the real load
- Gradle commands to run/debug individual tests

---

## Project Layout

```
speechify_java_algo_practice_gradle/
├─ build.gradle, settings.gradle
├─ src/
│  ├─ main/java/com/speechify/
│  │  ├─ LruCache.java      # TODOs
│  │  ├─ SsmlNode.java      # provided node model
│  │  ├─ SsmlParser.java    # TODOs
│  │  └─ NodeText.java      # TODOs
│  └─ test/java/com/speechify/
│     ├─ LruCacheTest.java
│     ├─ SsmlParserTest.java
│     └─ NodeTextTest.java
```

---

## How to Run

```bash
# from this folder
./gradlew test

# run only LRU tests
./gradlew test --tests "com.speechify.LruCacheTest"

# run a single test method
./gradlew test --tests "com.speechify.LruCacheTest.basicPutGet"

# run only SSML parser tests
./gradlew test --tests "com.speechify.SsmlParserTest"

# run only text conversion tests
./gradlew test --tests "com.speechify.NodeTextTest"
```

> Tip: In IntelliJ, right-click any test or method → **Run** or **Debug**.

---

## Problem 1 — LRU Cache

### WHAT
Implement a Least-Recently-Used cache with O(1) `get`/`set`. Evict the **least recently used** item when capacity is full.

### WHY
Demonstrates combining a **hash map** (index by key) with a **doubly linked list** (reorder by recency) and reasoning about invariants & edge cases.

### HOW
- Maintain sentinel **head**/**tail** nodes. The node after head is **MRU**, before tail is **LRU**.
- `get(k)`: find node → **move to MRU** → return value (or null if absent).
- `set(k,v)`: if exists → update & move to MRU. Else, if full → **evict LRU**; then insert new node at MRU.
- Edge cases: capacity 0, null values allowed, repeated updates don’t change size.

### TODO Guide (file: `LruCache.java`)
1. Implement `insertAfterHead` (link `head <-> n <-> head.next`).
2. Implement `detach` (unlink node from neighbors).
3. Implement `moveToFront` (detach + insertAfterHead).
4. Implement `evictLRU` (remove `tail.prev` and return it).
5. Implement `get` (lookup; if found, moveToFront and return value).
6. Implement `set` (update or insert with eviction rule).

Run tests until **all LRU tests pass**.

---

## Problem 2 — SSML Parser (no XML libraries)

### WHAT
Parse SSML-like markup into a **node tree**: start tags, end tags, self-closing tags, attributes (quoted), and text nodes. Return a synthetic `"root"` element that contains all top-level nodes.

### WHY
Parsing demonstrates state machines, tokenization, recursive descent, correct handling of nesting, and robust string processing—without external parsers.

### HOW (minimal approach)
- Scan the string with an index.
- On `<`:
  - If next is `/` → parse **end tag** and verify name matches.
  - Else → parse **start tag** name + attributes; if ends with `/>` → self-closing; else read children until matching `</name>`.
- Otherwise read a **text** node until the next `<`.
- Attributes are `key="value"` or `key='value'`, separated by whitespace.
- Name chars: letters, digits, `- _ :` (simple subset).

### TODO Guide (file: `SsmlParser.java`)
1. Implement helpers: `skipWs`, `expect`, `readName`.
2. Implement `parseText` (slice until `<`).
3. Implement `readAttrs` (loop: read name, `=`, quoted value; stop on `>` or `/>`). Use `LinkedHashMap` to keep order.
4. Implement `parseElementOrSelfClosing`:
   - `expect('<')`, `name = readName()`, `attrs = readAttrs()`, `skipWs()`
   - If next is `/>` → return `SsmlNode.elem(name, attrs, List.of())`
   - Else `expect('>')`, then read children until `</name>`
5. Implement `parse(String)`:
   - Initialize fields; repeatedly parse element or text; wrap in `"root"`.

Run **`SsmlParserTest`**.

---

## Problem 3 — ssmlNodeToText

### WHAT
Convert an SSML node tree to **plain text** by recursively visiting nodes.

### WHY
Exercises tree traversal and minimal semantics for common SSML tags.

### HOW (baseline rules)
- Text nodes: append their text.
- Elements: visit children; for some tags add separators:
  - `<break>` → add a **space**.
  - `<p>` and `<s>` → append a trailing **space** after visiting children.
- Normalize whitespace at the end: collapse runs of spaces/tabs into a single space; `trim()`.

### TODO Guide (file: `NodeText.java`)
1. In `toText`, build a `StringBuilder`, call `walk`, normalize with:
   `replaceAll("[ \t\x0B\f\r]+", " ").trim()`
2. Implement `walk`:
   - If `n.isText()` → `append(n.text)`
   - Else `switch (n.name.toLowerCase())`:
     - `"break"`: `sb.append(" "); return;`
     - `"p"`, `"s"`: visit all children then `sb.append(" "); return;`
     - default: visit children

Run **`NodeTextTest`**.

---

## Test Counts (approx.)

- **LRU**: 19 tests
- **SSML**: 7 parser tests + 4 toText tests = **11**

They are intentionally granular to simulate real noise during debugging (e.g., eviction order, promotions, attributes, mismatched tags).

---

## Debug Tips (fast)

- Make it **compile** first (replace each `UnsupportedOperationException` as you go).
- Solve **LRU** completely before tackling parsing—quick wins boost confidence.
- For the parser, add temporary `System.out.println("i="+i+" ch="+peek())` while debugging, then remove.
- If a test fails, re-run **just that test** via `--tests` for speed.

---

Good luck! Finish LRU fast, then parser, then text. When all tests pass here, you’ll be ready for the real repo’s tests and 90-minute pressure.
