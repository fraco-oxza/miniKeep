public class MenuInteraction {
    private User user;
    private final UserInput userInput = UserInput.getInstance();
    private boolean userWantsToExit;

    public MenuInteraction() {
        user = null;
        userWantsToExit = false;
    }

    public void startLoop() {
        do {
            if (user == null) {
                sessionMenu();
            } else {
                generalMenu();
            }
        } while (!userWantsToExit);
    }

    public void signIn() {
        String email = userInput.getText("Correo : ", UserInput.emailPattern);
        String password = userInput.getText("Clave  : ");

        User possibleUser = Users.get_instance().getUser(email, password);

        if (possibleUser == null) {
            System.out.println("!! Email y/o contraseña incorrecto");
        } else {
            System.out.println("## Inicio de sesion exitoso");
            user = possibleUser;
        }
    }

    public void signUp() {
        try {
            User user = Users.get_instance().addNewUserFromStdin();
            System.out.println("## Usuario creado con exito");
            System.out.println("## Su clave es : " + user.getPassword());
        } catch (UserAlreadyExistsException e) {
            System.out.println("!! Ya existe un usuario con su numero de matricula o correo");
        }
    }

    public void sessionMenu() {
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Crear cuenta");
        System.out.println("0. Salir");
        int option = userInput.getInt("Opción: ", 0, 2);

        switch (option) {
            case 1:
                signIn();
                break;
            case 2:
                signUp();
                break;
            case 0:
                userWantsToExit = true;
                break;
        }
    }

    public void generalMenu() {
        System.out.println("1. Ver Notas");
        System.out.println("2. Crear Nota");
        System.out.println("3. Editar Nota");
        System.out.println("4. Eliminar Nota");
        System.out.println("5. Ver papelera");
        System.out.println("6. Cerrar sesión");
        System.out.println("0. Salir");

        int option = userInput.getInt("Opción: ", 0, 6);

        switch (option) {
            case 1:
                notesMenu();
                break;
            case 2:
                break;
            case 0:
                userWantsToExit = true;
                break;

        }
    }

    public void notesMenu() {
        System.out.println("Mostrar las notas ordenadas por: ");
        System.out.println("1. Nombre");
        System.out.println("2. Fecha de creación");
        System.out.println("3. Prioridad");
        System.out.println("4. Agrupados por color");
        System.out.println("5. Agrupados por temas");
        int option = userInput.getInt("Opción: ", 1, 5);

        switch (option) {
            case 1:
                break;
            case 2:
                break;
            case 0:
                userWantsToExit = true;
                break;

        }
    }
}
