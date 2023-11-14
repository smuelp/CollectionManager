module start.projetopadrao {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens start.projetopadrao to javafx.fxml;
    exports start.projetopadrao;
    
    opens controller to javafx.fxml;
    exports controller;
    
    opens model to javafx.fxml;
    exports model;
}
