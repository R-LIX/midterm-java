package logic;

import exception.RecordExistException;
import model.Password;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UserStore {
    private static final Path DB_PATH = Paths.get("users.txt");
    private static final String SEPARATOR = ":";

    private UserStore() {
    }

    public static boolean usernameExists(String username) throws IOException {
        String normalizedUsername = normalizeUsername(username);
        return loadUsers().containsKey(normalizedUsername);
    }

    public static void createUser(String username, String rawPassword) throws IOException, RecordExistException {
        String normalizedUsername = normalizeUsername(username);
        Map<String, String> users = loadUsers();
        if (users.containsKey(normalizedUsername)) {
            throw new RecordExistException("Username already exists");
        }

        String hashedPassword = hashPassword(rawPassword);
        String line = normalizedUsername + SEPARATOR + hashedPassword + System.lineSeparator();
        Files.writeString(
            DB_PATH,
            line,
            StandardCharsets.UTF_8,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        );
    }

    public static boolean validateLogin(String username, String rawPassword) throws IOException {
        String normalizedUsername = normalizeUsername(username);
        Map<String, String> users = loadUsers();
        if (!users.containsKey(normalizedUsername)) {
            return false;
        }

        String hashedInputPassword = hashPassword(rawPassword);
        String storedHash = users.get(normalizedUsername);
        return storedHash.equals(hashedInputPassword);
    }

    private static Map<String, String> loadUsers() throws IOException {
        ensureDbExists();

        List<String> lines = Files.readAllLines(DB_PATH, StandardCharsets.UTF_8);
        Map<String, String> users = new LinkedHashMap<>();
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            int splitIndex = line.indexOf(SEPARATOR);
            if (splitIndex <= 0 || splitIndex == line.length() - 1) {
                continue;
            }

            String username = line.substring(0, splitIndex).trim();
            String passwordHash = line.substring(splitIndex + 1).trim();
            if (!username.isEmpty() && !passwordHash.isEmpty()) {
                users.put(username, passwordHash);
            }
        }

        return users;
    }

    private static void ensureDbExists() throws IOException {
        if (Files.notExists(DB_PATH)) {
            Files.createFile(DB_PATH);
        }
    }

    private static String normalizeUsername(String username) {
        return username.trim().toLowerCase();
    }

    private static String hashPassword(String rawPassword) throws IOException {
        try {
            return Password.getMD5(rawPassword);
        } catch (Exception e) {
            throw new IOException("Failed to hash password", e);
        }
    }
}
