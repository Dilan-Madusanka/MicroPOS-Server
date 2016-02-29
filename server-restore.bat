@ECHO OFF

ECHO =========================================================
ECHO                   DATABASE RESTORE TOOL
ECHO =========================================================
ECHO.

SET index=1

ECHO Available backup files...

SETLOCAL ENABLEDELAYEDEXPANSION
FOR %%f IN (micropos\backup\*.*) DO (
   SET file!index!=%%f
   ECHO !index! - %%f
   SET /A index=!index!+1
)
ECHO.
SETLOCAL DISABLEDELAYEDEXPANSION

SET /P selection="Select database restore file:"
ECHO.

SET file%selection% >nul 2>&1

IF ERRORLEVEL 1 (
   ECHO Invalid file selection.
   GOTO :FINISH
)

CALL :RESOLVE %%file%selection%%%

GOTO :EOF

:RESOLVE
SET file_name=%1
ECHO Restoring to %file_name%.....
ECHO.
micropos\mysql -u micropos -p micropos < %file_name%

ECHO.
ECHO DONE.

:FINISH
ECHO.
ECHO =========================================================
ECHO                   DATABASE RESTORE TOOL
ECHO =========================================================
PAUSE