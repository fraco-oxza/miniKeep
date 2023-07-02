package dev.fraco.minikeep.logic;
import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a user with various fields and properties. important: Every time it tries to create a
 * user, it tries to add it to the Users class, if it cannot be done, it is because there is already
 * a user with those credentials
 */
public class User implements Serializable {
    private final long registrationNumber;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    // Is set to string instead of a more suitable abstract type since it's never used in logic,
    // it's just a plain field, a String
    private final String birthdate;

    /**
     * Constructs a new User object with the provided details and automatically adds it to the Users
     * class, ensuring immediate and automatic storage of the user in the system.
     *
     * @param registrationNumber The registration number of the user.
     * @param firstName          The first name of the user.
     * @param lastName           The last name of the user.
     * @param email              The email address of the user.
     * @param phoneNumber        The phone number of the user.
     * @param birthdate          The birthdate of the user.
     * @throws UserAlreadyExistsException If a user with the same registration number or email already
     *                                    exists.
     */
    public User(long registrationNumber, String firstName, String lastName, String email, String phoneNumber, String birthdate) throws UserAlreadyExistsException, IOException {
        this.registrationNumber = registrationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;

        // auto-adds to Users instance
        Users.getInstance().addUser(this);
    }

    /**
     * Returns the registration number of the user
     *
     * @return registration number of the user
     */
    public long getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Returns the email of the user
     *
     * @return The email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Generates the user password based on the user's name and registration number. The password is
     * created by taking the first letter of the first name in uppercase, the first letter of the last
     * name in lowercase, and the last six digits of the registration number in reverse order. These
     * components are concatenated to form the password. The password is recalculated each time it is
     * needed and is not stored for security reasons.
     *
     * @return The generated user password.
     */
    public String getPassword() {
        StringBuilder password = new StringBuilder();

        password.append(String.format("%06d", registrationNumber % 1_000_000));
        password.append(lastName.toLowerCase().charAt(0));
        password.append(firstName.toUpperCase().charAt(0));
        password.reverse();

        return password.toString();
    }

    /**
     * Checks if the provided password matches the user's actual password. This method serves as an
     * interface to verify the correctness of a password. The getPassword() method, which should
     * ideally be private for enhanced security, is made public to display the user's password only
     * during the account creation process. It returns true if the provided password matches the
     * actual password, and false otherwise.
     *
     * @param password The password to be checked.
     * @return True if the provided password is correct, false otherwise.
     */
    public boolean isCorrectPassword(String password) {
        return getPassword().equals(password);
    }
}