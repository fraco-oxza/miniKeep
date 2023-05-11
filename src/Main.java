public class Main {
    public static void main(String[] args) throws Exception {
        Note n = (Notes.getInstance().getUserNotes(Users.get_instance().getUser("fcarvajal22@alumnos.utalca.cl", "Fc100704"))).get(0);
        System.out.println(n);
        //MenuInteraction menuInteraction = new MenuInteraction();

        //menuInteraction.startLoop();
    }
}