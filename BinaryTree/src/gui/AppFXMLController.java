package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import ads.AbstrTableException;
import enums.ETypKlice;
import enums.ETypProhlidky;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import pamatky.GPS;
import pamatky.Pamatky;
import pamatky.PamatkyException;
import pamatky.Zamek;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class AppFXMLController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnVybudovat;

    @FXML
    private Button btnZobrazitHaldu;

    @FXML
    private Button btnOdebratMax;

    @FXML
    private Button btnNahratUkazkove;

    @FXML
    private Button btnNahratUlozene;

    @FXML
    private Button btnNastavit;

    @FXML
    private Button btnNejblizsi;

    @FXML
    private Button btnPodleKlice;

    @FXML
    private Button btnPrebuduj;

    @FXML
    private Button btnProhlidka;

    @FXML
    private Button btnPromazat;

    @FXML
    private Button btnUlozit;

    @FXML
    private Button btnVlozit;

    @FXML
    private Button btnVygenerovat;

    @FXML
    private Button btnVymazat;

    @FXML
    private ToggleGroup groupKlic;

    @FXML
    private ToggleGroup groupProhlidka;

    @FXML
    private TextField inputDelka;

    @FXML
    private TextField inputNazev;

    @FXML
    private TextField inputSirka;

    @FXML
    private TextField inputPocet;

    @FXML
    private TextArea textArea;

    @FXML
    private ListView<Zamek> listView;

    private Pamatky pamatky;
    private ETypProhlidky aktualniProhlidka;
    private ETypKlice aktualniKlic;

    @FXML
    void nahratUkazkove(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vybrat ukazkovy soubor");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));

        File file = fileChooser.showOpenDialog(App.primaryStage);

        if (file != null) {
            try {
                int pocet = this.pamatky.importDatZTXT(file.getPath().trim());

                textArea.setText("Uspesne nahrano " + pocet + " zaznamu");
                naplnListView();
            } catch (FileNotFoundException | PamatkyException e) {
                errorDialog(e.getMessage());
            }
        }
    }

    @FXML
    void nahratUlozene(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vybrat zdrojovy soubor");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));

        File file = fileChooser.showOpenDialog(App.primaryStage);

        if (file != null) {
            try {
                int pocet = this.pamatky.importDat(file.getPath().trim());

                textArea.setText("Uspesne nahrano " + pocet + " zaznamu");
                naplnListView();
            } catch (FileNotFoundException | PamatkyException e) {
                errorDialog(e.getMessage());
            }
        }
    }

    @FXML
    void vygenerovat(ActionEvent event) {
        try {
            int pocet = Integer.parseInt(this.inputPocet.getText());

            this.pamatky.zrus();

            this.pamatky.generaceDat(pocet);

            textArea.setText("Uspesne vygenerovano " + pocet + " zaznamu");
            naplnListView();
        } catch (IllegalArgumentException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void najitNejblizsi(ActionEvent event) {
        try {
            if (this.aktualniKlic != ETypKlice.GPS) throw new AppException("Pro vyuziti teto funkce musi byt predvolen klic GPS");

            GPS lokace = new GPS((this.inputSirka.getText() + " " + this.inputDelka.getText()).trim());
            Zamek nalezeny = this.pamatky.najdiNejbliz(lokace.toString());

            if (nalezeny != null) {
                textArea.setText("Nejblizsi pamatka vzhledem k zadane lokaci:\n" + nalezeny + "\nVzdalenost: " + nalezeny.getLokace().vzdalenostOd(lokace) + " km");
            } else {
                throw new AppException("V okoli nebyla nalezena zadna pamatka");
            }
        } catch (PamatkyException | AbstrTableException | AppException | IllegalArgumentException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void najitPodleKlice(ActionEvent event) {
        try {
            if (this.aktualniKlic != this.pamatky.getAktualniKlic()) throw new AppException("Klic stromu se neshoduje s vybranym klicem v aplikaci");

            Zamek nalezeny = null;

            switch (this.aktualniKlic) {
                case GPS -> {
                    GPS lokace = new GPS((this.inputSirka.getText() + " " + this.inputDelka.getText()).trim());
                    nalezeny = this.pamatky.najdiZamek(lokace.toString());
                }
                case NAZEV -> nalezeny = this.pamatky.najdiZamek(this.inputNazev.getText().trim());
            }

            if (nalezeny != null) {
                textArea.setText("Nalezena pamatka:\n" + nalezeny);
            } else throw new AppException("Ve stromu se nenachazi zadna pamatka s podobnymi udaji");
        } catch (PamatkyException | AbstrTableException | IllegalArgumentException | AppException e) {
            errorDialog(e.getMessage());
        }
    }   

    @FXML
    void prebudovat(ActionEvent event) {
        try {
            if (this.aktualniKlic != this.pamatky.getAktualniKlic()) throw new AppException("Klic stromu se neshoduje s vybranym klicem v aplikaci");

            this.pamatky.prebuduj();
            naplnListView();
        } catch (PamatkyException | AppException e) { 
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void promazat(ActionEvent event) {
        this.pamatky.zrus();

        naplnListView();
    }

    @FXML
    void ulozit(ActionEvent event) {
        try {
            if (this.listView.getItems().size() == 0) throw new AppException("Neni co ukladat, strom je prazdny");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Vybrat cilovy soubor");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT Files", "*.txt"));

            File file = fileChooser.showSaveDialog(App.primaryStage);

            if (file != null) {
                try (
                    PrintWriter writer = new PrintWriter(file);
                ) {
                    writer.println(this.pamatky.exportDat(this.aktualniProhlidka));
                    textArea.setText("Uspesne ulozeno na misto:\n" + file.getPath());
                } catch (FileNotFoundException e) {
                    errorDialog(e.getMessage());
                }
            }
        } catch (AppException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void vlozit(ActionEvent event) {
        try {
            if (this.aktualniKlic != this.pamatky.getAktualniKlic()) throw new AppException("Klic stromu se neshoduje s vybranym klicem v aplikaci");

            String nazev = this.inputNazev.getText().trim();
            String souradnice = this.inputSirka.getText().trim() + " " + this.inputDelka.getText().trim();
            
            Zamek zamek = new Zamek(nazev, new GPS(souradnice));
            this.pamatky.vlozZamek(zamek);
        
            textArea.setText("Uspesne vlozeno");
            naplnListView();
        } catch (IllegalArgumentException | PamatkyException | AppException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void vymazat(ActionEvent event) {
        try {
            if (this.aktualniKlic != this.pamatky.getAktualniKlic()) throw new AppException("Klic stromu se neshoduje s vybranym klicem v aplikaci");

            Zamek odebirany = null;

            switch (this.aktualniKlic) {
                case GPS -> {
                    String souradnice = this.inputSirka.getText().trim() + " " + this.inputDelka.getText().trim();
                    
                    odebirany = this.pamatky.odeberZamek(souradnice);
                }
                case NAZEV -> {
                    String nazev = this.inputNazev.getText().trim();

                    odebirany = this.pamatky.odeberZamek(nazev);
                }
            }

            if (odebirany != null) {
                textArea.setText("Uspesne odebrana pamatka:\n" + odebirany);
                naplnListView();
            } else throw new AppException("Odebirana pamatka nebyla nalezena");
        } catch (PamatkyException | AbstrTableException | AppException e) {
            errorDialog(e.getMessage());
        }

    }

    @FXML
    void zobrazitProhlidku(ActionEvent event) {
        try {
            Iterator<Zamek> it = this.pamatky.iterator(this.aktualniProhlidka);
            ProhlidkaDialog.zobrazOkno(it);
        } catch (NullPointerException | PamatkyException e) {
            errorDialog(e.getMessage());
        }
    }

    private void naplnListView() {
        this.listView.getItems().clear();

        Iterator<Zamek> it;
        try {
            it = this.pamatky.iterator(this.aktualniProhlidka);

            while (it.hasNext()) {
                Zamek x = it.next();
    
                this.listView.getItems().add(x);
            }
        } catch (PamatkyException e) {
            //neni potreba upozornovat
        }
    }

    private void errorDialog(String contentText) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Chyba");
        alert.setHeaderText(null);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    @FXML
    void vybudovatHaldu(ActionEvent event) {
        try {
            GPS lokace = new GPS((this.inputSirka.getText() + " " + this.inputDelka.getText()).trim());

            this.pamatky.nastavAktualniPozici(lokace);
            this.pamatky.vybudujHaldu();
        } catch (PamatkyException | IllegalArgumentException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void zobrazitHaldu(ActionEvent event) {
        try {
            Iterator<Zamek> it = this.pamatky.iteratorHalda();
            ProhlidkaDialog.zobrazOkno(it);
        } catch (NullPointerException | PamatkyException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void odebratMax(ActionEvent event) {
        try {
            Zamek odebirany = this.pamatky.odeberMaxHalda();

            textArea.setText("Pamatka s nejvyssi prioritou:\n" + odebirany);
            zobrazitHaldu(event);
        } catch (PamatkyException e) {
            errorDialog(e.getMessage());
        }
    }

    @FXML
    void initialize() {
        this.pamatky = new Pamatky();
        this.listView.editableProperty().set(false);
        this.aktualniProhlidka = ETypProhlidky.HLUBOKA;
        this.aktualniKlic = ETypKlice.GPS;
        this.inputPocet.setText("20");

        this.groupProhlidka.selectedToggleProperty().addListener((ov, ol, ne) -> {
            if (((RadioButton) this.groupProhlidka.getSelectedToggle()).getText().equals("Hluboka")) {
                this.aktualniProhlidka = ETypProhlidky.HLUBOKA;
                naplnListView();
            } else {
                this.aktualniProhlidka = ETypProhlidky.SIROKA;
                naplnListView();
            }
        });

        this.groupKlic.selectedToggleProperty().addListener((ov, ol, ne) -> {
            try {
                if (((RadioButton) this.groupKlic.getSelectedToggle()).getText().equals("GPS")) {
                    this.aktualniKlic = ETypKlice.GPS;
                    this.pamatky.nastavKlic(ETypKlice.GPS);
                    this.pamatky.prebuduj();
                    naplnListView();
                } else {
                    this.aktualniKlic = ETypKlice.NAZEV;
                    this.pamatky.nastavKlic(ETypKlice.NAZEV);
                    this.pamatky.prebuduj();
                    naplnListView();
                }
            } catch (PamatkyException e) {
                // neni treba osetrovat
            }
        });
    }

}
