package GUI;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

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

    public GUISettings(){
        settings = new Popup();
        settings.setHideOnEscape(true);



        RadioButton filterAn = new RadioButton("An");
        filterAn.setToggleGroup(filter);
        filterAn.setSelected(true);
        RadioButton filterAus = new RadioButton("Aus");
        filterAus.setToggleGroup(filter);


        RadioButton tippAn = new RadioButton("An");
        tippAn.setToggleGroup(hinweis);
        tippAn.setSelected(true);
        RadioButton tippAus = new RadioButton("Aus");
        tippAus.setToggleGroup(hinweis);


        RadioButton sortAn = new RadioButton("Immer");
        sortAn.setToggleGroup(sort);
        RadioButton sortBeginn = new RadioButton("zu Beginn");
        sortBeginn.setToggleGroup(sort);
        RadioButton sortAus = new RadioButton("Aus");
        sortAus.setToggleGroup(sort);
        sortAus.setSelected(true);

        Label label = new Label("Einstellungen");
        Label wort = new Label("Schimpfwortfilter:");
        Label hinweis = new Label("Tipps:");
        Label kartesort = new Label("Karte sortieren:");

        header.getChildren().add(label);
        schimpf.getChildren().addAll(wort, filterAn, filterAus);
        tipp.getChildren().addAll(hinweis, tippAn, tippAus);
        sortieren.getChildren().addAll(kartesort, sortAn, sortBeginn, sortAus);

        schliessen.setOnMouseClicked(c->{settings.hide();});

        steuerung.setPrefWidth(400);
        steuerung.setPrefHeight(190);
        steuerung.getChildren().addAll(header, schimpf,tipp, sortieren, schliessen);
        steuerung.setSpacing(5);
        settings.getContent().add(steuerung);
    }

    public void openSettings(Stage owner){
        settings.setY(owner.getScene().getWindow().getX() + 5);
        settings.setX(owner.getScene().getWindow().getY() + 35);
        header.getStyleClass().add("header");
        schimpf.getStyleClass().add("hbox");
        tipp.getStyleClass().add("hbox");
        sortieren.getStyleClass().add("hbox");
        steuerung.getStyleClass().add("vbox");
        steuerung.getStylesheets().add("GUI/einstellung.css");


        settings.show(owner);
    }

    public void hideSettings(){
        settings.hide();
    }


}
