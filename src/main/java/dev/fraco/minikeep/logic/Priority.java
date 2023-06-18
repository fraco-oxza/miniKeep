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
     * Converts an integer value to a Priority object.
     *
     * @param cardinal the integer value representing the priority level
     * @return the corresponding Priority object
     */
    public static Priority from(int cardinal) {
        Priority priority = null;

        switch (cardinal) {
            case 0:
                priority = Priority.Low;
                break;
            case 1:
                priority = Priority.Normal;
                break;
            case 2:
                priority = Priority.High;
                break;
            case 3:
                priority = Priority.Critical;
                break;
        }

        return priority;
    }

    /**
     * Returns the localized string representation of the priority.
     *
     * @return the localized string representation of the priority
     */
    @Override
    public String toString() {
        switch (this) {
            case Low:
                return "Baja";
            case Normal:
                return "Normal";
            case High:
                return "Alta";
            case Critical:
                return "Critica";
        }
        return null;
    }
}