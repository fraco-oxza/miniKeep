import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class Users {
  private static Users usersInstance = null;
  private ArrayList<User> usersList;

  // Singleton
  private Users() {
    try {
      FileInputStream file = new FileInputStream("./users.ser.bin");
      ObjectInputStream objIn = new ObjectInputStream(file);
      usersList = (ArrayList<User>) objIn.readObject();
      objIn.close();
      file.close();
    } catch (FileNotFoundException e) {
      usersList = new ArrayList<>();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static synchronized Users getInstance() {
    if (usersInstance == null) usersInstance = new Users();

    return usersInstance;
  }

  private void save() {
    try {
      FileOutputStream file = new FileOutputStream("./users.ser.bin");
      ObjectOutputStream objOut = new ObjectOutputStream(file);
      objOut.writeObject(usersList);
      objOut.close();
      file.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void addUser(User new_user) throws UserAlreadyExistsException {
    for (User user : usersList) {
      if (user.getRegistrationNumber() == new_user.getRegistrationNumber())
        throw new UserAlreadyExistsException();
      if (Objects.equals(user.getEmail(), new_user.getEmail())) {
        throw new UserAlreadyExistsException();
      }
    }

    usersList.add(new_user);
    save();
  }

  public User getUser(String email, String password) {
    for (User user : usersList) {
      if (user.getEmail().equals(email)) {
        if (user.isCorrectPassword(password)) {
          return user;
        }
      }
    }

    return null;
  }
}
