@echo off
echo Starting Spring Boot Application...
echo.
echo If you see any errors, please make sure:
echo 1. Java 17+ is installed
echo 2. You are in the correct directory
echo 3. All files are saved
echo.
echo Setting optimized memory options...
set MAVEN_OPTS=-Xms128m -Xmx384m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxGCPauseMillis=200
.\mvnw.cmd spring-boot:run
pause 