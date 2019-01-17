package io.github.cristaling.swegg.backend.utils.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorMessages {

    USER_DOES_NOT_EXIST("User does not exist"),

    REVIEW_ALREADY_EXISTS("Review already exists"),
    JOB_DOES_NOT_EXIST("Job doesn't exist"),
    JOB_IS_NOT_DONE("Job is not done yet"),
    REVIEW_STARS_MISSING("Cant review without giving stars"),

    RECOMMENDATION_ALREADY_EXISTS("There is already a recommendation of the user between you two."),
    PLEASE_SPECIFY_EMAIL("The recommandation cannot have empty fields");

    String value;

    ErrorMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}