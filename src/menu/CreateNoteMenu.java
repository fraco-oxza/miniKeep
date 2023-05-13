package menu;

import context.AppContext;
import ioUtils.OutputFormatter;
import note.Note;
import note.NoteComparator;
import note.NoteParameter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class CreateNoteMenu extends Menu {
    public CreateNoteMenu(AppContext context) {
        super(context);
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public MenuResult exec() throws IOException {
        System.out.println("En que orden desea mostrar las notas: ");
        OutputFormatter.showMenu("1. Nombre", "2. Fecha", "3. Prioridad", "4. Agrupados por color", "5. Agrupados por temas");
        int option = userInput.getInt("Opción", 1, 5);

        List<Note> userNotes = notes.getUserNotes(user);

        Collections.sort(userNotes, new NoteComparator(NoteParameter.UpdateDate));
        switch (option) {
            case 1:
                Collections.sort(userNotes, new NoteComparator(NoteParameter.Header));
                formatter.showNotes(userNotes);
                break;
            case 2:
                OutputFormatter.showMenu();
                OutputFormatter.showMenu("1. Creacion", "2. Ultima Actualizacion", "3. Ultima Visita");
                NoteParameter parameter = NoteParameter.from(userInput.getInt("Ordenar por", 1, 3));
                Collections.sort(userNotes, new NoteComparator(parameter));
                formatter.showNotes(userNotes);
                break;
            case 3:
                notesByPriorityMenu(userNotes);
                break;
            case 4:
                notesByColorMenu(userNotes);
                break;
            case 5:
                notesByTagMenu(userNotes);
                break;
            case 0:
                userWantsToExit = true;
                break;
        }
    }
}
