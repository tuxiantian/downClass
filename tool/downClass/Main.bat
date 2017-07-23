@ECHO OFF
@ECHO STARTUP App
@ECHO 设置环境变量,循环当前目录下的lib目录下所有jar文件,并设置CLASSPATH
del /f .\config\file.txt
copy /y .\file.txt .\config\file.txt

for %%F in (lib\*.jar) do call CP %%F

SET CLASSPATH=%CLASSPATH%
java -cp %CLASS_PATH% com.smz.core.GetClassFile

pause

