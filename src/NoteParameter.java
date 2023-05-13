public enum NoteParameter {
  Header,
  CreationDate,
  UpdateDate,
  ViewDate,
  ;

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
