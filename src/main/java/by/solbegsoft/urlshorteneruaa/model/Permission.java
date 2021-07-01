package by.solbegsoft.urlshorteneruaa.model;

public enum Permission {
    LINK_READ("link:read"), LINK_WRITE("link:write"), LINK_DELETE("link:read"), ROLE_UPDATE("role:update");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}