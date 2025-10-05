module org.example.practica1medico {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.java;

    opens org.example.practica1medico to javafx.fxml;
    exports org.example.practica1medico;
    exports org.example.practica1medico.Controller;
    opens org.example.practica1medico.Controller to javafx.fxml;
    exports org.example.practica1medico.UTIL;
    opens org.example.practica1medico.UTIL to javafx.fxml;
}