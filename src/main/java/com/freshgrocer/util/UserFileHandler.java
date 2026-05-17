package com.freshgrocer.util;

import com.freshgrocer.model.CustomerUser;
import java.io.*;
import java.util.*;

public class UserFileHandler {

    // BUG FIX: relative "data/" path breaks when the working directory changes at runtime.
    // Use a path anchored to the user's home directory so it is always predictable.
    private static final String DATA_DIR   = System.getProperty("user.home") + File.separator + "freshgrocer-data";
    private static final String USERS_FILE  = DATA_DIR + File.separator + "users.txt";
    private static final String ADMINS_FILE = DATA_DIR + File.separator + "admins.txt";

    public UserFileHandler() {
        new File(DATA_DIR).mkdirs();
        try {
            new File(USERS_FILE).createNewFile();
            new File(ADMINS_FILE).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeCustomer(CustomerUser c) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(c.toFileString());
            bw.newLine();
        }
    }

    public List<String> readAllUserLines() throws IOException {
        return readLines(USERS_FILE);
    }

    public List<String> readAllAdminLines() throws IOException {
        return readLines(ADMINS_FILE);
    }

    private List<String> readLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line.trim());
            }
        }
        return lines;
    }

    public void overwriteUsers(List<String> lines) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, false))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public boolean emailExists(String email) throws IOException {
        for (String line : readAllUserLines()) {
            String[] parts = line.split(",");
            if (parts.length > 3 && parts[3].equalsIgnoreCase(email)) return true;
        }
        return false;
    }
}
