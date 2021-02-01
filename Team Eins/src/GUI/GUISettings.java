package GUI;

import Main.Main;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;

import static Main.Main.playMode;

public class GUISettings {
    private Popup settings;
    private static VBox schimpf = new VBox();
    private static VBox tipp = new VBox();
    private static VBox sortieren = new VBox();
    private static VBox steuerung = new VBox();
    private static HBox header = new HBox();
    private static Button schliessen = new Button("Schliessen");
    private static VBox transparenz = new VBox();
    ToggleGroup filter = new ToggleGroup();
    ToggleGroup hinweis = new ToggleGroup();
    ToggleGroup sort = new ToggleGroup();
    Slider slider;

    /**
     * Die Komponente der Einstellungen werden in Constructor erzeugt
     */
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
        schimpf.setAlignment(Pos.CENTER);
        HBox wortOptionen = new HBox(filterAn, filterAus);
        wortOptionen.setAlignment(Pos.TOP_CENTER);
        wortOptionen.setSpacing(20);

        Label hinweis = new Label("Tipps:");
        tipp.setAlignment(Pos.CENTER);
        HBox hinweisOptionen = new HBox(tippAn, tippAus);
        hinweisOptionen.setAlignment(Pos.TOP_CENTER);
        hinweisOptionen.setSpacing(20);

        Label kartesort = new Label("Karten sortieren:");
        sortieren.setAlignment(Pos.CENTER);
        HBox kartesortOpt = new HBox(sortAn, sortBeginn, sortAus);
        kartesortOpt.setAlignment(Pos.TOP_CENTER);
        kartesortOpt.setSpacing(5);

        Label chattranz = new Label("Chat Transparenz");
        transparenz.setAlignment(Pos.CENTER);
        slider = new Slider(0,1,0.8);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.25f);
        slider.setBlockIncrement(0.1f);



        header.getChildren().add(label);

        schimpf.getChildren().addAll(wort,wortOptionen );
        tipp.getChildren().addAll(hinweis, hinweisOptionen);
        sortieren.getChildren().addAll(kartesort, kartesortOpt );
        transparenz.getChildren().addAll(chattranz, slider);
        schliessen.setOnMouseClicked(c -> settings.hide());


        steuerung.setPrefWidth(220);
        steuerung.setPrefHeight(350);
        steuerung.setAlignment(Pos.CENTER);
        steuerung.getChildren().addAll(header, schimpf, tipp, sortieren,transparenz, schliessen);
        steuerung.setSpacing(5);
        settings.getContent().add(steuerung);
    }

    /**
     * @param owner Style in Settings
     */
    public void openSettings(Stage owner) {
        settings.setY(owner.getY()+100);
        settings.setX(owner.getX()+200);
        if(owner.isFullScreen()){
            settings.centerOnScreen();
        }
        settings.setAutoFix(true);
        header.getStyleClass().add("header");
        schimpf.getStyleClass().add("hbox");
        tipp.getStyleClass().add("hbox");
        sortieren.getStyleClass().add("hbox");
        transparenz.getStyleClass().add("hbox");
        steuerung.getStyleClass().add("vbox");
        steuerung.getStylesheets().add("GUI/einstellung.css");
        settings.show(owner);
    }

    public void hideSettings() {
        settings.hide();
    }

    /**
     * @param owner Popup Position wird aktualisiert mit
     *              der Bewegung der Spielfenster
     */
    public void reposition(Stage owner){
        settings.setX(owner.getX()+200);
        settings.setY(owner.getY()+100);
    }

    /** getter-Methode für settings
     * @return settings
     */
    public Popup getSettings() {
        return settings;
    }

    public double getSliderValue(){
       return slider.getValue();
    }
}
