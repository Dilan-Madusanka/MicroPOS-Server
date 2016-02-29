@ECHO off

ECHO =========================================================
ECHO                   DATABASE BACK UP TOOL
ECHO =========================================================
ECHO.

SET SAVESTAMP=%DATE:/=-%_%TIME::=-%

SET SAVESTAMP=manual_%SAVESTAMP: =%.sql

ECHO Backing up to micropos\backup\%SAVESTAMP%.....
ECHO.
micropos\mysqldump -u micropos -p micropos > micropos\backup\%SAVESTAMP%

ECHO.
ECHO Done.

ECHO.
ECHO =========================================================
ECHO                   DATABASE BACK UP TOOL
ECHO =========================================================
PAUSE