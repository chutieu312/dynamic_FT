# Speechify Java Refactoring Prep

## 🚨 CRITICAL: Assessment Details
- **Date**: October 18, 2025
- **Time**: 50 minutes total
- **Language**: Java  
- **Recording**: Camera + full screen share required
- **Restrictions**: No AI/Copilot, no external libraries, no copy-paste from Stack Overflow

## 🎯 50-Minute Strategy

### Phase 1: Setup & Understanding (0-5 min)
- [ ] **Read ALL instructions** in the repo
- [ ] **Run the project/tests** once to verify baseline
- [ ] **Set time alerts** for each phase

### Phase 2: Code Audit (5-10 min)
Identify top code smells:
- [ ] **Long methods** (>20 lines)
- [ ] **God classes** (>200 lines) 
- [ ] **Magic strings/numbers**
- [ ] **Deep nesting**
- [ ] **Duplicate code**
- [ ] **Primitive obsession**
- [ ] **Feature envy**

### Phase 3: Layered Refactoring (10-40 min)

#### Pass 1: Safety (10-15 min)
- [ ] **Add test harness** if no tests exist
- [ ] **Verify current behavior** works
- [ ] **Small extractions** with immediate verification

#### Pass 2: Structure (15-25 min)
- [ ] **Extract methods** from long functions
- [ ] **Split responsibilities** into separate classes
- [ ] **Introduce value objects** (Records, enums)
- [ ] **Replace conditionals** with polymorphism/strategy

#### Pass 3: Clarity (25-30 min)
- [ ] **Rename** to intent-revealing names
- [ ] **Remove dead code**
- [ ] **Add guard clauses** to reduce nesting
- [ ] **Parameter objects** for long param lists

#### Pass 4: Boundaries (30-35 min)
- [ ] **Interfaces** for external dependencies
- [ ] **Constructor injection** for DI
- [ ] **Immutable DTOs** (records/final fields)

#### Pass 5: Cleanup (35-40 min)
- [ ] **Add logging** (only if logger exists)
- [ ] **Input validation**
- [ ] **Replace magic values** with constants/enums

### Phase 4: Finalization (40-50 min)
- [ ] **Run tests/app** again
- [ ] **Remove TODOs, FIXMEs, dead code**
- [ ] **Clean imports**
- [ ] **Commit with clear messages**
- [ ] **Push to GitHub**
- [ ] **Write brief README**

## 🔧 Java Refactoring Quick Hits

### Extract Method/Class
```java
// Before: Long method
public void processOrder() { /* 50 lines */ }

// After: Extracted responsibilities  
public void processOrder() {
    validateOrder();
    calculateTotal();
    saveOrder();
}
```

### Parameter Object Pattern
```java
// Before: Too many parameters
public void createReport(String type, LocalDate start, LocalDate end, List<Double> items)

// After: Value object
public void createReport(ReportRequest request)
record ReportRequest(ReportType type, LocalDate start, LocalDate end, List<Double> items) {}
```

### Replace Conditionals with Polymorphism
```java
// Before: If-else chains
if (type.equals("DAILY")) { /* daily logic */ }
else if (type.equals("WEEKLY")) { /* weekly logic */ }

// After: Strategy pattern
enum ReportType {
    DAILY("Daily"), WEEKLY("Weekly"), MONTHLY("Monthly");
    // + behavior methods
}
```

### Magic Numbers → Constants
```java
// Before
if (items.size() > 100) { /* logic */ }

// After  
private static final int MAX_ITEMS = 100;
if (items.size() > MAX_ITEMS) { /* logic */ }
```

## 📝 Commit Message Style
```
refactor(parser): extract tokenization & remove duplication
refactor(billing): introduce Money value object to replace BigDecimal scatter  
test(order): add happy-path test for price calculation
```

## 🛠️ Environment Setup (Do This Today)

