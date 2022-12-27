module com.example.typingtrainer {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires AnimateFX;

    opens com.example.typingtrainer to javafx.fxml;
    exports com.example.typingtrainer;
}