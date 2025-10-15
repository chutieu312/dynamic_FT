package app;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REFACTORED ReportService - Target Solution
 * 
 * Improvements made:
 * 1. Replaced primitive obsession (Map) with ReportRequest record
 * 2. Extracted ReportType enum with behavior
 * 3. Separated concerns (validation, calculation, formatting)
 * 4. Eliminated duplicate conditionals
 * 5. Added proper input validation
 * 6. Used modern Java features (records, switch expressions, streams)
 */
public class ReportService {

    public String generate(ReportRequest req) {
        validate(req);
        double sum = sum(req.items());
        double avg = average(req.items());

        String header = header(req.type(), req.start(), req.end());
        String extras = extrasFor(req.type());

        return String.join("\n",
            header,
            "Count: " + req.items().size(),
            "Sum: " + sum,
            "Avg: " + avg,
            extras
        ) + "\n";
    }

    private void validate(ReportRequest r) {
        if (r.start().isAfter(r.end())) {
            throw new IllegalArgumentException("start must be <= end");
        }
    }

    private double sum(List<Double> items) { 
        return items.stream().mapToDouble(Double::doubleValue).sum(); 
    }
    
    private double average(List<Double> items) { 
        return items.isEmpty() ? 0 : sum(items) / items.size(); 
    }

    private String header(ReportType t, LocalDate start, LocalDate end) {
        return t.title() + " Report " + start + " - " + end;
    }

    private String extrasFor(ReportType t) {
        return switch (t) {
            case DAILY -> "! daily-mode";
            case WEEKLY -> "! weekly-mode";
            case MONTHLY -> "! monthly-mode";
        };
    }

    /* --- adapter for legacy map inputs if needed in the assignment --- */
    public String generate(Map<String, Object> params) {
        return generate(ReportRequest.from(params));
    }
}

enum ReportType {
    DAILY("Daily"), WEEKLY("Weekly"), MONTHLY("Monthly");
    
    private final String title;
    
    ReportType(String t) { 
        this.title = t; 
    }
    
    public String title() { 
        return title; 
    }
    
    public static ReportType fromString(String typeStr) {
        if (typeStr == null) return DAILY;
        
        return switch (typeStr.toUpperCase()) {
            case "DAILY" -> DAILY;
            case "WEEKLY" -> WEEKLY;
            case "MONTHLY" -> MONTHLY;
            default -> DAILY;
        };
    }
}

record ReportRequest(ReportType type, LocalDate start, LocalDate end, List<Double> items) {
    
    static ReportRequest from(Map<String, Object> p) {
        var type = ReportType.fromString((String) p.get("type"));
        var start = p.get("start") instanceof LocalDate d ? d : LocalDate.now();
        var end = p.get("end") instanceof LocalDate d ? d : start;
        var items = extractItems(p.get("items"));
        
        return new ReportRequest(type, start, end, items);
    }
    
    @SuppressWarnings("unchecked")
    private static List<Double> extractItems(Object itemsObj) {
        if (itemsObj instanceof List<?> list) {
            return list.stream()
                .filter(o -> o instanceof Number)
                .map(o -> ((Number) o).doubleValue())
                .toList();
        }
        return List.of();
    }
}