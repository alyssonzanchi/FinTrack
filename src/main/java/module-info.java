module br.edu.ifrs.fintrack {
    exports br.edu.ifrs.fintrack;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jbcrypt;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.desktop;

    opens br.edu.ifrs.fintrack.controller to javafx.fxml;
    opens br.edu.ifrs.fintrack.model to javafx.base;
}