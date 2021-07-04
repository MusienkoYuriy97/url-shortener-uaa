package by.solbegsoft.urlshorteneruaa.model;

public enum Permission {
    ROLE_UPDATE("role:update");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}