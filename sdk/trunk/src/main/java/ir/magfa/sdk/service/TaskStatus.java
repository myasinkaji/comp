package ir.magfa.sdk.service;

/**
 * @author Mohammad Yasin Kaji
 */
public enum TaskStatus {

    RESERVED("Reserved"),

    COMPLETED("Completed");

    private String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
