# Password Checker (Java Swing UI)

Password Checker is a Java Swing desktop app for creating accounts and signing in with password strength analysis.

## Features

- Create account and sign in UI
- Password strength score + animated result view
- Username existence checks (real-time)
- Password hashing before storage
- Local text-file user database (`users.txt`)

## Requirements

- Java JDK 8 or newer (JDK 17 recommended)
- `lib/flatlaf.jar` must exist in the project

## Project Structure

Run commands from inside the `PasswordChecker` directory:

```text
PasswordChecker/
  lib/flatlaf.jar
  src/
  out/                (generated after compile)
  users.txt           (generated on first signup)
```

## How To Run

### One-click launcher files

- macOS/Linux: run `./run-ui.sh`
- macOS double-click: open `run-ui.command`
- Windows: run `run-ui.bat`

### macOS/Linux (one command)

```bash
mkdir -p out && javac -cp "lib/flatlaf.jar" -d out src/exception/InvalidPasswordException.java src/exception/RecordExistException.java src/logic/PasswordScore.java src/logic/UserStore.java src/model/Password.java src/ui/AuthPanel.java src/ui/Hash.java src/ui/MainApp.java src/ui/ResultPanel.java && java -cp "out:lib/flatlaf.jar" ui.MainApp
```

### Windows CMD (one command)

```bat
if not exist out mkdir out && javac -cp "lib\flatlaf.jar" -d out src\exception\InvalidPasswordException.java src\exception\RecordExistException.java src\logic\PasswordScore.java src\logic\UserStore.java src\model\Password.java src\ui\AuthPanel.java src\ui\Hash.java src\ui\MainApp.java src\ui\ResultPanel.java && java -cp "out;lib\flatlaf.jar" ui.MainApp
```

## Run in Any IDE (IntelliJ/Eclipse/VS Code/NetBeans)

This repo now includes `pom.xml` (Maven), so most IDEs can run it directly:

1. Open/import the `PasswordChecker` folder as a Maven project.
2. Let the IDE load dependencies.
3. Run main class `ui.MainApp`.

Optional Maven terminal run:

```bash
mvn exec:java
```

## Notes

- Credentials are saved to `users.txt` as `username:hashedPassword`.
- `users.txt` is created automatically when needed.
- Do not commit `users.txt` to version control.
