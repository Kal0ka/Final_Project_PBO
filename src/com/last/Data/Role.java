package com.last.Data;

public class Role {
    private int id;
    private String roleName;

    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role fromString(String roleName) {
        switch (roleName.toUpperCase()) {
            case "ADMIN":
                return new Role(3, "ADMIN");
            case "STAFF":
                return new Role(2, "STAFF");
            case "MAHASISWA":
                return new Role(1, "MAHASISWA");
            default:
                return new Role(0, "UNKNOWN");
        }
    }

    public static Role fromId(int id) {
        switch (id) {
            case 1: return new Role(1, "MAHASISWA");
            case 2: return new Role(2, "STAFF");
            case 3: return new Role(3, "ADMIN");
            default: return new Role(id, "UNKNOWN");
        }
    }

    @Override
    public String toString() {
        return roleName;
    }
}
