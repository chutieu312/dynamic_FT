package app;

enum ReportType {
  DAILY("Daily", "! daily-mode"),
  WEEKLY("Weekly", "! weekly-mode"),
  MONTHLY("Monthly", "! monthly-mode"),
  UNKNOWN("Unknown", "! unknown-mode");

  private final String title;
  private final String mode;
  
  ReportType(String title, String mode) { 
    this.title = title; 
    this.mode = mode;
  }
  
  String title() { return title; }
  String mode() { return mode; }

  static ReportType fromString(String s, boolean nullMeansDaily) {
    if (s == null) return nullMeansDaily ? DAILY : UNKNOWN;
    return switch (s) {
      case "DAILY"   -> DAILY;
      case "WEEKLY"  -> WEEKLY;
      case "MONTHLY" -> MONTHLY;
      default        -> UNKNOWN;
    };
  }
}
