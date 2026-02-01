@echo off
set JAVA_HOME=%JAVA_HOME%
set MAVEN_HOME=%~dp0apache-maven-3.8.6
set PATH=%MAVEN_HOME%\bin;%PATH%
set JAVA_OPTS=-Xms128m -Xmx384m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxGCPauseMillis=200
mvn.cmd %* 