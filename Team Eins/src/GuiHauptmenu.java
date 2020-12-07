import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GuiHauptmenu {
    Slider slider;
    ComboBox botselect;
    ComboBox playeranzselect;
    TextField namefield;
    TextField ip;
    TextField port;


    /**
     * @param PrimaryStage Erzeugt und zeigt das Hauptmenü zu Beginn des Spiels an
     */
    void showSettingsMenu(Stage PrimaryStage) {
        GridPane center = new GridPane();
        center.setVgap(10);

        //Spielername
        namefield = new TextField(Main.myName);
        if(!Main.joined)
            center.addRow(0, new Label("Spielername: "), namefield);

        ip = new TextField("localhost");

        port = new TextField("50099");
        if (Main.playMode == 2 && !Main.joined) {
            //IP:Port
            center.addRow(1, new Label("Server-IP: "), ip);
            center.addRow(2, new Label("Server-Port: "), port);

        }


        //Spieleranzahl
        ObservableList<Integer> ploptions =
                FXCollections.observableArrayList(
                        2,
                        3,
                        4,
                        5,
                        6
                );
        playeranzselect = new ComboBox(ploptions);
        playeranzselect.getSelectionModel().select(4);
        if (Main.playMode < 2)
            center.addRow(1, new Label("Spieleranzahl: "), playeranzselect);


        //Boteinstellungen
        //Schwierigkeit
        ObservableList<String> botoptions =
                FXCollections.observableArrayList(
                        "Zufällig",
                        "Leicht",
                        "Mittel",
                        "Schwer"
                );
        botselect = new ComboBox(botoptions);
        botselect.getSelectionModel().select(0);
        if (Main.playMode < 2)
            center.addRow(2, new Label("Bot-Schwierigkeit: "), botselect);


        center.setHgap(60 * Main.zoomfactor);
        center.setId("MMcenter");
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('images/oberflaeche.jpg')");
        center.setMinHeight(250 * Main.zoomfactor);
        center.setMinWidth(200 * Main.zoomfactor);

        //Geschwindigkeit
        slider = new Slider();
        slider.setMin(500);
        slider.setMax(5000);
        slider.setValue(Main.botPlayTime == 0 ?
                (slider.getMax() - slider.getMin()) / 2 + slider.getMin() :
                Main.botPlayTime);
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        slider.setMinorTickCount(1000);
        slider.setMajorTickUnit(1000);
        slider.setBlockIncrement(10);
        slider.setPrefSize(150, 5);
        if (Main.playMode < 2)
            center.addRow(3, new Label("Bot-Bedenkzeit: "), slider);


        //Darstellung
        Label titel = new Label("Hauptmenü");
        titel.setTextFill(Color.WHITE);
        titel.setFont(new Font("Script MT Bold", 36 * Main.zoomfactor));

        VBox top = new VBox(titel);
        top.setMinHeight(Main.sceneHeight / 8);
        top.setAlignment(Pos.CENTER);

        //Singlepalyer / Host / Join
        HBox multiplayer = new HBox();
        multiplayer.setSpacing(50);
        multiplayer.setTranslateY(15);
        multiplayer.setAlignment(Pos.BOTTOM_CENTER);
        Label single = new Label("Einzelspieler");
        single.setOnMouseClicked(e -> {
            Main.playMode = 0;
            Main.joined = false;
            showSettingsMenu(PrimaryStage);
        });

        Pane host = new Pane(new Label("Server erstellen"));
        host.setOnMouseClicked(mouseEvent -> {
            Main.playMode = 1;
            Main.joined = false;
            showSettingsMenu(PrimaryStage);
        });

        Label join = new Label("Server joinen");
        join.setOnMouseClicked(e -> {
            Main.playMode = 2;
            Main.joined = false;
            showSettingsMenu(PrimaryStage);
        });

        multiplayer.getChildren().addAll(single, host, join);
        multiplayer.setId("MMtop");
        BorderPane root = new BorderPane();

        top.getChildren().add(multiplayer);
        HBox bottom = new HBox();
        bottom.setMinHeight(Main.sceneHeight / 8);
        bottom.setAlignment(Pos.CENTER);
        root.setBottom(bottom);


        Button start = new Button();
        switch (Main.playMode) {
            case 0:
                start.setText("Spiel starten");
                start.setOnAction(e -> setSettings("start"));
                break;
            case 1:
                if (!Main.joined) {
                    start.setText("Raum erstellen");
                    start.setOnAction(e -> setSettings("create"));
                } else {
                    start.setText("Spiel starten");
                    start.setOnAction(e -> setSettings("startserver"));
                }
                break;
            case 2:
                if (Main.joined) {
                    start.setText("Raum verlassen");
                    start.setOnAction(e -> setSettings("leave"));
                } else {
                    start.setText("Spiel beitreten");
                    start.setOnAction(e -> setSettings("join"));
                }
                break;
        }
        start.setTranslateY(-10);
        bottom.setSpacing(10);
        bottom.getChildren().add(start);

        if(Main.playMode == 1 && Main.joined) {
            Button close = new Button("Raum schließen");
            close.setOnAction(e -> setSettings("close"));
            close.setTranslateY(-10);
            bottom.getChildren().add(close);
        }


        if (Main.playMode > 0 && Main.joined) {
            VBox lobby = new VBox(new Label("Lobby:"));
            lobby.setStyle("-fx-background-color:rgba(255,255,255,0.5);");
            lobby.setSpacing(10);
            lobby.setPrefHeight(150);
            lobby.setMinWidth(100);
            center.add(lobby, 2, 0, 1, 3);
        }
        center.setAlignment(Pos.TOP_LEFT);
        center.setMaxHeight(center.getHeight());

        root.setTop(top);
        root.setCenter(center);


        BackgroundImage myBI = new BackgroundImage(Main.table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        Scene menu = new Scene(root, Main.sceneWidth, Main.sceneHeight);
        menu.getStylesheets().add("MainMenu.css");

        PrimaryStage.setScene(menu);
        PrimaryStage.show();
    }


    /** Führt die Button-Actions im Hauptmenü aus
     * @param action welcher Button wurde geklickt
     */
    void setSettings(String action) {
        if (action == "start") { //Single-Player-Spiel
            Main.botPlayTime = (long) slider.getValue();
            Main.botlevel = botselect.getSelectionModel().getSelectedIndex();
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
            Main.anzSpieler = (int) playeranzselect.getValue();
            Main.initGame();
            Main.sceneWidth = 600;
            Main.sceneHeight = 400;
            Main.runTimers(Main.classPrimaryStage);
            Main.spieltischGui.buildStage(Main.classPrimaryStage);
        } else if (action == "close") { //Host
            Main.joined = false;
            //TODO Server beenden
            showSettingsMenu(Main.classPrimaryStage);

        } else if (action == "create") { //Host
            Main.joined = true;
            Main.botPlayTime = (long) slider.getValue();
            Main.botlevel = botselect.getSelectionModel().getSelectedIndex();
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
            Main.anzSpieler = (int) playeranzselect.getValue();
            //TODO Server erstellen/starten
            showSettingsMenu(Main.classPrimaryStage);

        } else if (action == "startserver") {
            //TODO andere menschliche Spieler übergeben,
            Main.sceneWidth = 600;
            Main.sceneHeight = 400;
            Main.initGame();
            Main.runTimers(Main.classPrimaryStage);
            Main.spieltischGui.buildStage(Main.classPrimaryStage);

        } else if (action == "join") {
            Main.joined = true;
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
            System.out.println(ip.getText());
            System.out.println(port.getText());
            GuiZoomLoader.getZoomedImages();
            showSettingsMenu(Main.classPrimaryStage);

        } else if (action == "leave") {
            Main.joined = false;
            showSettingsMenu(Main.classPrimaryStage);
        }
    }
}
