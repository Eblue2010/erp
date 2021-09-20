package io.eliteblue.erp.core.constants;

public enum BloodType {
    A_PLUS("A+"),
    A_MINUS("A-"),
    B_PLUS("B+"),
    B_MINUS("B-"),
    O_PLUS("O+"),
    O_MINUS("O-"),
    AB_PLUS("AB-"),
    AB_MINUS("AB+");

    private String value;

    private BloodType(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
