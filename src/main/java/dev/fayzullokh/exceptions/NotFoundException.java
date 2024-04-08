package dev.fayzullokh.exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(String usernameIsAlreadyTaken) {
        super(usernameIsAlreadyTaken);
    }
}
