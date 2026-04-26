@echo off
setlocal

cd /d "%~dp0"

if not exist out mkdir out

javac -cp "lib\flatlaf.jar" -d out ^
  src\exception\InvalidPasswordException.java ^
  src\exception\RecordExistException.java ^
  src\logic\PasswordScore.java ^
  src\logic\UserStore.java ^
  src\model\Password.java ^
  src\ui\AuthPanel.java ^
  src\ui\Hash.java ^
  src\ui\MainApp.java ^
  src\ui\ResultPanel.java

if errorlevel 1 exit /b 1

java -cp "out;lib\flatlaf.jar" ui.MainApp
