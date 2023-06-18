module dev.fraco.minikeep {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.fraco.minikeep to javafx.fxml;
    exports dev.fraco.minikeep;
    exports dev.fraco.minikeep.controllers;
    exports dev.fraco.minikeep.logic;
    opens dev.fraco.minikeep.controllers to javafx.fxml;
}