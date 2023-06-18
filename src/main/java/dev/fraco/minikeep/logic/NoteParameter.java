package dev.fraco.minikeep.logic;

/**
 * The NoteParameter enum represents different parameters for comparing Note objects.
 * It provides a mapping from integers to NoteParameter values and custom string representations.
 */
public enum NoteParameter {
    Header,
    CreationDate,
    UpdateDate,
    ViewDate,
    ;

    /**
     * Returns the NoteParameter corresponding to the specified integer value.
     *
     * @param number the integer value representing the NoteParameter
     * @return the NoteParameter corresponding to the integer value, or null if not found
     */
    public static NoteParameter from(int number) {
        switch (number) {
            case 0:
                return Header;
            case 1:
                return CreationDate;
            case 2:
                return UpdateDate;
            case 3:
                return ViewDate;
            default:
                return null;
        }
    }

    /**
     * Returns a string representation of the NoteParameter.
     *
     * @return a string representation of the NoteParameter
     */
    @Override
    public String toString() {
        switch (this) {
            case Header:
                return "Titulo";
            case CreationDate:
                return "Fecha de Creacion";
            case UpdateDate:
                return "Fecha de Actualizacion";
            case ViewDate:
                return "Fecha de Visita";
        }
        return null;
    }
}