import java.io.*;
import java.util.ArrayList;

public class Users implements Serializable {
    private static Users users_instance = null;
    ArrayList<User> users_list;

    private Users() {
        try {
            FileInputStream file = new FileInputStream("./users.ser");
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

    public static Users get_instance() throws ClassNotFoundException {
        if (users_instance == null)
            users_instance = new Users();

        return users_instance;
    }

    private void save() {
        try {
            FileOutputStream file = new FileOutputStream("./users.ser");
            ObjectOutputStream objOut = new ObjectOutputStream(file);
            objOut.writeObject(users_list);
            objOut.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void add_user(User new_user) throws Exception {
        for (var user : users_list) {
            if (user.getEmail().equals(new_user.getEmail()))
                throw new Exception("There is already a user with this email");
        }

        users_list.add(new_user);
        save();
    }
}
