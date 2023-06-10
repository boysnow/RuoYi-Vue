@echo off

rem jar
set AppName=ruoyi-admin.jar
set path=%path%;C:\pleiades\java\8\bin

rem JVM options
set JVM_OPTS="-Dname=%AppName%  -Duser.timezone=Asia/Tokyo -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"


ECHO.
	ECHO.  [1] start %AppName%
	ECHO.  [2] stop %AppName%
	ECHO.  [3] restart %AppName%
	ECHO.  [4] status %AppName%
	ECHO.  [5] quit
ECHO.

ECHO.Please input the command no:
set /p ID=
	IF "%id%"=="1" GOTO start
	IF "%id%"=="2" GOTO stop
	IF "%id%"=="3" GOTO restart
	IF "%id%"=="4" GOTO status
	IF "%id%"=="5" EXIT
PAUSE
:start
    for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %AppName%`) do (
		set pid=%%a
		set image_name=%%b
	)
	echo %pid%
	if  defined pid (
		echo %%is running
		PAUSE
	)

start javaw %JVM_OPTS% -jar %AppName%

echo  starting...
echo  Start %AppName% success...
goto:eof

rem confirm the pid
:stop
	for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %AppName%`) do (
		set pid=%%a
		set image_name=%%b
	)
	if not defined pid (echo process %AppName% does not exists) else (
		echo prepare to kill %image_name%
		echo start kill %pid% ...
		rem kill the process by PID
		taskkill /f /pid %pid%
	)
goto:eof
:restart
	call :stop
    call :start
goto:eof
:status
	for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %AppName%`) do (
		set pid=%%a
		set image_name=%%b
	)
	if not defined pid (echo process %AppName% is dead ) else (
		echo %image_name% is running
	)
goto:eof