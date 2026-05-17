package com.freshgrocer.model;

public class User {

    private String userId;
    private String username;
    private String password;
    private String email;
    private String role;
    private String status;

    public User() {}

    public User(String userId, String username, String password,
                String email, String role, String status) {
        this.userId   = userId;
        this.username = username;
        this.password = password;
        this.email    = email;
        this.role     = role;
        this.status   = status;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) &&
                this.password.equals(password);
    }

    public String displayInfo() {
        return "User: " + username + " | Email: " + email;
    }

    public String toFileString() {
        return userId + "," + username + "," + password + ","
                + email + "," + role + "," + status;
    }

    public String getUserId()   { return userId;   }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()    { return email;    }
    public String getRole()     { return role;     }
    public String getStatus()   { return status;   }

    public void setUserId(String userId)     { this.userId   = userId;   }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email)       { this.email    = email;    }
    public void setRole(String role)         { this.role     = role;     }
    public void setStatus(String status)     { this.status   = status;   }
}