package io.github.cristaling.swegg.backend.utils.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorMessages {

    USER_DOES_NOT_EXIST(100),

    REVIEW_ALREADY_EXISTS(101),
    JOB_DOES_NOT_EXIST(102),
    JOB_IS_NOT_DONE(103),
    REVIEW_STARS_MISSING(104),

    RECOMMENDATION_ALREADY_EXISTS(105),
    PLEASE_SPECIFY_EMAIL(106);

    int value;

    ErrorMessages(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}