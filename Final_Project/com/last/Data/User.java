package com.last.Data;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private String nimNip;

    public User(int id, String name, String email, String password, Role role, String nimNip) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nimNip = nimNip;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return  email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getNimNip() {
        return nimNip;
    }

    // Getters & Setters
}
