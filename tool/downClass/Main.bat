@ECHO OFF
@ECHO STARTUP App
@ECHO ���û�������,ѭ����ǰĿ¼�µ�libĿ¼������jar�ļ�,������CLASSPATH
del /f .\config\file.txt
copy /y .\file.txt .\config\file.txt

for %%F in (lib\*.jar) do call CP %%F

SET CLASSPATH=%CLASSPATH%
java -cp %CLASS_PATH% com.smz.core.GetClassFile

pause

