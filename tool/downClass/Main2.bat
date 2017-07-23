@ECHO OFF
@ECHO STARTUP App
@ECHO 设置环境变量,循环当前目录下的lib目录下所有jar文件,并设置CLASSPATH

del /f .\config\file.txt
copy /y .\file.txt .\config\file.txt


FOR %%F IN (lib\*.jar) DO call :addcp %%F
goto extlibe

:addcp
SET CLASSPATH=%CLASSPATH%;%1
goto :eof

:extlibe
@ECHO 当要运行的jar设置到CLASSPATH中
SET CLASSPATH=%CLASSPATH%
@ECHO 显示CLASSPATH
SET CLASSPATH
@ECHO 运行应用程序
java -server com.smz.core.GetClassFile
@echo ================================================
pause