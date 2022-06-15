package com.products.data;

import org.springframework.http.HttpStatus;

public class Error {
    private Integer code;
    private String message;

    public Error(){
        setCode(HttpStatus.BAD_REQUEST.value());
        setMessage("Validation Failed");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
