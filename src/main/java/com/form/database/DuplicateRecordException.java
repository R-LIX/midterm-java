package com.form.database;

public class DuplicateRecordException extends RuntimeException {
    public  DuplicateRecordException(String message) {
        super("Record already exist: " + message);
    }
}
