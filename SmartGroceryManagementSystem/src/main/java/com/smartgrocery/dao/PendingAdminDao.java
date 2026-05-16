package com.smartgrocery.dao;

import com.smartgrocery.model.PendingAdmin;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PendingAdminDao {

    private static final String PENDING_FILE = Constants.DATA_DIR + "pending_admins.txt";

    public List<PendingAdmin> findAll() {
        List<PendingAdmin> list = new ArrayList<>();
        File file = new File(PENDING_FILE);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    list.add(new PendingAdmin(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PendingAdmin> findByStatus(String status) {
        List<PendingAdmin> result = new ArrayList<>();
        for (PendingAdmin pa : findAll()) {
            if (pa.getStatus().equals(status)) {
                result.add(pa);
            }
        }
        return result;
    }

    public boolean save(PendingAdmin pending) {
        if (pending.getId() == null) {
            pending.setId("PAD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PENDING_FILE, true))) {
            bw.write(formatLine(pending));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(PendingAdmin pending) {
        List<PendingAdmin> list = findAll();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(pending.getId())) {
                list.set(i, pending);
                return rewriteFile(list);
            }
        }
        return false;
    }

    private boolean rewriteFile(List<PendingAdmin> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PENDING_FILE, false))) {
            for (PendingAdmin pa : list) {
                bw.write(formatLine(pa));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatLine(PendingAdmin pa) {
        return pa.getId() + "|" + pa.getUsername() + "|" + pa.getPassword() + "|" +
                pa.getEmail() + "|" + pa.getRequestDate() + "|" + pa.getStatus();
    }
}