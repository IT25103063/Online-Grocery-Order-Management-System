package com.freshgrocer.model;

public class CustomerUser extends User {

    private String phoneNumber;
    private String address;
    private String birthday;

    public CustomerUser() {
        super();
        this.setRole("customer");
    }

    public CustomerUser(String userId, String username,
                        String password, String email,
                        String status, String phoneNumber,
                        String address, String birthday) {
        super(userId, username, password, email, "customer", status);
        this.phoneNumber = phoneNumber;
        this.address     = address;
        this.birthday    = birthday;
    }

    @Override
    public boolean login(String email, String password) {
        return this.getEmail().equals(email) &&
                this.getPassword().equals(password) &&
                this.getStatus().equals("active");
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + " | Phone: " + phoneNumber;
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + phoneNumber
                + "," + address + "," + birthday;
    }

    public boolean isBirthdayToday() {
        if (birthday == null || birthday.isEmpty()) return false;
        java.time.LocalDate today = java.time.LocalDate.now();
        String mm = String.valueOf(today.getMonthValue());
        String dd = String.valueOf(today.getDayOfMonth());
        String todayMMDD = (mm.length()==1?"0"+mm:mm) + "-"
                + (dd.length()==1?"0"+dd:dd);
        String bdayMMDD = birthday.length() >= 7
                ? birthday.substring(5,7) + "-" + birthday.substring(8)
                : "";
        return todayMMDD.equals(bdayMMDD);
    }

    public static CustomerUser fromFileString(String line) {
        String[] p = line.split(",");
        if (p.length < 9) return null;
        return new CustomerUser(p[0],p[1],p[2],p[3],p[5],p[6],p[7],p[8]);
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress()     { return address;     }
    public String getBirthday()    { return birthday;    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address)         { this.address     = address;     }
    public void setBirthday(String birthday)       { this.birthday    = birthday;    }
}