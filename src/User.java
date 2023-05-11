import java.io.Serializable;

public class User implements Serializable {
    private final long registration_number;
    private final String first_name;
    private final String last_name;
    private final String email;
    private final String phone_number;
    private final String birthdate;

    public User(long registration_number, String first_name, String last_name, String email, String phone_number, String birthdate) {
        this.registration_number = registration_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.birthdate = birthdate;
    }

    public long getRegistration_number() {
        return registration_number;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        StringBuilder password = new StringBuilder();

        password.append(String.format("%d", registration_number % 1_000_000));
        password.append(last_name.toLowerCase().charAt(0));
        password.append(first_name.toUpperCase().charAt(0));
        password.reverse();

        return password.toString();
    }

    public boolean isCorrectPassword(String password) {
        return getPassword().equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "registration_number=" + registration_number +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
