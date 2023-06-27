package dev.fraco.minikeep.logic;

/**
 * The NoteParameter enum represents different parameters for comparing Note objects.
 * It provides a mapping from integers to NoteParameter values and custom string representations.
 */
public enum NoteParameter {
    Header,
    CreationDate,
    UpdateDate,
    ;

    /**
     * Returns a string representation of the NoteParameter.
     *
     * @return a string representation of the NoteParameter
     */
    @Override
    public String toString() {
        return switch (this) {
            case Header -> "Titulo";
            case CreationDate -> "Fecha de Creacion";
            case UpdateDate -> "Fecha de Actualizacion";
        };
    }
}