### IDE Configuration (CRITICAL)
#### Disable AI Features in IntelliJ IDEA:
```
File → Settings → Tools → AI Assistant → Disable
File → Settings → Editor → Code Completion → Machine Learning → Disable
Plugins → Disable GitHub Copilot, Tabnine, CodeWhisperer
```

#### Disable AI Features in VS Code:
```
Extensions → Disable GitHub Copilot
Extensions → Disable IntelliCode  
Settings → Search "ai" → Disable all AI-related features
```

### Git & GitHub Setup
```bash
# Test SSH connection
ssh -T git@github.com  # Should say "You've successfully authenticated"

# Configure identity
git config --global user.name "Your Name" 
git config --global user.email "cannguyen312@gmail.com"
```

### Browser & Recording
- [ ] **Chrome browser** ready (Chromium-based required)
- [ ] **Camera permissions** enabled
- [ ] **Microphone permissions** enabled  
- [ ] **Screen sharing** tested
- [ ] **Disable suspicious extensions** during test

## 🚨 Assessment Day Protocol

### 30 Minutes Before
- [ ] **Close unnecessary apps**
- [ ] **Clear desktop** (only essential items visible)
- [ ] **Test camera/mic** in Chrome
- [ ] **Disable notifications** (Windows/Mac)
- [ ] **Check internet stability**
- [ ] **Have water ready**

### 5 Minutes Before  
- [ ] **Open Chrome** (Chromium-based browser required)
- [ ] **Clear browser cache**
- [ ] **Close extra tabs**
- [ ] **Navigate to assessment link**
- [ ] **Deep breath and focus**

## 🔍 Code Smell Detection Guide

### Quick Visual Identification
- 🚨 **Long Method**: More than 20 lines, scrolling required
- 🚨 **God Class**: Class with many methods and responsibilities  
- 🚨 **Magic Numbers**: Hardcoded values (100, 365, etc.)
- 🚨 **Magic Strings**: Hardcoded text ("DAILY", "WEEKLY", etc.)
- 🚨 **Duplicate Code**: Same logic in multiple places
- 🚨 **Deep Nesting**: if-else chains, multiple indentation levels
- 🚨 **Primitive Obsession**: Map<String,Object>, String everywhere
- 🚨 **Feature Envy**: Method uses more methods from another class

### Priority Attack Order
1. **Magic strings/numbers** → Constants/Enums (easy win)
2. **Long methods** → Extract Method (biggest impact)
3. **Primitive obsession** → Value Objects (structural improvement)
4. **Duplicate conditionals** → Strategy Pattern (professional)
5. **Mixed responsibilities** → Extract Class (architecture)

## � During Recording Best Practices

### Camera Etiquette
- **Face visible**: Look professional and focused
- **Good lighting**: Ensure your face is clearly visible
- **Minimal movement**: Stay focused on screen
- **Professional demeanor**: You're being evaluated

### Screen Sharing
- **Full screen sharing** required
- **Clean desktop**: Only relevant windows
- **Large font size**: Ensure code is readable
- **Close distractions**: Email, chat, social media

### Think Aloud (Brief Comments)
- "I'm extracting this method because it's doing too many things"
- "Replacing this magic string with an enum for type safety"
- "This conditional logic can be simplified with a strategy pattern"

## 🚨 Critical Restrictions (RED FLAGS)

### Technical Don'ts
- ❌ **Don't use AI**: Copilot, ChatGPT, any code generation
- ❌ **Don't copy code**: From Stack Overflow or anywhere
- ❌ **Don't add external libraries**
- ❌ **Don't break functionality**
- ❌ **Don't over-complicate**

### Behavioral Don'ts  
- ❌ **Don't panic**: If stuck, take a breath and continue
- ❌ **Don't rush**: Better to do less well than more poorly
- ❌ **Don't ignore instructions**: Follow requirements exactly
- ❌ **Don't multitask**: Focus solely on the assessment

