
ECHO OFF
CLS
:MENU
ECHO.
ECHO *********************************************
ECHO * MRT Gource - SVN repository visualization *
ECHO *********************************************
ECHO.
ECHO s - Slow run
ECHO f - Fast run
ECHO e - Exit
ECHO.
CHOICE /C "sfe" /M "What would you like to do?"
IF %ERRORLEVEL%==1 GOTO SLOW
IF %ERRORLEVEL%==2 GOTO FAST
IF %ERRORLEVEL%==3 GOTO END
GOTO MENU

:SLOW
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-files 0 --key ../../
cd ../
GOTO MENU

:FAST
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users  --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-file-lag 0.1 --max-files 0 --key -s 0.03 -a 0.01 ../../
cd ../
GOTO MENU

:END
cls
