#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

mkdir -p out
javac -cp "lib/flatlaf.jar" -d out \
  src/exception/InvalidPasswordException.java \
  src/exception/RecordExistException.java \
  src/logic/PasswordScore.java \
  src/logic/UserStore.java \
  src/model/Password.java \
  src/ui/AuthPanel.java \
  src/ui/Hash.java \
  src/ui/MainApp.java \
  src/ui/ResultPanel.java

java -cp "out:lib/flatlaf.jar" ui.MainApp
