#!/bin/bash
cd src/main/java
rm -f app/*.class
javac app/ReportType.java app/ReportService.java app/Main.java
if [ $? -eq 0 ]; then
    echo "Compilation successful, running..."
    java app.Main
else
    echo "Compilation failed!"
fi