## 🏆 Success Indicators

### You're Ready When:
- ✅ Can identify code smells in under 2 minutes
- ✅ Extract methods automatically without thinking
- ✅ Apply appropriate design patterns naturally  
- ✅ Complete refactoring with time to spare
- ✅ Feel confident explaining your changes

### Course Correction Signals:
- 🚨 **Spending too long** on one issue
- 🚨 **Code doesn't compile**
- 🚨 **Getting overwhelmed** by complexity
- 🚨 **Forgetting to test changes**

## 💪 Assessment Day Mindset

### Success Mantras:
- *"I read carefully, think systematically, refactor incrementally"*
- *"Quality over quantity - fewer things done well"*
- *"I trust my practice and apply proven patterns"*
- *"Every change makes the code better and more maintainable"*

### Remember:
- **You're prepared**: You've practiced this scenario
- **They want to hire you**: Show your systematic thinking
- **Stay calm**: Breathe and follow your proven process
- **Focus on value**: Obvious improvements beat perfect code

### Current Mess: ReportService.java
**Code Smells Present:**
- ✅ **Primitive obsession** (Map<String,Object> params)
- ✅ **Magic strings** ("DAILY", "WEEKLY", "MONTHLY")  
- ✅ **Duplicate conditionals** (type checking repeated)
- ✅ **Mixed responsibilities** (validation + computation + formatting)
- ✅ **Null handling** scattered throughout
- ✅ **StringBuilder** when String.join would be cleaner

### Refactoring Goals
1. **Replace Map params** with ReportRequest record
2. **Extract enum** for ReportType with behavior
3. **Separate concerns** (validation, calculation, formatting)
4. **Remove duplication** in conditional logic
5. **Add input validation** with clear error messages
6. **Use modern Java** features (streams, records, switch expressions)

### Target Architecture
```java
public class ReportService {
    public String generate(ReportRequest req) {
        validate(req);
        ReportData data = calculate(req);
        return format(req.type(), data, req.start(), req.end());
    }
}

record ReportRequest(ReportType type, LocalDate start, LocalDate end, List<Double> items) {
    static ReportRequest from(Map<String, Object> params) { /* adapter */ }
}

enum ReportType {
    DAILY("Daily"), WEEKLY("Weekly"), MONTHLY("Monthly");
    public String formatExtras() { /* type-specific formatting */ }
}
```

## ⏱️ Practice Schedule

### Today (Day 1)
- [ ] **First attempt**: 35-40 minutes, focus on understanding
- [ ] **Second attempt**: 25-30 minutes, focus on speed
- [ ] **Review**: Compare with target solution

### Days 2-3  
- [ ] **Find open-source Java katas** for additional practice
- [ ] **Rehearse 50-minute workflow** end-to-end
- [ ] **Practice commit/push workflow**

### Day 4 (Assessment Day)
- [ ] **Environment setup**: Clean Chrome, disable AI, test camera
- [ ] **Final dry-run**: 45-minute practice session
- [ ] **Assessment**: Execute the plan!

## 🏆 Success Criteria

### Technical Goals
- ✅ **Code compiles** throughout refactoring process
- ✅ **Behavior preserved** (tests pass, app runs same)
- ✅ **Clear improvements** in readability and structure
- ✅ **Professional patterns** applied appropriately

### Process Goals  
- ✅ **Time management** (finish with 3-5 minutes to spare)
- ✅ **Systematic approach** (follow the layered passes)
- ✅ **Clean commits** (3-6 meaningful commits with good messages)
- ✅ **Documentation** (brief but clear README)

## 💡 Assessment Day Reminders

1. **Read instructions completely** (don't skip this!)
2. **Start with safety** (verify current behavior)
3. **Work incrementally** (small changes, frequent verification)
4. **Stay calm** (you've practiced this extensively)
5. **Focus on value** (obvious improvements over perfect code)

**You've got this! 🚀**