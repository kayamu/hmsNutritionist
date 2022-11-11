package com.polarbears.capstone.hmsnutritionist.domain.enumeration;

/**
 * The STATUS enumeration.
 */
public enum STATUS {
    WAITING("waiting"),
    ACCEPTED("accepted"),
    CANCELLED("cancelled"),
    TRANSFERED("transfered");

    private final String value;

    STATUS(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
