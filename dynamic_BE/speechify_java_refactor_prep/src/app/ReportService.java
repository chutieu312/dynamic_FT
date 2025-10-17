package app;

import java.time.LocalDate;
import java.util.*;

public class ReportService {
    public String generate(Map<String, Object> params) {
        return generate(ReportRequest.fromMap(params));
    }
    
    public String generate(ReportRequest request) {
        Statistics stats = calculateStatistics(request.items());
        return formatReport(request, stats);
    }
    
    private String formatReport(ReportRequest request, Statistics stats) {
        return String.join("\n",
            request.type().title() + " Report " + request.start() + " - " + request.end(),
            "Count: " + stats.count(),
            "Sum: " + stats.sum(),
            "Avg: " + stats.average(),
            request.type().mode()
        ) + "\n";
    }
    
    private Statistics calculateStatistics(List<Double> items) {
        double sum = items.stream().mapToDouble(Double::doubleValue).sum();
        double avg = items.isEmpty() ? 0 : sum / items.size();
        return new Statistics(items.size(), sum, avg);
    }
}

record Statistics(int count, double sum, double average) {}



record ReportRequest(ReportType type, LocalDate start, LocalDate end, List<Double> items) {

    public static ReportRequest fromMap( Map<String, Object> params ) {
        String typeString = (String) params.get("type");
        ReportType type = ReportType.fromString(typeString, true);

        LocalDate startDate = (LocalDate) params.get("start");
        LocalDate endDate = (LocalDate) params.get("end");
        List<Double> items = (List<Double>) params.get("items");

        if (items == null) items = new ArrayList<>();
        if (startDate == null) startDate = LocalDate.now();
        if (endDate == null)  endDate = startDate;

        return new ReportRequest(type, startDate, endDate, items);

    }

}

