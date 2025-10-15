package app;

import java.time.LocalDate;
import java.util.*;

public class ReportService {
    public String generate(Map<String, Object> params) {
        // expected params: type: String ("DAILY"|"WEEKLY"|"MONTHLY"), start: LocalDate, end: LocalDate, items: List<Double>
        String type = (String) params.get("type");
        LocalDate start = (LocalDate) params.get("start");
        LocalDate end = (LocalDate) params.get("end");
        List<Double> items = (List<Double>) params.get("items");
        if (type == null) type = "DAILY";
        if (items == null) items = new ArrayList<>();
        if (start == null) start = LocalDate.now();
        if (end == null) end = start;

        double sum = 0;
        for (int i = 0; i < items.size(); i++) sum += items.get(i);
        double avg = items.size() == 0 ? 0 : sum / items.size();

        String h = "";
        if (type.equals("DAILY")) h = "Daily";
        else if (type.equals("WEEKLY")) h = "Weekly";
        else if (type.equals("MONTHLY")) h = "Monthly";
        else h = "Unknown";

        StringBuilder sb = new StringBuilder();
        sb.append(h).append(" Report ");
        sb.append(start.toString()).append(" - ").append(end.toString()).append("\n");
        sb.append("Count: ").append(items.size()).append("\n");
        sb.append("Sum: ").append(sum).append("\n");
        sb.append("Avg: ").append(avg).append("\n");

        if (type.equals("DAILY")) {
            // daily extra formatting
            sb.append("! daily-mode\n");
        } else if (type.equals("WEEKLY")) {
            // weekly extra formatting
            sb.append("! weekly-mode\n");
        } else if (type.equals("MONTHLY")) {
            sb.append("! monthly-mode\n");
        } else {
            sb.append("! unknown-mode\n");
        }

        return sb.toString();
    }
}