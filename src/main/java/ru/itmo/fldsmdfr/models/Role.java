package ru.itmo.fldsmdfr.models;

public enum Role {
    CITIZEN("Житель города"),
    DELIVERYMAN("Доставщик"),
    SCIENTIST("Учёный");

    private final String displayText;


    Role(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText() {
        return displayText;
    }


    // Другие роли

    public static Role fromString(String roleName) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + roleName);
    }


}

