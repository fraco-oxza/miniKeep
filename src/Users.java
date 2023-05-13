import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class Users {
  private static Users users_instance = null;
  ArrayList<User> users_list;

  // Singleton
  private Users() {
    try {
      FileInputStream file = new FileInputStream("./users.ser.bin");
      ObjectInputStream objIn = new ObjectInputStream(file);
      users_list = (ArrayList<User>) objIn.readObject();
      objIn.close();
      file.close();
    } catch (FileNotFoundException e) {
      users_list = new ArrayList<>();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static synchronized Users getInstance() {
    if (users_instance == null) users_instance = new Users();

    return users_instance;
  }

  private void save() {
    try {
      FileOutputStream file = new FileOutputStream("./users.ser.bin");
      ObjectOutputStream objOut = new ObjectOutputStream(file);
      objOut.writeObject(users_list);
      objOut.close();
      file.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void addUser(User new_user) throws UserAlreadyExistsException {
    for (User user : users_list) {
      if (user.getRegistrationNumber() == new_user.getRegistrationNumber())
        throw new UserAlreadyExistsException();
      if (Objects.equals(user.getEmail(), new_user.getEmail())) {
        throw new UserAlreadyExistsException();
      }
    }

    users_list.add(new_user);
    save();
  }

  public User getUser(String email, String password) {
    for (User user : users_list) {
      if (user.getEmail().equals(email)) {
        if (user.isCorrectPassword(password)) {
          return user;
        }
      }
    }

    return null;
  }
}
