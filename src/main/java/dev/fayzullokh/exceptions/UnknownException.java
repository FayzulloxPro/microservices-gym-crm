package dev.fayzullokh.exceptions;

public class UnknownException extends Exception {
    public UnknownException(String usernameIsAlreadyTaken) {
        super(usernameIsAlreadyTaken);
    }
}
