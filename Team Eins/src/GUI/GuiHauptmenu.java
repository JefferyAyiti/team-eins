package GUI;

import RMI.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Main.*;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import static Main.Main.*;

public class GuiHauptmenu {
    Slider slider;
    ComboBox botselect;
    ComboBox playeranzselect;
    ComboBox spielart;
    TextField spielartLimit;
    TextField namefield;
    TextField ip;
    TextField port;
    public Timer update;
    Label status = new Label();
    RunServer runServer;
    RunClient runClient;
    Thread getTisch;
    boolean assigned = false;
    String IP = "localhost";
    String Port = "8001";
    boolean hasLeftServer = false;

    boolean shutdown = false;
    boolean settingsOpen = false;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE =
            "-fx-background-insets: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-cursor:hand;" +
                    "-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);";


    /**
     * @param PrimaryStage Erzeugt und zeigt das Hauptmenü zu Beginn des Spiels an
     */
    public void showSettingsMenu(Stage PrimaryStage){
        Double fontsize= 12*zoomfactor;
        status.setId("status");
        status.setFont(new Font(fontsize));

        PrimaryStage.xProperty().addListener((obs, oldVal, newVal) -> reposition(PrimaryStage));
        PrimaryStage.yProperty().addListener((obs, oldVal, newVal) -> reposition(PrimaryStage));



        if(classPrimaryStage.getScene() != null) {
            sceneWidth = classPrimaryStage.getScene().getWidth();
            sceneHeight = classPrimaryStage.getScene().getHeight();
        }
        inMenu = true;
        try {
            if(server != null){
            }

            if(server != null && server.getGameStart(uniqueID) && playMode == 2) {
                try {
                    if(!assigned){
                        ich = server.assignId(uniqueID);
                        if(ich == -1) {//join fehlerhaft
                            server.leaveServer(uniqueID);
                            return;
                        }
                        assigned = true;
                    }

                } catch (RemoteException e) {
                }
                inMenu = false;

                tisch = server.updateTisch();
                anzSpieler = server.getAnzahlSpieler();
                update.cancel();
                Platform.runLater(() -> spieltischGui.buildStage(classPrimaryStage));
                Main.runTimers(Main.classPrimaryStage);
                getTisch = new Thread(new ClientThread(Main.server, runClient.client));
                getTisch.start();
                return;

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        GridPane center = new GridPane();
        center.setVgap(10);
        center.setGridLinesVisible(false);


        ImageView settings = new ImageView(new Image("GUI/images/gear_icon.png"));

        settings.setFitWidth(20 * zoomfactor);
        settings.setPreserveRatio(true);

        settings.setOnMouseClicked(s -> {
            if (!settingsOpen) {
                einstellung.openSettings(classPrimaryStage);
                settingsOpen = true;
            } else { //chat bereits offen
                einstellung.hideSettings();
                settingsOpen = false;
            }

        });
        settings.setOnMouseEntered(e -> settings.setStyle(HOVERED_BUTTON_STYLE));
        settings.setOnMouseExited(e -> settings.setStyle(IDLE_BUTTON_STYLE));
        einstellung.reposition(classPrimaryStage);



        //Spielername
        namefield = new TextField(Main.myName);
        namefield.addEventFilter(KeyEvent.KEY_TYPED, maxLength(13));
        Label l = new Label("Spielername: ");
        l.setFont(new Font(fontsize));

        if(!Main.joined) {

            center.addRow(0,l, namefield);
        }
        ip = new TextField(IP);
        ip.setMinWidth(ip.getWidth()*zoomfactor);
        ip.setMinHeight(ip.getHeight()*zoomfactor);

        port = new TextField(Port);
        port.setMinWidth((port.getWidth()*zoomfactor));
        port.setMinHeight(port.getHeight()*zoomfactor);

        Label sip = new Label("Server-IP: ");
        sip.setFont(new Font(fontsize));
        Label sp = new Label("Server-Port: ");
        sp.setFont(new Font(fontsize));

        if (Main.playMode == 2 && !Main.joined) {
            //IP:Port

            center.addRow(1,sip , ip);
            center.addRow(2, sp, port);
            center.addRow(5, status);

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
        playeranzselect.setMinWidth(playeranzselect.getWidth()*zoomfactor);
        playeranzselect.setMinHeight(playeranzselect.getHeight()*zoomfactor);
        playeranzselect.getSelectionModel().select(4);
        Label sa = new Label("Spieleranzahl: ");
        sa.setFont(new Font(fontsize));

        if (Main.playMode < 2) {
            center.addRow(1, sa, playeranzselect);
        }
            if(Main.playMode == 1){
                center.addRow(1, sip, ip);
            }


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
        botselect.setMinWidth(botselect.getWidth()*zoomfactor);
        botselect.setMinHeight(botselect.getHeight()*zoomfactor);
        botselect.getSelectionModel().select(0);
        Label bs =  new Label("Bot-Schwierigkeit: ");
        bs.setFont(new Font(fontsize));
        if (Main.playMode < 2)
            center.addRow(2,bs, botselect);
            if(Main.playMode == 1){
                center.addRow(2, sp, port);
                center.addRow(5, status);
            }

        if(Main.playMode==1 ){
            center.setHgap(15*zoomfactor);
            namefield.setMaxWidth(150 *zoomfactor);
            ip.setMaxWidth(80*zoomfactor);
            port.setMaxWidth(40*zoomfactor);
        }else {
            center.setHgap(60 * Main.zoomfactor);
        }
        center.setId("MMcenter");
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('/GUI/images/oberflaeche.jpg');-fx-background-size: cover");
        center.setMinHeight(250 * Main.zoomfactor);
        center.setMinWidth(600 * Main.zoomfactor);

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
        slider.setPrefSize(150*zoomfactor, 5);

        if (Main.playMode < 2) {
            Label b = new Label("Bot-Bedenkzeit: ");
            b.setFont(new Font(fontsize));
            center.addRow(3, b, slider);
        }
        //Spielmodus

        spielart = new ComboBox(FXCollections.observableArrayList(
                "Normal (bis 40 Punkte)",
                "Runden-Limit",
                "Unendlich"
        ));


        spielart.getSelectionModel().select(spielArt);
        spielart.setMinWidth(spielart.getWidth()*zoomfactor);
        spielart.setMinHeight(spielart.getHeight()*zoomfactor);


        spielartLimit = new TextField(Integer.toString(spielArtLimit));
        spielartLimit.setMaxWidth(50*zoomfactor);
        spielartLimit.setMinHeight(spielartLimit.getHeight()*zoomfactor);

        Label sart = new Label("Spielart: ");
        sart.setFont(new Font(fontsize));

        if (Main.playMode < 2)
            center.addRow(4, sart, spielart);

        Label r = new Label("Rundenanzahl: ");
        r.setFont(new Font (fontsize));
        if(spielart.getSelectionModel().getSelectedIndex() == 1){

            center.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 4);
            center.addRow(4, sart, spielart);
            center.add( r,2,4);
            center.add( spielartLimit,3,4);

        }

        spielart.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
                    if(spielart.getSelectionModel().getSelectedIndex() == 1) {
                        if (Main.playMode < 2) {
                            center.add( r,2,4);
                            center.add( spielartLimit,3,4);
                        }

                    } else {
                        center.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 2 && GridPane.getRowIndex(node) == 4);
                        center.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == 3 && GridPane.getRowIndex(node) == 4);
                    }
                }
        );
        //Tutorial
        if(playMode==0) {
            HBox t = new HBox();
            ToggleGroup tutor = new ToggleGroup();
            Label tut = new Label("Tutorial");
            tut.setFont(new Font(fontsize));
            RadioButton tutAn = new RadioButton("An");
            tutAn.setToggleGroup(tutor);
            tutAn.setOnMouseClicked(e -> {
                tutorialAn = true;
            });
            RadioButton tutAus = new RadioButton("Aus");
            tutAus.setSelected(true);
            tutAus.setToggleGroup(tutor);
            tutAus.setOnMouseClicked(e -> {
                tutorialAn = false;
            });
            t.getChildren().addAll(tutAn,tutAus);
            t.setSpacing(20*zoomfactor);
            center.addRow(5, tut, t);
        }

        //Darstellung
        Label titel = new Label("Hauptmenü");
        titel.setTextFill(Color.WHITE);
        titel.setFont(new Font("Script MT Bold", 36 * Main.zoomfactor));

        BorderPane header = new BorderPane();
        header.setRight(settings);
        header.setAlignment(settings, Pos.TOP_LEFT);
        VBox top = new VBox(titel);
        top.setAlignment(Pos.CENTER);

        //Singlepalyer / Host / Join
        HBox multiplayer = new HBox();
        multiplayer.setMinWidth(580*zoomfactor);
        multiplayer.setPrefHeight(20*zoomfactor);
        multiplayer.setSpacing(50*zoomfactor);
        multiplayer.setTranslateY(15);
        multiplayer.setAlignment(Pos.BOTTOM_CENTER);
        Label single = new Label("Einzelspieler");
        single.setFont(new Font(14*zoomfactor));
        single.setOnMouseClicked(e -> {
            status.setText("");
            Main.playMode = 0;
            Main.joined = false;
            showSettingsMenu(PrimaryStage);
        });

        Label serverLabel = new Label("Server erstellen");
        serverLabel.setFont(new Font(14*zoomfactor));
        Pane host = new Pane(serverLabel);
        host.setOnMouseClicked(mouseEvent -> {
            status.setText("");
            Main.playMode = 1;
            Main.joined = false;
            showSettingsMenu(PrimaryStage);
        });

        Label join = new Label("Server joinen");
        join.setFont(new Font(14*zoomfactor));
        join.setOnMouseClicked(e -> {
            status.setText("");
            Main.playMode = 2;
            Main.joined = false;
            showSettingsMenu(PrimaryStage);
        });
        //multiplayer.setMaxHeight(multiplayer.getHeight()*zoomfactor);
        multiplayer.getChildren().addAll(single, host, join);
        multiplayer.setId("MMtop");
        BorderPane root = new BorderPane();

        top.getChildren().add(multiplayer);
        header.setCenter(top);
        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);
        root.setBottom(bottom);


        Button start = new Button();
        switch (Main.playMode) {
            case 0:
                single.setId("Tab");
                start.setText("Spiel starten");
                start.setOnAction(e -> setSettings("start"));
                break;
            case 1:
                if (!Main.joined) {
                    serverLabel.setId("Tab");
                    start.setText("Raum erstellen");
                    start.setOnAction(e -> setSettings("create"));
                } else {
                    start.setText("Spiel starten");
                    start.setOnAction(e -> setSettings("startserver"));
                }
                break;
            case 2:
                join.setId("Tab");
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

        top.setMinHeight((400*zoomfactor) / 4);
        //center.setPrefHeight(PrimaryStage.getHeight()- (top.getHeight()+bottom.getHeight()));
        bottom.setMinHeight((400*zoomfactor)/6);


        root.setTop(header);
        root.setCenter(center);


        BackgroundImage myBI = new BackgroundImage(Main.table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));
        try {
            if (playMode>=1 && Main.joined && server.serverOpen()){
                Scene menu = GuiLobby.lobby();
                 PrimaryStage.setScene(menu);
                 PrimaryStage.show();
            }else {
                if(playMode>=1 && Main.joined && !server.serverOpen()){
                    Main.joined = false;
                    assigned = false;
                    try {
                        server.leaveServer(uniqueID);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    server = null;
                    shutdown = true;

                }
                //neue Scene
                Scene menu = new Scene(root, Main.sceneWidth, Main.sceneHeight);
                menu.getStylesheets().add("GUI/MainMenu.css");

                PrimaryStage.setScene(menu);
                PrimaryStage.show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        PrimaryStage.setOnCloseRequest(windowEvent -> {
            try {System.exit(0);
            }
            catch (Exception e) {}

        });
    }


    /** Führt die Button-Actions im Hauptmenü aus
     * @param action welcher Button wurde geklickt
     */
    void setSettings(String action) {
        status.setTranslateY(30*zoomfactor);
        status.setId("status");
        status.setFont(new Font(12*zoomfactor));

        if (action == "start") { //Single-Player-Spiel
            inMenu = false;
            playMode = 0;
            ich = 0;
            Main.botPlayTime = (long) slider.getValue();
            Main.botlevel = botselect.getSelectionModel().getSelectedIndex();
            spielArt = spielart.getSelectionModel().getSelectedIndex();
            if(spielArt == 1)
                spielArtLimit = Integer.parseInt(spielartLimit.getText());
            System.out.println(spielArt);
            System.out.println(Integer.parseInt(spielartLimit.getText()));
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
            Main.anzSpieler = (int) playeranzselect.getValue();
            Main.initGame();
            Main.runTimers(Main.classPrimaryStage);
            Main.spieltischGui.buildStage(Main.classPrimaryStage);
            if(tutorialAn && playMode==0){
                tutorial.startTutorial();
            }



        } else if (action == "close") { //Host
            if(Main.playMode == 1){
                try {
                    server.closeServer();
                } catch (RemoteException e) {
                    System.err.println("Server schließen fehlgeschlagen");
                    e.printStackTrace();
                }
                Main.joined = false;
                try {
                    runServer.stop();
                    update.cancel();
                    server = null;
                } catch (Exception e) {}
                status.setText("Server wurde geschlossen");
                showSettingsMenu(Main.classPrimaryStage);
            }else{
                joined = false;
                update.cancel();
                try {
                    server.leaveServer(uniqueID);
                    server = null;
                    System.out.println("Client Disconnected. action = close");
                    status.setText("Verbindung wurde getrennt");
                } catch (RemoteException e) {}
                showSettingsMenu(Main.classPrimaryStage);
            }


        } else if (action == "create") { //Host
            Main.joined = true;
            Main.botPlayTime = (long) slider.getValue();
            Main.botlevel = botselect.getSelectionModel().getSelectedIndex();
            spielArt = spielart.getSelectionModel().getSelectedIndex();
            if(spielArt == 1)
                spielArtLimit = Integer.parseInt(spielartLimit.getText());
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
            Main.anzSpieler = (int) playeranzselect.getValue();

            try {
                tisch = null;
                int portn = Integer.parseInt(port.getText());
                runServer = new RunServer(ip.getText(),
                        "Server", portn, uniqueID, myName);
                Port = port.getText();
                IP = ip.getText();

                server = runServer.starting();
                status.setText("Warte auf Spieler");
                status.setTextFill(Color.LIGHTGREEN);
                update = new Timer();
                update.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(Main.spiellogik == null)
                        Platform.runLater(() -> showSettingsMenu(Main.classPrimaryStage));
                        else
                        {
                            update.cancel();
                            Platform.runLater(() ->
                                    Main.spieltischGui.buildStage(Main.classPrimaryStage));
                        }


                    }
                }, 1000, 1000);
            } catch (Exception e) {
                status.setTextFill(Color.RED);
                status.setText("Server konnte nicht gestartet werden");
                e.printStackTrace();

            }

            showSettingsMenu(Main.classPrimaryStage);

        } else if (action == "startserver") {
            try {
                update.cancel();
            } catch (Exception e) {}
            inMenu = false;

            Main.initGame();
            try {
                ich = server.assignId(uniqueID);
            } catch (RemoteException e) {
            }
            gameRunning = true;
            try {
                ich = server.assignId(uniqueID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Main.runTimers(Main.classPrimaryStage);
            Main.spieltischGui.buildStage(Main.classPrimaryStage);


        } else if (action == "join") {
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) {
                Main.myName = "Spieler";
            }
            try {
                runClient = new RunClient(ip.getText(),
                        Integer.parseInt(port.getText()),
                        "Server",
                        uniqueID,
                        Main.myName);
                server = runClient.client.server;
                try{
                    if(!server.getSpielBeendet() && server.isInGame(uniqueID) && server.serverOpen()){
                        server.reconnect(uniqueID, myName);
                        hasLeftServer = true;

                        inMenu = false;
                        assigned = true;

                        tisch = server.updateTisch();
                        anzSpieler = server.getAnzahlSpieler();
                        update.cancel();

                        Platform.runLater(() -> spieltischGui.buildStage(classPrimaryStage));
                        Main.runTimers(Main.classPrimaryStage);
                        getTisch = new Thread(new ClientThread(Main.server, runClient.client));
                        getTisch.start();

                    }

                }catch (RemoteException | NullPointerException e2){
                    //System.err.println(e2.toString());
                }
                if(server.getAnzClients() >= server.getAnzahlSpieler()) {
                    System.out.println(server.getAnzClients() + " max: " + server.getAnzahlSpieler());
                }
                update = new Timer();
                update.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> showSettingsMenu(Main.classPrimaryStage));
                        if(shutdown){
                            update.cancel();
                            shutdown = false;
                            status.setText("Verbindung zu Server verloren");
                        }
                    }
                }, 1500, 1500);
                joined = true;
                status.setText("Verbunden");
                status.setFont(new Font(12*zoomfactor));
                status.setTextFill(Color.LIGHTGREEN);


            } catch (Exception e) {
                status.setText("Verbindung konnte nicht hergestellt werden");
                status.setTextFill(Color.RED);
            }
            Main.myName = namefield.getText();
            if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";

            GuiZoomLoader.getZoomedImages();
            showSettingsMenu(Main.classPrimaryStage);

        } else if (action == "leave") {
            joined = false;
            update.cancel();
                       try {
                server.leaveServer(uniqueID);
                server = null;
                status.setText("Verbindung wurde getrennt");
                status.setFont(new Font(12*zoomfactor));
                System.out.println("Client Disconnected. action = leave ");

            } catch (RemoteException e) {}
            showSettingsMenu(Main.classPrimaryStage);
        }
    }

    /**
     * Wird für das resetten des Clients verwendet, wenn er die verbindung zum Server verliert.
     * siehe: RMIClient.forceLeaveServer()
     */
    public void cleanupServer(){
        Main.joined = false;
        update.cancel();
        assigned = false;
        Main.myTurnUpdate = true;
        try {
            server.leaveServer(uniqueID);
        } catch (RemoteException e) {
        }catch (NullPointerException e){
            System.err.println(e.toString());
        }
        server = null;
        Platform.runLater(() ->status.setText("Verbindung zu Server verloren"));
    }

    /**
     * Schließt den Server. Host Seite.
     */
    public void closeServer(){
        Main.joined = false;
        try {
            runServer.stop();
            update.cancel();
            server = null;
        } catch (Exception e) {}
        status.setText("Server wurde geschlossen");

        showSettingsMenu(Main.classPrimaryStage);
    }

    /**
     * Methode zur begrenzung der Text Eingabe
     * @param i erlaube Anzahl an Zeichen
     * @return EventHandler
     */
    public EventHandler<KeyEvent> maxLength(final Integer i) {
        return new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent arg0) {
                if(namefield.getText() != null) {
                    TextField tx = (TextField) arg0.getSource();
                    if (tx.getText().length() >= i) {
                        arg0.consume();
                    }
                }
            }

        };

    }

    public void reposition(Stage primaryStage){
        if(chatbox != null && chatbox.chat != null){
            chatbox.reposition(primaryStage);
        }

        if(einstellung != null && einstellung.getSettings() != null){
            einstellung.reposition(primaryStage);
        }


    }
}
