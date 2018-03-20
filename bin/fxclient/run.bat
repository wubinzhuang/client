@echo off
if "%1" == "h" goto begin
mshta vbscript:createobject("wscript.shell").run("""%~nx0"" h",0)(window.close)&&exit
:begin
REM
java -classpath /lib -jar fxclient-app-0.0.1-SNAPSHOT.jar


