package GUI;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import Main.*;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.MissingFormatArgumentException;

import static Main.Main.*;

public class GuiScoreboard {

    public static boolean isReady = false;

    static Scene showRangliste(Map<Spieler, Integer> ranking) throws  RemoteException {
        //System.out.println("gibt Rangliste aus");
        int p = 1;
        chatbox.hideChat();
        einstellung.hideSettings();

        //Scoreboard
        VBox names = new VBox(new Label(""));
        VBox score = new VBox(new Label(""));
        VBox differ = new VBox(new Label(""));
        VBox platz = new VBox(new Label(""));

        GridPane center = new GridPane();

        Map<Spieler, Integer> rangl = null;
        if(Main.server != null && playMode == 2) {
            try {
                rangl = Main.server.getRangliste();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else
            rangl = Main.spiellogik.ranglisteErstellen();

        for (Map.Entry<Spieler, Integer> entry : rangl.entrySet()) {
            //Platz
            Label rang = new Label("\t" + p + ". Platz: " + "\t");
            rang.setFont(new Font("Ink Free", 19 * Main.zoomfactor));
            rang.setTextFill(Color.WHITE);
            platz.getChildren().add(rang);
            //platz.setStyle("-fx-effect: dropshadow( gaussian , black ,10 ,0.4 ,0 ,0 )");
            //Spieler
            Label name = new Label(entry.getKey().getName() + "  \t");
            name.setTextFill(Color.WHITE);
            name.setFont(new Font("Ink Free", 19 * Main.zoomfactor));
            names.getChildren().add(name);
            //names.setStyle("-fx-effect: dropshadow( gaussian , black ,10 ,0.4 ,0 ,0 )");

            //Punktestand
            int dif = entry.getKey().getPoints() - entry.getKey().getOldScore();


            Label kassiert;
            if (dif < 0) {
                kassiert = new Label(Integer.toString(dif) + "\t");
                kassiert.setTextFill(Color.web("#f76254"));
            } else {
                kassiert = new Label("+" + Integer.toString(dif) + "\t");
                kassiert.setTextFill(Color.LIGHTGREEN);

            }
            kassiert.setFont(new Font("Ink Free", 19 * Main.zoomfactor));
            kassiert.setStyle("-fx-effect: dropshadow( gaussian , black ,10 ,0.7 ,0 ,0 ); -fx-font-weight: bolder");


            differ.getChildren().add(kassiert);

            Label sco = new Label(Integer.toString(entry.getValue()));
            sco.setFont(new Font("Ink Free", 19 * Main.zoomfactor));
            sco.setTextFill(Color.WHITE);
            score.getChildren().add(sco);
            //score.setStyle("-fx-effect: dropshadow( gaussian , black ,10 ,0.6 ,0 ,0 )");

            p++;

            //System.out.println(entry.getKey().getName() + ":  -  alt:" + entry.getKey().getOldScore() + "   neu:" + entry.getKey().getPoints() + "   dif:" + dif);

        }

        center.addRow(0, platz, names, score, differ);
        center.setHgap(30 * Main.zoomfactor);
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('/GUI/images/oberflaeche.jpg');-fx-background-size: cover");
        center.setMinHeight(250 * Main.zoomfactor);
        center.setMinWidth(300 * Main.zoomfactor);

        HBox bottom = new HBox();
        bottom.setSpacing(15);
        //bottom.setMinHeight(sceneHeight / 8);
        bottom.setAlignment(Pos.CENTER);

        Button nextRound = new Button();
        Label wait = new Label();
        //Tutorial
        if (playMode==0&&tutorialAn){
            Button spielStart = new Button("Spiel starten");
            spielStart.setOnAction(e->{
                Main.bots.cancel();
                resizecheck.cancel();
                spiellogik = null;
                tutorialAn=false;
                tooltip=false;
                hauptmenuGui.setSettings("start");

            });

            Button hauptmenu = new Button ("Hauptmenu");
            hauptmenu.setOnAction(e->{
                resizecheck.cancel();
                Main.inMenu = true;
                Main.gameRunning = false;
                Main.myTurnUpdate = true;
                spiellogik = null;
                try {
                    Main.bots.cancel();
                } catch (NullPointerException l) {
                }
                hauptmenuGui.showSettingsMenu(Main.classPrimaryStage);
            });
            hauptmenu.setTranslateY(-15);
            spielStart.setTranslateY(-15);
            bottom.getChildren().addAll(spielStart,hauptmenu);

        }else {

            try {
                if ((Main.server != null && !Main.server.getSpielBeendet() && !isReady) ||
                        (Main.server == null && !Main.spiellogik.spielBeendet)) {
                    nextRound = new Button("Nächste Runde");
                    nextRound.setOnAction(e -> {
                        if (playMode >= 1) {
                            try {
                                Main.server.neueRunde(true, uniqueID);
                                isReady = true;
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                        } else
                            Main.spiellogik.initNeueRunde();
                        sortedOnce = false;
                        Main.spieltischGui.buildStage(Main.classPrimaryStage);
                    });

                } else if ((Main.server != null && !Main.server.getSpielBeendet() && isReady)) {
                    wait.setText("Warte auf Mitspieler... [" + server.getAnzReadyClients() + "|" + server.getAnzClients() + "] sind bereit");
                    server.checkForNewRound();

                } else {
                    Button endGame = new Button("Spiel beenden");
                    endGame.setTranslateY(-15);
                    endGame.setOnAction(e -> {
                                if (playMode == 2) {
                                    try {
                                        server.leaveServer(uniqueID);
                                    } catch (RemoteException remoteException) {
                                        remoteException.printStackTrace();
                                    }
                                }

                                try {
                                    Main.bots.cancel();
                                } catch (NullPointerException l) {
                                }
                                Main.resizecheck.cancel();
                                System.exit(0);
                            }
                    );
                    bottom.getChildren().add(endGame);

                    nextRound = new Button("Hauptmenü");
                    nextRound.setOnAction(e -> {
                                if (playMode == 2) {
                                    resizecheck.cancel();
                                    joined = false;
                                    try {
                                        Main.server.replaceSpielerDurchBot(uniqueID);
                                    } catch (RemoteException e2) {
                                    }
                                    hauptmenuGui.update.cancel();
                                    server = null;
                                    hauptmenuGui.status = new Label("Server verlassen");
                                    Platform.runLater(() -> hauptmenuGui.showSettingsMenu(Main.classPrimaryStage));

                                } else {
                                    if (playMode == 1){
                                        try {
                                            server.closeServer();
                                        } catch (RemoteException e1) {}
                                        hauptmenuGui.status = new Label("Server verlassen");
                                        Main.hauptmenuGui.update.cancel();
                                    }

                                    resizecheck.cancel();
                                    joined = false;
                                    Main.inMenu = true;
                                    Main.gameRunning = false;
                                    Main.myTurnUpdate = true;
                                    spiellogik = null;
                                    server = null;
                                    try {
                                        Main.bots.cancel();
                                    } catch (NullPointerException l) {
                                    }
                                    hauptmenuGui.closeServer();
                                }

                            }
                    );
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if ((Main.server != null && !Main.server.getSpielBeendet() && isReady)) {
                wait.setFont(new Font("Ink Free", 19 * Main.zoomfactor));
                wait.setTextFill(Color.WHITE);
                wait.setTranslateY(-15);

                bottom.getChildren().add(wait);

            } else {
                nextRound.setTranslateY(-15);
                bottom.getChildren().add(nextRound);
            }
        }



        //Darstellung
        Label titel = new Label("Rangliste");
        titel.setFont(new Font("Script MT Bold", 50 * Main.zoomfactor));
        titel.setTextFill(Color.WHITE);

        HBox top = new HBox(titel);
        //top.setMinHeight(sceneHeight/8);
        top.setAlignment(Pos.CENTER);


        VBox left = new VBox();
        left.setMinWidth(Main.sceneWidth / 10);
        left.setAlignment(Pos.TOP_LEFT);

        VBox right = new VBox();
        right.setMinWidth(Main.sceneWidth / 10);
        right.setAlignment(Pos.TOP_RIGHT);

        center.setAlignment(Pos.TOP_LEFT);
        center.setMaxHeight(center.getHeight());
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(center);
        root.setRight(right);
        root.setBottom(bottom);
        root.setLeft(left);

        BackgroundImage myBI = new BackgroundImage(Main.table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
            new BackgroundSize(1.0, 2.0, true, true, false, false));
        root.setBackground(new Background(myBI));


        //neue Scene
        Scene rangliste = new Scene(root, Main.sceneWidth, Main.sceneHeight);
        rangliste.getStylesheets().add("GUI/Rangliste.css");

        Main.classPrimaryStage.setScene(rangliste);
        //spiellogik.durchschnitt();
        return rangliste;
    }

    public boolean getIsReady(){
        return isReady;
    }
    public void setIsReady(boolean bool){
        isReady = bool;
    }
}
