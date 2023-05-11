public class Main {
    public static void main(String[] args) throws Exception {
        //Users users = Users.get_instance();

        //System.out.println(users.users_list.get(0));
        //var r = users.users_list.get(0).isCorrectPassword("Cb321993");
        //System.out.println(r);
        UserInput ui = new UserInput();
        var user = ui.getUser("Ingrese su usuario: ");
        System.out.println(user);
    }
}