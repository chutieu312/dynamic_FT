package app;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for ReportService
 * Tests all report types and edge cases
 */
public class ReportServiceTest {
    
    private ReportService reportService;
    
    public void setUp() {
        reportService = new ReportService();
    }
    
    public void testDailyReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "DAILY");
        params.put("start", LocalDate.of(2025, 10, 1));
        params.put("end", LocalDate.of(2025, 10, 1));
        params.put("items", Arrays.asList(1.0, 2.0, 7.0));
        
        String result = reportService.generate(params);
        
        assertContains(result, "Daily Report");
        assertContains(result, "2025-10-01 - 2025-10-01");
        assertContains(result, "Count: 3");
        assertContains(result, "Sum: 10.0");
        assertContains(result, "Avg: 3.3333333333333335");
        assertContains(result, "! daily-mode");
    }
    
    public void testWeeklyReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "WEEKLY");
        params.put("start", LocalDate.of(2025, 10, 1));
        params.put("end", LocalDate.of(2025, 10, 7));
        params.put("items", Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0));
        
        String result = reportService.generate(params);
        
        assertContains(result, "Weekly Report");
        assertContains(result, "2025-10-01 - 2025-10-07");
        assertContains(result, "Count: 5");
        assertContains(result, "Sum: 15.0");
        assertContains(result, "Avg: 3.0");
        assertContains(result, "! weekly-mode");
    }
    
    public void testMonthlyReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "MONTHLY");
        params.put("start", LocalDate.of(2025, 10, 1));
        params.put("end", LocalDate.of(2025, 10, 31));
        params.put("items", Arrays.asList(10.0, 20.0));
        
        String result = reportService.generate(params);
        
        assertContains(result, "Monthly Report");
        assertContains(result, "2025-10-01 - 2025-10-31");
        assertContains(result, "Count: 2");
        assertContains(result, "Sum: 30.0");
        assertContains(result, "Avg: 15.0");
        assertContains(result, "! monthly-mode");
    }
    
    public void testNullDefaults() {
        Map<String, Object> params = new HashMap<>();
        // All null - should default to DAILY with empty items and today's date
        
        String result = reportService.generate(params);
        
        assertContains(result, "Daily Report");
        assertContains(result, "Count: 0");
        assertContains(result, "Sum: 0.0");
        assertContains(result, "Avg: 0");
        assertContains(result, "! daily-mode");
    }
    
    public void testEmptyItems() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "WEEKLY");
        params.put("items", Arrays.asList());
        
        String result = reportService.generate(params);
        
        assertContains(result, "Count: 0");
        assertContains(result, "Sum: 0.0");
        assertContains(result, "Avg: 0");
    }
    
    public void testUnknownType() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "INVALID");
        params.put("items", Arrays.asList(5.0));
        
        String result = reportService.generate(params);
        
        assertContains(result, "Unknown Report");
        assertContains(result, "! unknown-mode");
    }
    
    public void testSingleItem() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "DAILY");
        params.put("items", Arrays.asList(42.5));
        
        String result = reportService.generate(params);
        
        assertContains(result, "Count: 1");
        assertContains(result, "Sum: 42.5");
        assertContains(result, "Avg: 42.5");
    }
    
    // Simple assertion helper
    private void assertContains(String actual, String expected) {
        if (!actual.contains(expected)) {
            throw new AssertionError("Expected to find: '" + expected + "' in: '" + actual + "'");
        }
    }
    
    // Test runner
    public static void main(String[] args) {
        ReportServiceTest test = new ReportServiceTest();
        
        try {
            test.setUp();
            test.testDailyReport();
            System.out.println("✓ testDailyReport passed");
            
            test.setUp();
            test.testWeeklyReport();
            System.out.println("✓ testWeeklyReport passed");
            
            test.setUp();
            test.testMonthlyReport();
            System.out.println("✓ testMonthlyReport passed");
            
            test.setUp();
            test.testNullDefaults();
            System.out.println("✓ testNullDefaults passed");
            
            test.setUp();
            test.testEmptyItems();
            System.out.println("✓ testEmptyItems passed");
            
            test.setUp();
            test.testUnknownType();
            System.out.println("✓ testUnknownType passed");
            
            test.setUp();
            test.testSingleItem();
            System.out.println("✓ testSingleItem passed");
            
            System.out.println("\n🎉 All tests passed!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}