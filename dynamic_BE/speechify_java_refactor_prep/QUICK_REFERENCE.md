# Quick Refactoring Reference Card

## 🚨 50-Minute Time Allocation
- **0-5 min**: Read instructions + understand codebase
- **5-10 min**: Identify top 5 code smells  
- **10-40 min**: Execute refactoring in passes
- **40-50 min**: Test, commit, push, README

## 🔍 Priority Code Smells (Attack First)
1. **Long methods** (>20 lines) → Extract Method
2. **Magic strings/numbers** → Constants/Enums  
3. **Primitive obsession** (Map<String,Object>) → Value Objects
4. **Duplicate conditionals** → Strategy Pattern
5. **Mixed responsibilities** → Extract Class

## ⚡ Java Refactoring Patterns

### Extract Method
```java
// Before: 30-line method
public void process() { /* validation + calculation + formatting */ }

// After: Single responsibility methods
public void process() {
    validate();
    calculate(); 
    format();
}
```

### Parameter Object
```java
// Before: Too many params
createReport(String type, LocalDate start, LocalDate end, List<Double> items)

// After: Record
record ReportRequest(ReportType type, LocalDate start, LocalDate end, List<Double> items) {}
```

### Replace Conditionals
```java
// Before: if-else chains
if (type.equals("DAILY")) return "Daily Report";
else if (type.equals("WEEKLY")) return "Weekly Report";

// After: Enum with behavior
enum ReportType {
    DAILY("Daily Report"), WEEKLY("Weekly Report");
    public String getTitle() { return title; }
}
```

### Strategy Pattern
```java
// Before: Conditional logic scattered
if (paymentType.equals("CREDIT")) { /* credit logic */ }
else if (paymentType.equals("PAYPAL")) { /* paypal logic */ }

// After: Strategy interface
interface PaymentProcessor { void process(Payment p); }
Map<PaymentType, PaymentProcessor> processors = Map.of(
    CREDIT, new CreditProcessor(),
    PAYPAL, new PayPalProcessor()
);
```

## 🎯 5-Pass Refactoring Strategy

### Pass 1: Safety (10-15 min)
- [ ] Add test harness if no tests exist
- [ ] Verify current behavior works
- [ ] Small extractions with immediate verification

### Pass 2: Structure (15-25 min)  
- [ ] Extract methods from long functions
- [ ] Split responsibilities into separate classes
- [ ] Introduce value objects (Records, enums)
- [ ] Replace conditionals with polymorphism/strategy

### Pass 3: Clarity (25-30 min)
- [ ] Rename to intent-revealing names
- [ ] Remove dead code
- [ ] Add guard clauses to reduce nesting
- [ ] Parameter objects for long param lists

### Pass 4: Boundaries (30-35 min)
- [ ] Interfaces for external dependencies
- [ ] Constructor injection for DI
- [ ] Immutable DTOs (records/final fields)

### Pass 5: Cleanup (35-40 min)
- [ ] Add logging (only if logger exists)
- [ ] Input validation
- [ ] Replace magic values with constants/enums

## 🎯 Commit Message Templates
```
refactor(service): extract validation logic into separate methods
refactor(model): replace Map params with ReportRequest record  
refactor(enum): introduce ReportType enum to eliminate magic strings
test(service): add behavior verification harness
```

## 🏆 Success Checklist
- [ ] **Code compiles** after every change
- [ ] **Behavior preserved** (tests pass / manual verification)
- [ ] **3-6 meaningful commits** with clear messages
- [ ] **Brief README** explaining changes
- [ ] **Pushed within 50 minutes**

## 💡 Emergency Tips
- **Stuck?** Start with obvious wins (magic numbers → constants)
- **Running late?** Focus on biggest impact changes first
- **Complex refactoring?** Break into smaller steps
- **Unsure?** Keep current behavior, improve structure
- **Time running out?** Commit what you have, then continue

## 🚨 Red Flags to Avoid
- ❌ Using AI/Copilot (instant disqualification)
- ❌ Copy-pasting from Stack Overflow
- ❌ Adding external libraries
- ❌ Breaking existing functionality
- ❌ Over-engineering simple problems
- ❌ Spending too long on perfect solutions

## 🎯 Assessment Success Mantras
- *"Read carefully, refactor systematically, verify constantly"*
- *"Quality over quantity - fewer things done well"*
- *"Every change improves readability and maintainability"*
- *"I trust my practice and apply proven patterns"*

**Trust your practice - you've got this! 🚀**