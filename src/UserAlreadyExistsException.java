/**
 * Exception thrown when attempting to add a user that already exists in the system.
 * This exception is used to indicate that a user with the same registration number or email already exists.
 * It is thrown to prevent duplicate users in the system.
 */
public class UserAlreadyExistsException extends Exception {
    /**
     * Constructs a UserAlreadyExistsException with an empty message.
     */
    UserAlreadyExistsException() {
        super("");
    }
}
