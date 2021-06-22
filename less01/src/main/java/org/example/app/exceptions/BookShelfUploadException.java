package org.example.app.exceptions;

public class BookShelfUploadException extends Exception {

    private final String message;

    public BookShelfUploadException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
