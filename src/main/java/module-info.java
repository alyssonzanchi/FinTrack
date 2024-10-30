module br.edu.ifrs.fintrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.sql;


    opens br.edu.ifrs.fintrack to javafx.fxml;
    exports br.edu.ifrs.fintrack;
}