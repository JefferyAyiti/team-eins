package GUI;

import Main.Main;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import static Main.Main.playMode;

public class GUISettings {
    private Popup settings;
    private static HBox schimpf = new HBox();
    private static HBox tipp = new HBox();
    private static HBox sortieren = new HBox();
    private static VBox steuerung = new VBox();
    private static HBox header = new HBox();
    private static Button schliessen = new Button("Schliessen");
    ToggleGroup filter = new ToggleGroup();
    ToggleGroup hinweis = new ToggleGroup();
    ToggleGroup sort = new ToggleGroup();

    public GUISettings() {
        settings = new Popup();
        settings.setHideOnEscape(true);



        RadioButton filterAn = new RadioButton("An");
        filterAn.setToggleGroup(filter);
        filterAn.setOnMouseClicked(e -> {
            Main.schimpfFilter = true;
        });

        RadioButton filterAus = new RadioButton("Aus");
        filterAus.setToggleGroup(filter);
        filterAus.setOnMouseClicked(e -> {
            Main.schimpfFilter = false;
        });
        filterAus.setSelected(true);


        RadioButton tippAn = new RadioButton("An");
        tippAn.setOnMouseClicked(e -> {
            Main.tooltip = true;
            Main.spieltischGui.buildStage(Main.classPrimaryStage);
        });
        tippAn.setToggleGroup(hinweis);
        RadioButton tippAus = new RadioButton("Aus");
        tippAus.setToggleGroup(hinweis);
        tippAus.setSelected(true);
        tippAus.setOnMouseClicked(e -> {
            Main.tooltip = false;
            Main.spieltischGui.buildStage(Main.classPrimaryStage);
        });


        RadioButton sortAn = new RadioButton("Immer");
        sortAn.setToggleGroup(sort);
        sortAn.setOnMouseClicked(e -> {
            Main.autoSort = true;
            Main.spieltischGui.buildStage(Main.classPrimaryStage);
        });


        RadioButton sortBeginn = new RadioButton("zu Beginn");
        sortBeginn.setToggleGroup(sort);
        sortBeginn.setOnMouseClicked(e -> {
            Main.autoSort = false;
        });


        RadioButton sortAus = new RadioButton("Aus");
        sortAus.setToggleGroup(sort);
        sortAus.setOnMouseClicked(e -> {
            Main.autoSort = null;
            Main.spieltischGui.buildStage(Main.classPrimaryStage);
        });
        sortAus.setSelected(true);

        Label label = new Label("Einstellungen");
        Label wort = new Label("Schimpfwortfilter:");
        Label hinweis = new Label("Spielbare Karte anzeigen:");
        Label kartesort = new Label("Karte sortieren:");

        header.getChildren().add(label);

        schimpf.getChildren().addAll(wort, filterAn, filterAus);
        tipp.getChildren().addAll(hinweis, tippAn, tippAus);
        sortieren.getChildren().addAll(kartesort, sortAn, sortBeginn, sortAus);

        schliessen.setOnMouseClicked(c -> settings.hide());

        steuerung.setPrefWidth(400);
        steuerung.setPrefHeight(190);
        steuerung.getChildren().addAll(header);
            steuerung.getChildren().add(schimpf);
        steuerung.getChildren().addAll(tipp, sortieren, schliessen);
        steuerung.setSpacing(5);
        settings.getContent().add(steuerung);
    }

    public void openSettings(Stage owner) {
       settings.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_RIGHT);
        settings.setAutoFix(true);
        header.getStyleClass().add("header");
        schimpf.getStyleClass().add("hbox");
        tipp.getStyleClass().add("hbox");
        sortieren.getStyleClass().add("hbox");
        steuerung.getStyleClass().add("vbox");
        steuerung.getStylesheets().add("GUI/einstellung.css");
        if (playMode == 0)
            schimpf.setVisible(false);

        settings.show(owner);
    }

    public void hideSettings() {
        settings.hide();
    }
}
