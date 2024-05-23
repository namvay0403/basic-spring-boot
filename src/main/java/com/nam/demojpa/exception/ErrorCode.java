package com.nam.demojpa.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED(100, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(102, "User not existed", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(101, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(104, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USERNAME_INVALID(102, "Username must be at least 6 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(103, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(401, "You do not have permission", HttpStatus.FORBIDDEN),;


    private int code;
    private String message;
    private HttpStatusCode statusCode;

     ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public HttpStatusCode getStatusCode(){
        return statusCode;
    }

}
