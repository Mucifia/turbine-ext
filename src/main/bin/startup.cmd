@REM turbine
@echo off
@setlocal


@REM Basic Folder
set "EXEC_JAR_NAME=Turbine_Server.jar"
set "DIR_BIN=..\bin"
set "DIR_LOG=..\log"
set "DIR_LIB=..\libs"
set "DIR_CONF=..\config"

set "FILE_LOG=%DIR_LOG%\turbine.log"
set "FILE_LIB=%DIR_LIB%\%EXEC_JAR_NAME%"
set "FILE_PID=%DIR_LIB%\turbine.pid"

@REM User define jdk path
set "JRE_LOCATION=..\..\jre"


set "JAVACMD=%JRE_LOCATION%\bin\java.exe"

if exist "%JAVACMD%" goto startService

if not "%JAVA_HOME%"=="" goto okJHome
for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
goto checkJCmd

:okJHome
set "JAVACMD=%JAVA_HOME%\bin\java.exe"

:checkJCmd
if exist "%JAVACMD%" goto CheckJVersion
echo Could not fould Java executable ,Please Check your Java enviroment! >&2
goto end

:CheckJVersion
for /f tokens^=2-5^ delims^=.-_^" %%j in ('"%JAVACMD%" -fullversion 2^>^&1') do set "jver=%%j.%%k%%l%%m"
if "%jver%" gtr "1.8" goto startService
echo Java version is %jver%, please make sure java 1.8+ is in your java enviroment >&2
goto end

@REM Start PTP Turbine

:startService

set "JAVA_OPTS=-Dlogging.file=%FILE_LOG% -Dspring.pid.file=%FILE_PID% -Dspring.config.location=%DIR_CONF%\"

cd %DIR_LIB%

"%JAVACMD%" %JAVA_OPTS% -jar %FILE_LIB%

:end
@endlocal
