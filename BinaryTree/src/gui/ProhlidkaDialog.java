package gui;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pamatky.Zamek;

import java.util.Iterator;
import javafx.geometry.Pos;

public class ProhlidkaDialog {
    
    public static void zobrazOkno(Iterator<Zamek> iterator) {
        if (iterator == null) throw new NullPointerException("Iterator neexistuje");

        Stage stage = new Stage();
        
        stage.initModality(Modality.APPLICATION_MODAL);

        ListView<String> listKandidati = new ListView<String>();
        while (iterator.hasNext()) {
            Zamek x = iterator.next();

            listKandidati.getItems().add(x.toString());
        }

        VBox layout = new VBox(listKandidati);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 400);

        stage.setTitle("Prohlidka");
        stage.setScene(scene);
        stage.show();
    }

}
