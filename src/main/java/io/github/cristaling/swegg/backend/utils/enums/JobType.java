package io.github.cristaling.swegg.backend.utils.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum JobType {

    SINGLE("Single"),
    RECURRENT("Recurrent");

    String value;

    JobType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
