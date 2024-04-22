package gui;

import java.util.Iterator;

import ads.AbstrHeap;
import ads.AbstrHeapException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        App.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("AppFXML.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Pamatky");

        primaryStage.show();
    }

    public static void main(String[] args) throws AbstrHeapException {
        launch(args);
    }

}
