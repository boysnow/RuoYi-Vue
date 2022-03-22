@echo off
echo.
echo Run the web project using the Jar command
echo.

rem set JAVA_HOME=C:\pleiades\java\8
set path=%path%;C:\pleiades\java\8\bin

cd %~dp0
cd ../ruoyi-admin/target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -jar %JAVA_OPTS% ruoyi-admin.jar

cd bin
pause