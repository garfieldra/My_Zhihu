package com.example.myzhihu.exception;

/**
 * Thrown when the authenticated user attempts to modify or delete a resource
 * that is owned by a different user.
 */
public class OwnershipMismatchException extends RuntimeException {

    public OwnershipMismatchException(String message) {
        super(message);
    }

    public OwnershipMismatchException() {
        super("无权操作该资源");
    }
}


