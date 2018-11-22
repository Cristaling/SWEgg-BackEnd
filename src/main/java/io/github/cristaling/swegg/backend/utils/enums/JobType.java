package io.github.cristaling.swegg.backend.utils.enums;

public enum JobType {

    SINGLE("Single"),
    RECURRENT("Recurrent");

    String name;

    JobType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
