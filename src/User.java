import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class User implements Serializable {
    private long registration_number;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String birthdate;

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

    public User(long registration_number, String first_name, String last_name, String email, String phone_number, String birthdate) {
        this.registration_number = registration_number;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    private String getPassword() {
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
}
