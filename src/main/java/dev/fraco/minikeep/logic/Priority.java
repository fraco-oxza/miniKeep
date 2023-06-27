package dev.fraco.minikeep.logic;

/**
 * Enumeration representing the priority levels of a note in the system. The priority levels are:
 * Low, Normal, High, and Critical. The Priority enum provides methods to convert from an integer
 * value to a Priority object, as well as a toString() method to return the corresponding localized
 * string representation of the priority.
 */
public enum Priority {
    Low,
    Normal,
    High,
    Critical,
    ;

    /**
     * Returns the localized string representation of the priority.
     *
     * @return the localized string representation of the priority
     */
    @Override
    public String toString() {
        return switch (this) {
            case Low -> "Baja";
            case Normal -> "Normal";
            case High -> "Alta";
            case Critical -> "Critica";
        };
    }
}