: Author: Lukas Elmer, 2011
ECHO OFF
CLS
:MENU
ECHO.
ECHO *********************************************
ECHO * MRT Gource - SVN repository visualization *
ECHO *********************************************
ECHO.
ECHO s - Slow run
ECHO m - Medium run
ECHO f - Fast run
ECHO a - Export slow
ECHO b - Export fast
ECHO e - Exit
ECHO.
CHOICE /C "smfabe" /M "What would you like to do?"
IF %ERRORLEVEL%==1 GOTO SLOW
IF %ERRORLEVEL%==2 GOTO MEDIUM
IF %ERRORLEVEL%==3 GOTO FAST
IF %ERRORLEVEL%==4 GOTO EXPORT_SLOW
IF %ERRORLEVEL%==5 GOTO EXPORT_FAST
IF %ERRORLEVEL%==6 GOTO END
GOTO MENU

:SLOW
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-files 0 --key ../../../
cd ../
GOTO MENU

:MEDIUM
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-file-lag 1 --max-files 0 --key -s 0.3 -a 0.1  ../../../
cd ../
GOTO MENU

:FAST
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users  --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-file-lag 0.1 --max-files 0 --key -s 0.03 -a 0.01 ../../../
GOTO MENU

:EXPORT_SLOW
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-files 0 --key ../../../ -o out/ppm_slow.out
ffmpeg -y -b 500000K -r 30 -f image2pipe -vcodec ppm -i out/ppm_slow.out -vcodec libx264 -threads 0 out/gource.slow.x264.avi
:rm ppm_slow.out
cd ../
GOTO MENU

:EXPORT_FAST
cd gource-0.32.win32/
gource.exe --user-scale 2 --highlight-users  --title "MRT - Mobile Reporting Tool" --logo userdata/logo.png --user-image-dir userdata/avatars/ --file-idle-time 0 --max-file-lag 0.1 --max-files 0 --key -s 0.03 -a 0.01 ../../../ -o out/ppm_fast.out
ffmpeg -y -b 500000K -r 30 -f image2pipe -vcodec ppm -i out/ppm_fast.out -vcodec libx264 -threads 0 out/gource.fast.x264.avi
:rm ppm_fast.out
cd ../
GOTO MENU

:ffmpeg -y -b 3000K -r 60 -f image2pipe -vcodec ppm -i - -vcodec libx264 -vpre slow -threads 0 gource.mp4
:ffmpeg -y -b 3000K -r 60 -f image2pipe -vcodec ppm -i ppm.out -vcodec libx264 -fpre "C:\\ffmpeg\\presets\\libx264-slow.ffpreset" -threads 0 gource.x264.avi

cls
