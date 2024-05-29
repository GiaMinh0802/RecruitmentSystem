package com.fpt.recruitmentsystem.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseMessage {
    private String message;

    @JsonCreator
    public ResponseMessage(@JsonProperty("message") String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

