#!/bin/bash
cd src/main/java
rm -f app/*.class
javac app/ReportType.java app/ReportService.java app/Main.java app/ReportServiceTest.java
if [ $? -eq 0 ]; then
    echo "=== Running Main ==="
    java app.Main
    echo ""
    echo "=== Running Tests ==="
    java app.ReportServiceTest
else
    echo "Compilation failed!"
fi