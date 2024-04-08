package dev.fayzullokh.exceptions;

public class DuplicateUsernameException extends Exception {
    public DuplicateUsernameException(String usernameIsAlreadyTaken) {
        super(usernameIsAlreadyTaken);
    }
}
