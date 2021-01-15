package GUI;

import RMI.RMIClient;
import RMI.RunClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.*;

import java.rmi.RemoteException;
import java.util.*;

import Main.*;

import static Main.Main.*;
import static Main.Main.ich;


public class GuiSpieltisch {


    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();
    private ArrayList<Double> deg =  new ArrayList<>();
    private List<String> log = new ArrayList<>();
    boolean chatOpened = false;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE =
            "-fx-background-insets: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-cursor:hand;" +
                    "-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);";


    public GuiSpieltisch() {

    }

    /**
     * Erstellt die Fläche auf dem Tisch für einen einzelnen Spieler,
     * d.h. Karten, Chips, Name etc
     *
     * @param playerId
     * @return Gibt ein Panel zurück
     */
    private Pane makepanel(int playerId) {


        VBox pane = new VBox();
        pane.setAlignment(Pos.BOTTOM_CENTER);
        //pane.setStyle("-fx-background-color:#eeeeee;");
        VBox.setMargin(pane, new Insets(0, 0, 5, 0));


        int tablePos = Math.floorMod(playerId-ich, anzSpieler);
        Label plr = new Label(tisch.getSpielerList()[playerId].getName()); //PlayerID für debug
        if (tablePos > 1 && tablePos < 5) {
            plr.setRotate(180);
        }
        plr.setTextFill(Color.WHITE);
        plr.setFont(Font.font("Verdana", 12 * zoomfactor));
        if (tisch.getSpielerList()[playerId] == Main.tisch.getAktivSpieler()) {
            plr.setTextFill(Color.YELLOW);
            plr.setFont(Font.font("Verdana", FontWeight.BOLD, 14 * zoomfactor));
        }
        if(playerId!=ich)
            plr.setTranslateY(-15);
        pane.getChildren().add(plr);

        int cardcount = tisch.getSpielerList()[playerId].getCardCount();
        if (cardcount > 7 && playerId != ich) {
            plr.setText(tisch.getSpielerList()[playerId].getName() + " (" + cardcount + ")");
            cardcount = 7;
        }


        ColorAdjust desaturate = new ColorAdjust();
        desaturate.setSaturation(-1);


        StackPane cards = new StackPane();
        cards.setAlignment(Pos.BASELINE_CENTER);
        //cards.setStyle("-fx-background-color:#cccccc;");

        //verdeckte Karten


        for (int i = 0; i < cardcount; i++) {
            ImageView imgView = new ImageView(Main.image);

            if (playerId == ich) {
                imgView = new ImageView(
                        Main.cardsArray[tisch.getSpielerList()[playerId].getCardHand().getKarte(i).getValue() - 1]);
                ImageView finalImgView = imgView;
                imgView.setOnMouseEntered(e -> finalImgView.setStyle(HOVERED_BUTTON_STYLE));
                ImageView finalImgView1 = imgView;
                imgView.setOnMouseExited(e -> finalImgView1.setStyle(IDLE_BUTTON_STYLE));
            } else if (i > 6)
                continue;


            imgView.setPreserveRatio(true);
            imgView.setSmooth(true); //Visuelle Große der Handkarte ändern
            imgView.setFitWidth(playerId == ich ? 80 * zoomfactor : 40 * zoomfactor);


            if (playerId != ich) {
                imgView.setTranslateX(-cardcount / 2 * 10*zoomfactor + 10*zoomfactor * i);
                imgView.setTranslateY(-10);
                imgView.setRotate(-cardcount / 2 * 15 + i * 15);
            } else {
                int finalI = i;
                imgView.setOnMouseClicked(mouseEvent -> {
                    //Multiplayermodus

                    if(Main.playMode == 2){
                        try {
                            server.karteLegen(server.updateTisch().getSpielerList()[playerId],
                                    server.updateTisch().getSpielerList()[playerId].getCardHand().getKarte(finalI));

                            System.out.println("karte abgelegt");

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                    else{//lokaler Spielmodus
                    Main.spiellogik.karteLegen(tisch.getSpielerList()[playerId],
                            tisch.getSpielerList()[playerId].getCardHand().getKarte(finalI));}


                    if(playMode == 2){
                        try {
                            if(server.updateTisch().getSpielerList()[playerId].getCardHand().getHandKarte().isEmpty()){
                                if(!tisch.getSpielerList()[playerId].getCardHand().getHandKarte().isEmpty()){
                                tisch.getSpielerList()[playerId].getCardHand().removeKarte(tisch.getSpielerList()[playerId].getCardHand().getKarte(0));
                                }
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    if (tisch.getSpielerList()[playerId].getCardHand().getHandKarte().isEmpty()) {
                        Chip tausch;
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("     ");
                        alert.initStyle(StageStyle.TRANSPARENT);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add("GUI/alert.css");

                        ButtonType buttonTypeWhite = new ButtonType("weiß");
                        ButtonType buttonTypeBlack = new ButtonType("schwarz");
                        ButtonType buttonTypeCancel = new ButtonType("schließen", ButtonBar.ButtonData.CANCEL_CLOSE);

                        //spieler hat weiße und Schwarze Chips
                        if (tisch.getSpielerList()[playerId].getWhiteChips() >= 1 && tisch.getSpielerList()[playerId].getBlackChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeBlack, buttonTypeCancel);
                            //nur weiße
                        } else if (tisch.getSpielerList()[playerId].getWhiteChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeCancel);
                            //nur schwarze
                        } else if (tisch.getSpielerList()[playerId].getBlackChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeBlack, buttonTypeCancel);
                        } else {
                            alert.setHeaderText("Du hast keine Chips zum abgeben");
                            alert.getButtonTypes().setAll(buttonTypeCancel);
                        }

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeWhite) {
                            // ... user chose "weiß"
                            tausch = new WhiteChip();
                            if(Main.playMode == 2){ //Multiplayermodus
                                try {
                                    server.chipAbgeben(tisch.getSpielerList()[playerId],tausch);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{//lokaler Spielmodus
                                Main.spiellogik.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                            }
                        } else if (result.get() == buttonTypeBlack) {
                            // ... user chose "schwarz"
                            tausch = new BlackChip();
                            if(Main.playMode == 2){  //Multiplayermodus
                                try {
                                    server.chipAbgeben(tisch.getSpielerList()[playerId],tausch);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            else { //lokaler Spielmodus
                                Main.spiellogik.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                            }

                        } else {
                            // ... user chose CANCEL or closed the dialog
                        }

                    }

                    buildStage(Main.classPrimaryStage);


                });
            }
            if (playerId == ich) {
                if (cardcount > 7) {
                    imgView.setTranslateX(10 * (cardcount % 2) + cardcount / 2 * 20 * zoomfactor - 20 * zoomfactor * i);
                } else {
                    imgView.setTranslateX((cardcount % 2 == 0 ? 55 / 2 * zoomfactor : 0) + 55 * zoomfactor * (i - cardcount / 2));
                }
            }

            if (!tisch.getSpielerList()[playerId].inGame())
                imgView.setEffect(desaturate);
            cards.getChildren().add(imgView);
        }


       /* if (playerId != ich) {
            cards.setTranslateX(+(min(6, spieler[playerId].getCardCount()) * 30) / 2 - 20);
        }*/
        int chipsize = 15;
        pane.getChildren().add(cards);
        GridPane chips = new GridPane();
        //chips.setMaxWidth(60 * zoomfactor);
        ImageView blChip = new ImageView(Main.blackChipImage);
        blChip.setFitHeight(chipsize * zoomfactor);
        blChip.setFitWidth(chipsize * zoomfactor);
        //Chip counter unter die Karte

        ImageView whChip = new ImageView(Main.whiteChipImage);
        whChip.setFitHeight(chipsize * zoomfactor);
        whChip.setFitWidth(chipsize * zoomfactor);

        if (Main.ich == playerId) {
            chips.setOnMouseClicked(mouseEvent -> {
                if(Main.playMode == 2){//Multiplaymodus
                    try {
                        server.chipsTauschen(playerId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else{//lokaler Spielmodus
                  Main.spiellogik.chipsTauschen(playerId);}
                  buildStage(Main.classPrimaryStage);
            });


            chips.setOnMouseEntered(e -> chips.setStyle(HOVERED_BUTTON_STYLE));
            chips.setOnMouseExited(e -> chips.setStyle(IDLE_BUTTON_STYLE));
        }

        chips.add(blChip, 0, 0);
        chips.add(whChip, 0, 1);

        Text text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText("" + tisch.getSpielerList()[playerId].getBlackChips());
        chips.add(text, 1, 0);

        text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText(tisch.getSpielerList()[playerId].getWhiteChips() + "");

        chips.add(text, 1, 1);
        //chips.setY(102);

        //Mute
        if(playerId != ich && playMode != 0) {
            Image muteIcon = new Image("GUI/images/mute.png");
            Image unMuteIcon = new Image("GUI/images/unmute.png");
            ImageView mute = new ImageView(
                    mutelist.contains(tisch.getSpielerList()[playerId].getName())?
                            unMuteIcon:muteIcon
            );
            mute.setTranslateX(10);
            mute.setPreserveRatio(true);
            mute.setFitWidth(20*zoomfactor);
            mute.setOnMouseEntered(e -> mute.setStyle(HOVERED_BUTTON_STYLE));
            mute.setOnMouseExited(e -> mute.setStyle(IDLE_BUTTON_STYLE));

            if(tablePos == 5)
                mute.setRotate(90);
            else if(tablePos == 1)
                mute.setRotate(-90);

            mute.setOnMouseClicked(mouseEvent -> {
                if(mutelist.contains(tisch.getSpielerList()[playerId].getName())) {
                    //bereits gemutet -> unmute
                    mutelist.remove(tisch.getSpielerList()[playerId].getName());
                    mute.setImage(muteIcon);
                } else {
                    mutelist.add(tisch.getSpielerList()[playerId].getName());
                    mute.setImage(unMuteIcon);
                }
            });

            chips.add(mute, 2, 0, 1, 2);

        }
        if(tablePos > 1 && tablePos < 5) {
            chips.setRotate(180);
        }

        HBox bottom = new HBox(chips);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(30);

        //Aussteigen
        if (playerId == Main.ich) {

            ImageView exit = new ImageView(Main.loader.getImg("GUI/images/SVG/no-touch.svg", zoomfactor * 0.45));
            exit.setTranslateY(-7);
            chips.setTranslateY(7);


            exit.setOnMouseEntered(e -> exit.setStyle(HOVERED_BUTTON_STYLE));
            exit.setOnMouseExited(e -> exit.setStyle(IDLE_BUTTON_STYLE));
            bottom.getChildren().add(exit);

            exit.setOnMouseClicked(mouseEvent -> {
                if(Main.playMode == 2){//Multiplaymodus
                    try {
                        server.aussteigen(tisch.getSpielerList()[playerId]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else{//lokaler Spielmodus
                    Main.spiellogik.aussteigen(tisch.getSpielerList()[playerId]);}
                buildStage(Main.classPrimaryStage);
            });

        }
        //Spiel verlassen
        if (playerId == Main.ich) {
            ImageView beenden = new ImageView(Main.loader.getImg("GUI/images/exit.svg", zoomfactor * 0.25));
            beenden.setTranslateY(-5);
            beenden.setTranslateX(125* zoomfactor);
            bottom.setViewOrder(0.0);

            beenden.setOnMouseEntered(e -> beenden.setStyle(HOVERED_BUTTON_STYLE));
            beenden.setOnMouseExited(e -> beenden.setStyle(IDLE_BUTTON_STYLE));
            bottom.getChildren().add(beenden);

            beenden.setOnMouseClicked(mouseEvent -> {
                if (Main.playMode <= 1){
                    Main.gameRunning = false;
                    Main.bots.cancel();
                    Main.joined = false;
                    if (Main.playMode == 1) {

                        try {
                            server.closeServer();
                            Main.hauptmenuGui.runServer.stop();
                            Main.hauptmenuGui.update.cancel();
                            server = null;
                            hauptmenuGui.status = new Label("Server geschlossen");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    Main.hauptmenuGui.showSettingsMenu(Main.classPrimaryStage);

                }else if(Main.playMode == 2){//Multiplaymodus
                    joined = false;
                    try {
                        Main.server.replaceSpielerDurchBot(uniqueID);
                    } catch (RemoteException e) {
                    }
                    hauptmenuGui.update.cancel();
                    server = null;
                    hauptmenuGui.status = new Label("Server verlassen");

                    Main.hauptmenuGui.showSettingsMenu(Main.classPrimaryStage);

                }else{//lokaler Spielmodus
                    Main.gameRunning = false;
                    Main.bots.cancel();
                    Main.hauptmenuGui.showSettingsMenu(classPrimaryStage);}

            });

        }



            pane.getChildren().add(bottom);

        switch (Math.floorMod(playerId-ich,anzSpieler)) {
            case 1:
            case 5:
                pane.setTranslateY(+20 * zoomfactor);
                break;
            case 4:
                pane.setTranslateX(30 * zoomfactor);
                pane.setTranslateY( + 10 * zoomfactor);
                break;
            case 3:
                pane.setTranslateY(10 * zoomfactor);
                break;
            case 2:
                pane.setTranslateX(-30 * zoomfactor);
                pane.setTranslateY( + 10 * zoomfactor);
                break;
        }
        return pane;

    }


    /**
     * Bildet die Stage (neu), sodass Änderungen im Spiel dargestellt werden
     *
     * @param primaryStage
     */
       public void buildStage(Stage primaryStage) {
        try {
            StackPane root = new StackPane();
            Main.classPrimaryStage = primaryStage;

            if (Main.sceneWidth == 0) {
                Main.sceneWidth = 600;
            } else
                Main.sceneWidth = Main.classPrimaryStage.getScene().getWidth();
            if (Main.sceneHeight == 0) {
                Main.sceneHeight = 400;
            } else
                Main.sceneHeight = Main.classPrimaryStage.getScene().getHeight();

            Scene scene = new Scene(root, Main.sceneWidth, Main.sceneHeight, true, SceneAntialiasing.BALANCED);

            if(Main.inMenu) {
                Main.hauptmenuGui.showSettingsMenu(classPrimaryStage);
                return;
            } else {
            if (Main.server == null && Main.spiellogik.getRundeBeendet()) {
                Main.scoreboardGui.showRangliste(Main.spiellogik.ranglisteErstellen());
                return;
            } else if(Main.server != null && Main.server.getRundeBeendet()) {
                    Main.scoreboardGui.showRangliste(server.getRangliste());
                    return;
            }else if(Main.server != null && !Main.server.getRundeBeendet() && scoreboardGui.getIsReady()){
                scoreboardGui.setIsReady(false);
            }


            }
            Main.sceneWidth = scene.getWidth();
            Main.sceneHeight = scene.getHeight();
            Main.lastmove = System.currentTimeMillis();



            BackgroundImage myBI = new BackgroundImage(Main.table1,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, false, true));


            GridPane table = new GridPane();
            //Nachziehstapel
            Image imageback = Main.loader.getImg("GUI/images/SVG/Back.svg", zoomfactor*2);
            Pane nachziehstapel = new Pane();
            for (int i = 0; i < Main.tisch.getNachziehStapelSize(); i++) {
                ImageView imgView = new ImageView(imageback);
                imgView.setY(i * 0.2);
                imgView.setX(i * 0.2);

                if (i == Main.tisch.getNachziehStapelSize() - 1) {
                    imgView.setOnMouseClicked(mouseEvent -> {
                        if(Main.playMode == 2){
                            try {
                                server.karteNachziehen(tisch.getSpielerList()[ich]);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            System.out.println("\t Ziehe Karte");
                        } else
                        if (Main.spiellogik.karteNachziehen(tisch.getSpielerList()[ich]))
                            System.out.println("\t Ziehe Karte");

                        buildStage(Main.classPrimaryStage);
                    });
                    imgView.setOnMouseEntered(e -> imgView.setStyle(HOVERED_BUTTON_STYLE));
                    imgView.setOnMouseExited(e -> imgView.setStyle(IDLE_BUTTON_STYLE));
                }
                imgView.setPreserveRatio(true);
                imgView.setSmooth(true);
                imgView.setFitWidth(60 * zoomfactor); // Visuelle Große des Nachzeihstapel ändern
                nachziehstapel.getChildren().add(imgView);
            }
            table.add(nachziehstapel, 0, 0, 1, 1);

            //Ablagestapel
            Pane ablagestapel = new Pane();
            for (int i = 0; i < Main.tisch.getAblageStapelSize(); i++) {
                ImageView imgView = new ImageView(Main.cardsArray[Main.tisch.ablageStapel.stapel.get(i).getValue()-1]);
                if(i >= x.size()) {
                    y.add(i, Math.random() * 3);
                    x.add(i, 15-30*Math.random());
                    deg.add(i, 15-30*Math.random());
                }
                imgView.setY(y.get(i));
                imgView.setX(x.get(i));
                imgView.setRotate(deg.get(i));
                imgView.setFitWidth(90 * zoomfactor); //Visuelle Große der Ablagestapel ändern
                imgView.setPreserveRatio(true);
                imgView.setSmooth(true);
                ablagestapel.getChildren().add(imgView);
            }
            table.add(ablagestapel, 2, 0, 1, 1);

            GridPane chipsgrid = new GridPane();
            chipsgrid.setAlignment(Pos.CENTER);

            ImageView imgView = new ImageView(Main.blackChipImage);
            imgView.setFitHeight(25 * zoomfactor);
            imgView.setFitWidth(25 * zoomfactor);
            chipsgrid.add(imgView, 0, 0, 1, 1);
            Text ChipText = new Text();
            ChipText.setText(" " + Main.tisch.getBlackChips());
            ChipText.setFont(Font.font("Verdana", 20 * zoomfactor));
            ChipText.setFill(Color.WHITE);
            chipsgrid.add(ChipText, 1, 0, 1, 1);

            imgView = new ImageView(Main.whiteChipImage);
            imgView.setFitHeight(25 * zoomfactor);
            imgView.setFitWidth(25 * zoomfactor);
            chipsgrid.add(imgView, 0, 1, 1, 1);
            ChipText = new Text();
            ChipText.setText(" " + Main.tisch.getWhiteChips());
            ChipText.setFont(Font.font("Verdana", 20 * zoomfactor));
            ChipText.setFill(Color.WHITE);

            chipsgrid.add(ChipText, 1, 1, 1, 1);


            GridPane gridPane = new GridPane();
            gridPane.setBackground(new Background(myBI));
            gridPane.setGridLinesVisible(false);
            gridPane.setAlignment(Pos.TOP_CENTER);

            table.getColumnConstraints().add(new ColumnConstraints()); // column 0 is 100 wide
            table.getColumnConstraints().add(new ColumnConstraints(80 * zoomfactor)); // column 0 is 100 wide

            table.add(chipsgrid, 1, 0, 1, 1);

            for (int i = 0; i < 5; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setPercentWidth(20);

                column.setMinWidth(120);
                column.setFillWidth(true);
                column.setHalignment(HPos.CENTER);
                gridPane.getColumnConstraints().add(column);

                RowConstraints row = new RowConstraints();
                row.setPercentHeight(20);
                row.setMinHeight(50);
                switch (i) {
                    case 0:
                        row.setPercentHeight(33);
                        row.setMinHeight(80);
                        break;
                    case 1:
                    case 2:
                    case 3:
                        row.setPercentHeight(40 / 3);
                        row.setMinHeight(30);
                        break;
                    case 4:
                        row.setPercentHeight(33);
                        row.setMinHeight(120);
                        break;

                }
                row.setFillHeight(true);
                gridPane.getRowConstraints().add(row);
            }

            gridPane.add(table, 1, 1, 3, 3);
            table.setAlignment(Pos.CENTER);

            Rotate rotate = new Rotate();
            //Setting pivot points for the rotation
            rotate.setAngle(90);

            gridPane.add(makepanel(ich), 1, 4, 3, 1);

            Node player1 = makepanel((1+ich)%anzSpieler);
            player1.setRotate(90);
            gridPane.add(player1, 0, 2, 1, 1);

            if (Main.anzSpieler > 2) {
                Node player2 = makepanel((2+ich)%anzSpieler);
                gridPane.add(player2, 0, 0, 2, 1);
                player2.setRotate(155);
            }

            if (Main.anzSpieler > 3) {
                Node player3 = makepanel((3+ich)%anzSpieler);
                player3.setRotate(180);
                gridPane.add(player3, 2, 0, 1, 1);
            }

            if (Main.anzSpieler > 4) {
                Node player4 = makepanel((4+ich)%anzSpieler);
                player4.setRotate(205);
                gridPane.add(player4, 3, 0, 2, 1);
            }

            if (Main.anzSpieler > 5) {
                Node player5 = makepanel((5+ich)%anzSpieler);
                player5.setRotate(-90);
                gridPane.add(player5, 4, 2, 2, 1);
            }


            //Scoreboard
            GridPane score = new GridPane();
            score.setTranslateY(10);
            score.setTranslateX(-5*zoomfactor);
            score.setMaxWidth(90*zoomfactor);
            VBox names = new VBox();
            VBox sc = new VBox();
            names.setAlignment(Pos.TOP_LEFT);
            sc.setAlignment(Pos.TOP_RIGHT);
            Map<Spieler, Integer> rangliste;
            if(Main.server == null) {
                rangliste =  Main.spiellogik.ranglisteErstellen();
            } else rangliste = server.getRangliste();

           for (Map.Entry<Spieler, Integer> entry : rangliste.entrySet()) {
                //System.out.println(entry.getKey().getName() + ":" + entry.getValue());
                Label name = new Label(entry.getKey().getName());
                name.setTextFill(Color.WHITE);
                name.setFont(new Font(10 * zoomfactor));
                names.getChildren().add(name);
                Label sco = new Label(Integer.toString(Math.abs(entry.getValue())));
                sco.setFont(new Font(10 * zoomfactor));
                sco.setTextFill(Color.WHITE);
                sc.getChildren().add(sco);
            }

            score.addRow(0, names, sc);
            score.setHgap(15);
            score.setAlignment(Pos.CENTER_RIGHT);
            gridPane.add(score, 4, 4, 1, 1);


            if(playMode > 0) {
                Button chatButton = new Button(chatOpened? "Chat <<": "Chat >>");
                chatButton.setTranslateX(30);
                chatButton.setTranslateY(40);
                chatButton.setOnMouseClicked(e -> {

                            if (!chatOpened) {
                                chatbox.openChat(classPrimaryStage, 240 * zoomfactor, 90 * zoomfactor);
                                chatButton.setText("Chat <<");
                                chatOpened = true;
                            } else { //chat bereits offen
                                chatButton.setText("Chat >>");
                                chatOpened = false;
                                chatbox.hideChat();
                            }
                        }
                );
                chatButton.setStyle(
                        "-fx-text-fill: black;\n" +
                                "    -fx-background-color: rgba(255,255,255,0.4);\n" +
                                "    -fx-pref-height: 15px;\n" +
                                "    -fx-pref-width: 80px;\n" +
                                "    -fx-font-size: 100%;\n" +
                                "    -fx-alignment: center;"
                );

                gridPane.add(chatButton, 0, 4, 1, 1);

                //Einstellungen
                Image hinweis = new Image("GUI/images/tip_plain.png");
                Image filter = new Image("GUI/Images/filter.png");
                Image sort = new Image("GUI/images/sort.png");
                VBox steuerung = new VBox();
                steuerung.setSpacing(10);
                ImageView schimpf = new ImageView(filter);
                ImageView tipp = new ImageView(hinweis);
                ImageView sortieren = new ImageView(sort); ;
                steuerung.getChildren().addAll(schimpf,tipp, sortieren);
                steuerung.getStylesheets().add("GUI/einstellung.css");

                TitledPane settings = new TitledPane("Einstellungen", steuerung);
                settings.setExpanded(false);
                steuerung.setMaxWidth(20);
                settings.setMaxWidth(20);
                settings.getStylesheets().add("GUI/einstellung.css");
                gridPane.add(settings, 4,0,1,1);
                gridPane.setValignment(settings, VPos.TOP);
                gridPane.setHalignment(settings, HPos.RIGHT);
            }

            root.getChildren().add(gridPane);
            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an
            primaryStage.setScene(scene);
            scene.getStylesheets().add("GUI/Chat.css");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void printtoLog(String event){
           log.add(event);
    }

}


