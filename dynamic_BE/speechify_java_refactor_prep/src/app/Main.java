package app;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        var svc = new ReportService();
        var out = svc.generate(Map.of(
            "type", "DAILY",
            "start", LocalDate.of(2025,10,1),
            "end", LocalDate.of(2025,10,1),
            "items", List.of(2.0, 3.0, 5.0)
        ));
        System.out.println(out);
        
        // Test weekly report
        System.out.println("\n--- Weekly Report ---");
        var weekly = svc.generate(Map.of(
            "type", "WEEKLY",
            "start", LocalDate.of(2025,10,1),
            "end", LocalDate.of(2025,10,7),
            "items", List.of(1.0, 2.0, 3.0, 4.0, 5.0)
        ));
        System.out.println(weekly);
    }
}