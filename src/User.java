import java.io.Serializable;

public class User implements Serializable {
  private final long registrationNumber;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String phoneNumber;
  private final String birthdate;

  public User(
      long registrationNumber,
      String firstName,
      String lastName,
      String email,
      String phoneNumber,
      String birthdate)
      throws UserAlreadyExistsException {
    this.registrationNumber = registrationNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.birthdate = birthdate;

    Users.getInstance().addUser(this);
  }

  public long getRegistrationNumber() {
    return registrationNumber;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    StringBuilder password = new StringBuilder();

    password.append(String.format("%d", registrationNumber % 1_000_000));
    password.append(lastName.toLowerCase().charAt(0));
    password.append(firstName.toUpperCase().charAt(0));
    password.reverse();

    return password.toString();
  }

  public boolean isCorrectPassword(String password) {
    return getPassword().equals(password);
  }

  @Override
  public String toString() {
    return "User{"
        + "registration_number="
        + registrationNumber
        + ", first_name='"
        + firstName
        + '\''
        + ", last_name='"
        + lastName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", phone_number='"
        + phoneNumber
        + '\''
        + ", birthdate="
        + birthdate
        + '}';
  }
}
