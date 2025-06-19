package com.last.Data;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private String nimNip;

    public User(String id, String name, String email, String password, String roleId, String nimNip) {
        this.id = Integer.parseInt(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.fromId(Integer.parseInt(roleId));
        this.nimNip = nimNip;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public String getNimNip() { return nimNip; }

    public void setRole(Role role) {
        this.role = role;
    }

    public String toCSVString() {
        return id + "," + name + "," + email + "," + password + "," + role.getId() + "," + nimNip;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", role=" + role.getRoleName() + '}';
    }
}
