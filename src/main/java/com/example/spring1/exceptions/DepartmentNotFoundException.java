package com.example.spring1.exceptions;

public class DepartmentNotFoundException extends Exception {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
