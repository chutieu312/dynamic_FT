package app;

import java.time.LocalDate;
import java.util.*;

public class ReportService {
    public String generate(Map<String, Object> params) {
        // expected params: type: String ("DAILY"|"WEEKLY"|"MONTHLY"), 
        // start: LocalDate, end: LocalDate, items: List<Double>

        String typeStr = (String) params.get("type");
        LocalDate start = (LocalDate) params.get("start");
        LocalDate end = (LocalDate) params.get("end");
        List<Double> items = (List<Double>) params.get("items");
        
        ReportType type = ReportType.fromString(typeStr, true);
        if (items == null) items = new ArrayList<>();
        if (start == null) start = LocalDate.now();
        if (end == null) end = start;

        Statistics stats = calculateStatistics(items);

        String h = type.title();

        StringBuilder sb = new StringBuilder();
        sb.append(h).append(" Report ");
        sb.append(start.toString()).append(" - ").append(end.toString()).append("\n");
        sb.append("Count: ").append(stats.count()).append("\n");
        sb.append("Sum: ").append(stats.sum()).append("\n");
        sb.append("Avg: ").append(stats.average()).append("\n");

        sb.append(type.mode()).append("\n");

        return sb.toString();
    }
    
    private Statistics calculateStatistics(List<Double> items) {
        double sum = 0;
        for (int i = 0; i < items.size(); i++) sum += items.get(i);
        double avg = items.size() == 0 ? 0 : sum / items.size();
        return new Statistics(items.size(), sum, avg);
    }
}

record Statistics(int count, double sum, double average) {}