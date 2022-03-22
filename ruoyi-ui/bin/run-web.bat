@echo off
echo.
echo Run the web project using the Vue CLI command
echo.

%~d0
cd %~dp0

cd ..
npm run dev

pause