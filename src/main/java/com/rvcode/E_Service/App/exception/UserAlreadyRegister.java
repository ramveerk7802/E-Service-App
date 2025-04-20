package com.rvcode.E_Service.App.exception;

public class UserAlreadyRegister extends RuntimeException {
    public UserAlreadyRegister(String message) {
        super(message);
    }
}
