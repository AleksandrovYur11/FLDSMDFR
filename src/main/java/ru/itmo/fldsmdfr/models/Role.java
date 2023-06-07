package ru.itmo.fldsmdfr.models;

public enum Role {
    CITIZEN("Житель города"),
    DELIVERYMAN("Доставщик"),
    SCIENTIST("Учёный");

    private final String displayText;


    Role(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayText(){
        return displayText;
    }
}

