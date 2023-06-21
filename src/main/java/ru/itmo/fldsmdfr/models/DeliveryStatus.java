package ru.itmo.fldsmdfr.models;

public enum DeliveryStatus {

    NEW("Новый"),
    IN_PROGRESS("В работе"),
    DELIVERED("Доставлено");

    private String text;

    DeliveryStatus(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
