@echo off
echo.
echo packaging war/jar
echo.

set JAVA_HOME=C:\pleiades\java\8

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true

pause