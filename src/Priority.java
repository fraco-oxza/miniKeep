public enum Priority {
    Low,
    Normal,
    High,
    Critical,
    ;

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
