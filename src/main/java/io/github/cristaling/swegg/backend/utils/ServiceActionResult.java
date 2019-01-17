package io.github.cristaling.swegg.backend.utils;

import io.github.cristaling.swegg.backend.utils.enums.ErrorMessages;

public class ServiceActionResult<T> {
    private ErrorMessages error;
    private T result;

    public ServiceActionResult() {
    }

    public void setError(ErrorMessages error) {
        this.error = error;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public boolean isSuccessful(){
        return this.error == null;
    }

    public String getError(){
        return this.error.getValue();
    }
}
