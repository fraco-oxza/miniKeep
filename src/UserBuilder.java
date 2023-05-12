public class UserBuilder {
  private Long registration_number;
  private String first_name;
  private String last_name;
  private String email;
  private String phone_number;
  private String birthdate;

  public UserBuilder() {
    registration_number = null;
    first_name = null;
    last_name = null;
    email = null;
    phone_number = null;
    birthdate = null;
  }

  public UserBuilder registration_number(long n) {
    registration_number = n;
    return this;
  }

  public UserBuilder first_name(String fn) {
    first_name = fn;
    return this;
  }

  public UserBuilder last_name(String ln) {
    last_name = ln;
    return this;
  }

  public UserBuilder email(String e) {
    email = e;
    return this;
  }

  public UserBuilder phone_number(String pn) {
    phone_number = pn;
    return this;
  }

  public UserBuilder birthdate(String bd) {
    birthdate = bd;
    return this;
  }

  public User build() {
    // Check non-null fields
    return new User(registration_number, first_name, last_name, email, phone_number, birthdate);
  }
}
