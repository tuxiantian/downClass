@ECHO OFF
@ECHO STARTUP App
@ECHO ���û�������,ѭ����ǰĿ¼�µ�libĿ¼������jar�ļ�,������CLASSPATH

del /f .\config\file.txt
copy /y .\file.txt .\config\file.txt


FOR %%F IN (lib\*.jar) DO call :addcp %%F
goto extlibe

:addcp
SET CLASSPATH=%CLASSPATH%;%1
goto :eof

:extlibe
@ECHO ��Ҫ���е�jar���õ�CLASSPATH��
SET CLASSPATH=%CLASSPATH%
@ECHO ��ʾCLASSPATH
SET CLASSPATH
@ECHO ����Ӧ�ó���
java -server com.smz.core.GetClassFile
@echo ================================================
pause