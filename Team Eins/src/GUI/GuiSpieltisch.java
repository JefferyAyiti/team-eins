package GUI;

import RMI.RMIClient;
import RMI.RunClient;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
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
import java.util.function.DoubleToIntFunction;

import Main.*;
import javafx.util.Duration;

import static Main.Main.*;
import static Main.Main.ich;


public class GuiSpieltisch {


    private ArrayList<Double> x = new ArrayList<>();
    private ArrayList<Double> y = new ArrayList<>();
    private ArrayList<Double> deg = new ArrayList<>();;
    boolean chatOpened = false;
    boolean settingsOpen = false;
    int[] cardId;
    Boolean myTurnNotice = null;
    private GridPane spielfeld;

    double dragX;
    double dragY;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE =
            "-fx-background-insets: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-cursor:hand;" +
                    "-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);";
    private static final String HILIGHT_BUTTON_STYLE =
            "-fx-background-insets: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-cursor:hand;" +
                    "-fx-effect: dropshadow(three-pass-box, gold, 30, 0.5, 0, 0);";


    public GuiSpieltisch() {

    }

    StackPane myCards;


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


        int tablePos = Math.floorMod(playerId - ich, anzSpieler);
        Label plr = new Label(tisch.getSpielerList()[playerId].getName()); //PlayerID für debug
        if (tutorialAn==false && tablePos > 1 && tablePos < 5) {
            plr.setRotate(180);
        }

        plr.setTextFill(Color.WHITE);
        plr.setFont(Font.font("Verdana", 12 * zoomfactor));
        if (tisch.getSpielerList()[playerId] == Main.tisch.getAktivSpieler()) {
            plr.setTextFill(Color.YELLOW);
            plr.setFont(Font.font("Verdana", FontWeight.BOLD, 14 * zoomfactor));
        }
        if (playerId != ich)
            plr.setTranslateY(-15);
        pane.getChildren().add(plr);

        int cardcount = tisch.getSpielerList()[playerId].getCardCount();
        if (cardcount > 7 && playerId != ich) {
            plr.setText(tisch.getSpielerList()[playerId].getName() + " (" + cardcount + ")");
            cardcount = 7;
        }


        if (playerId == ich) {
            if (autoSort != null && (autoSort == false && !sortedOnce || autoSort)) {
                //sortieren
                ArrayList<HandKarte> sortedHand =
                        tisch.getSpielerList()[ich].getCardHand().getHandKarte();
                Collections.sort(sortedHand, new Comparator<>() {
                    @Override
                    public int compare(HandKarte handKarte, HandKarte t1) {
                        return handKarte.getValue() >= t1.getValue() ? 1 : -1;
                    }
                });
                sortedOnce = true;
                tisch.getSpielerList()[ich].getCardHand().setHandKarten(sortedHand);
            }
        }

        ColorAdjust desaturate = new ColorAdjust();
        desaturate.setSaturation(-1);

        double[] cardPos = new double[cardcount];
        StackPane cards = new StackPane();
        if (playerId == ich) {
            myCards = cards;
        }
        pane.getChildren().add(cards);
        cards.setAlignment(Pos.BASELINE_CENTER);
        //cards.setStyle("-fx-background-color:#cccccc;");

        //verdeckte Karten

        ImageView[] myCardImages = new ImageView[cardcount];

        for (int i = 0; i < cardcount; i++) {
            ImageView imgView = new ImageView(Main.image);

            if (playerId == ich) {

                imgView = new ImageView(
                        Main.cardsArray[tisch.getSpielerList()[playerId].getCardHand().getKarte(i).getValue() - 1]);
                ImageView finalImgView = imgView;
                myCardImages[i] = imgView;
                if (tisch.aktiv == ich || true) {

                    imgView.setOnMouseEntered(e -> finalImgView.setStyle(HOVERED_BUTTON_STYLE));
                    ImageView finalImgView1 = imgView;
                    imgView.setOnMouseExited(e -> finalImgView1.setStyle(IDLE_BUTTON_STYLE));
                }
            } else if (i > 6)
                continue;


            imgView.setPreserveRatio(true);
            imgView.setSmooth(true); //Visuelle Große der Handkarte ändern
            imgView.setFitWidth(playerId == ich ? 80 * zoomfactor : 40 * zoomfactor);


            if (playerId != ich) {
                imgView.setTranslateX(-cardcount / 2 * 10 * zoomfactor + 10 * zoomfactor * i);
                imgView.setTranslateY(-10);
                imgView.setRotate(-cardcount / 2 * 15 + i * 15);
            } else {
                if (playerId == ich) {
                    if (cardcount > 7) {
                        cardPos[i] = 10 * (cardcount % 2) + cardcount / 2 * 20 * zoomfactor - 20 * zoomfactor * i;
                        imgView.setTranslateX(cardPos[i]);
                    } else {
                        cardPos[i] = (cardcount % 2 == 0 ? 55 / 2 * zoomfactor : 0) + 55 * zoomfactor * (i - cardcount / 2);
                        imgView.setTranslateX(cardPos[i]);
                    }

                    if (tisch.aktiv == ich || true) {
                        ImageView myCard = imgView;
                        myCardImages[i] = imgView;
                        int finalI = i;

                        //Spielbare Karten
                        if (Main.tooltip && tisch.aktiv == ich) {
                            if (!tisch.getSpielerList()[playerId].getCardHand().getKarte(finalI).isPlayable()) {
                                ColorAdjust colorAdjust = new ColorAdjust();
                                colorAdjust.setBrightness(-0.6);
                                myCard.setEffect(colorAdjust);
                            }
                        }

                        final double[] myCardsX = new double[2];
                        final double[] myCardsY = new double[2];
                        final int[] shifted = new int[cardcount];

                        imgView.setOnMouseDragged(e -> {
                            timerRunning = false;
                            if (e.getSceneX() > ablageX[0] &&
                                    e.getSceneX() < ablageX[1] &&
                                    e.getSceneY() > ablageY[0] &&
                                    e.getSceneY() < ablageY[1]) {
                                if (tisch.getSpielerList()[playerId].getCardHand().getKarte(finalI).isPlayable() &&
                                tisch.aktiv == ich)
                                    ablagestapel.setStyle("-fx-background-insets: 20; " +
                                            "-fx-background-radius: 20; " +
                                            "-fx-cursor:hand;" +
                                            "-fx-effect: dropshadow(three-pass-box, green, 20, 0, 0, 0);");
                                else
                                    ablagestapel.setStyle("-fx-background-insets: 20; " +
                                            "-fx-background-radius: 20; " +
                                            "-fx-cursor:hand;" +
                                            "-fx-effect: dropshadow(three-pass-box, red, 20, 0, 0, 0);");
                            } else {
                                ablagestapel.setStyle(IDLE_BUTTON_STYLE);
                            }


                            //hovern über dem kartenstapel
                            if (e.getSceneX() > myCardsX[0] - 40 &&
                                    e.getSceneX() < myCardsX[1] + 40 &&
                                    e.getSceneY() > myCardsY[0] &&
                                    e.getSceneY() < myCardsY[1]) {
                                for (int c = 0; c < tisch.getSpielerList()[playerId].getCardCount(); c++) {
                                    //für jede karte prüfen ob beim hovern links oder rechts von der gezogenen karte
                                    if (!myCardImages[c].equals(myCard)) {
                                        if (myCard.getTranslateX()
                                                > myCardImages[c].getTranslateX()) {
                                            if (shifted[c] >= 0 && cardPos[c] > cardPos[finalI]) {
                                                myCardImages[c].setTranslateX(cardPos[c] - 60);
                                                shifted[c] = -1;
                                            }
                                        } else if (!myCardImages[c].equals(myCard)) {
                                            if (myCard.getTranslateX()
                                                    < myCardImages[c].getTranslateX()) {
                                                if (shifted[c] <= 0 && cardPos[c] < cardPos[finalI]) {
                                                    myCardImages[c].setTranslateX(cardPos[c] + 60
                                                    );
                                                    shifted[c] = 1;
                                                    break;


                                                }
                                            }
                                        }
                                    }
                                }

                            }

                            myCard.setTranslateX(e.getSceneX() - pane.getLayoutX() - myCard.getLayoutX() - 40);
                            myCard.setTranslateY(e.getSceneY() - pane.getLayoutY() - 60);

                        });


                        imgView.setOnMousePressed(event -> {

                            timerRunning = false;
                            Bounds nodeBounds = myCards.localToScene(myCards.getBoundsInLocal());
                            myCardsX[0] = nodeBounds.getMinX();
                            myCardsX[1] = nodeBounds.getMaxX();

                            myCardsY[0] = nodeBounds.getMinY();
                            myCardsY[1] = nodeBounds.getMaxY();


                            cardId = new int[]{playerId, finalI};

                        });

                        imgView.setOnMouseClicked(e -> {
                            if (e.isStillSincePress()) {
                                if(tutorial.wrongCard(finalI)) {
                                }else{
                                    kartelegen(playerId, finalI);
                                }
                            }
                        });


                        int finalCardcount = cardcount;
                        imgView.setOnMouseReleased(e -> {

                            if (e.getSceneX() > ablageX[0] &&
                                    e.getSceneX() < ablageX[1] &&
                                    e.getSceneY() > ablageY[0] &&
                                    e.getSceneY() < ablageY[1] &&
                                    tisch.getSpielerList()[playerId].getCardHand().getKarte(finalI).isPlayable()) {

                                kartelegen(cardId[0], cardId[1]);
                            } else //karten sortieren
                                if (e.getSceneX() > myCardsX[0] - 40 &&
                                        e.getSceneX() < myCardsX[1] + 40 &&
                                        e.getSceneY() > myCardsY[0] &&
                                        e.getSceneY() < myCardsY[1]) {
                                    TreeMap<Double, HandKarte> newHand = new TreeMap<>();
                                    for (int c = 0; c < finalCardcount; c++) {
                                        newHand.put(myCardImages[c].getTranslateX(),
                                                tisch.getSpielerList()[playerId].getCardHand().getKarte(c));
                                    }


                                    Collection<HandKarte> values = newHand.values();
                                    ArrayList<HandKarte> newHandCards = new ArrayList<HandKarte>(values);
                                    if (playMode == 0) {
                                        tisch.getSpielerList()[playerId].getCardHand().setHandKarten(newHandCards);
                                    } else { //multiplayer
                                        try {
                                            server.setCardHand(playerId, newHandCards);
                                        } catch (RemoteException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    buildStage(classPrimaryStage);

                                } else {
                                    myCard.setTranslateX(cardPos[finalI]);
                                    myCard.setTranslateY(0);
                                    cardId = null;
                                    myCard.setMouseTransparent(false);
                                }
                            timerRunning = true;
                        });
                    }

                    //imgView.setTranslateZ(imgView.getTranslateZ()-1);
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
                if (Main.playMode == 2) {//Multiplaymodus
                    try {
                        server.chipsTauschen(playerId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {//lokaler Spielmodus
                    Main.spiellogik.chipsTauschen(playerId);
                }
                buildStage(Main.classPrimaryStage);
            });

            if (tutorial.chips) {
                chips.setStyle(HILIGHT_BUTTON_STYLE);

            }else if(tutorialAn && tutorial.chips==false){
                chips.setStyle(IDLE_BUTTON_STYLE);
            }else {
                    chips.setOnMouseEntered(e -> chips.setStyle(HOVERED_BUTTON_STYLE));
                    chips.setOnMouseExited(e -> chips.setStyle(IDLE_BUTTON_STYLE));
                }
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
        if (playerId != ich && playMode != 0) {
            Image muteIcon = new Image("GUI/images/mute.png");
            Image unMuteIcon = new Image("GUI/images/unmute.png");

            ImageView mute = new ImageView(
                    mutelist.contains(tisch.getSpielerList()[playerId].getName()) ?
                            unMuteIcon : muteIcon
            );
            mute.setTranslateZ(0);
            mute.setTranslateX(10);
            mute.setPreserveRatio(true);
            mute.setFitWidth(20 * zoomfactor);
            mute.setOnMouseEntered(e -> mute.setStyle(HOVERED_BUTTON_STYLE));
            mute.setOnMouseExited(e -> mute.setStyle(IDLE_BUTTON_STYLE));

            if (tablePos == 5)
                mute.setRotate(90);
            else if (tablePos == 1)
                mute.setRotate(-90);

            mute.setOnMouseClicked(mouseEvent -> {
                if (mutelist.contains(tisch.getSpielerList()[playerId].getName())) {
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
        if (!tutorialAn &&(tablePos > 1 && tablePos < 5)) {
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
                if (Main.playMode == 2) {//Multiplaymodus
                    try {
                        server.aussteigen(tisch.getSpielerList()[playerId]);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {//lokaler Spielmodus
                    Main.spiellogik.aussteigen(tisch.getSpielerList()[playerId]);
                }
                buildStage(Main.classPrimaryStage);
            });

        }


        pane.getChildren().add(bottom);

        switch (Math.floorMod(playerId - ich, anzSpieler)) {
            case 1:
            case 5:
                pane.setTranslateY(+20 * zoomfactor);
                break;
            case 4:
                pane.setTranslateX(30 * zoomfactor);
                pane.setTranslateY(+10 * zoomfactor);
                break;
            case 3:
                pane.setTranslateY(10 * zoomfactor);
                break;
            case 2:
                if(tutorialAn){
                    pane.setTranslateY(10 * zoomfactor);
                }else {
                    pane.setTranslateX(-30 * zoomfactor);
                    pane.setTranslateY(+10 * zoomfactor);
                }
                break;
        }
        return pane;

    }

    StackPane root = new StackPane();
    Scene scene = new Scene(root, Main.sceneWidth, Main.sceneHeight, true, SceneAntialiasing.BALANCED);
    Pane ablagestapel;
    VBox turnNotice = new VBox();

    double[] ablageX, ablageY;

    /**
     * Bildet die Stage (neu), sodass Änderungen im Spiel dargestellt werden
     *
     * @param primaryStage
     */
    public void buildStage(Stage primaryStage) {
        primaryStage.xProperty().addListener((obs, oldVal, newVal) -> reposition(primaryStage));
        primaryStage.yProperty().addListener((obs, oldVal, newVal) -> reposition(primaryStage));



        if(myTurnNotice == null || tisch.aktiv != ich) {
            myTurnNotice = true;
        }

        root.setOnMousePressed(event -> {
            Bounds nodeBounds = ablagestapel.localToScene(ablagestapel.getBoundsInLocal());
            ablageX = new double[]{nodeBounds.getMinX(), nodeBounds.getMaxX()};
            ablageY = new double[]{nodeBounds.getMinY(), nodeBounds.getMaxY()};
            turnNotice.setVisible(false);

        });
        try {

            Main.classPrimaryStage = primaryStage;



            if (Main.inMenu) {
                Main.hauptmenuGui.showSettingsMenu(classPrimaryStage);
                return;
            } else {
                if (Main.server == null && Main.spiellogik.getRundeBeendet()) {
                    Main.scoreboardGui.showRangliste(Main.spiellogik.ranglisteErstellen());
                    return;
                } else if (Main.server != null && Main.server.getRundeBeendet()) {
                    Main.scoreboardGui.showRangliste(server.getRangliste());
                    return;
                } else if (Main.server != null && !Main.server.getRundeBeendet() && scoreboardGui.getIsReady()) {
                    scoreboardGui.setIsReady(false);
                }


            }

            Main.lastmove = System.currentTimeMillis();


            BackgroundImage myBI = new BackgroundImage(Main.table1,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 2.0, true, true, false, false));




            GridPane table = new GridPane();
            //Nachziehstapel
            Image imageback = Main.loader.getImg("GUI/images/SVG/Back.svg", zoomfactor * 2);
            Pane nachziehstapel = new Pane();
            for (int i = 0; i < Main.tisch.getNachziehStapelSize(); i++) {
                ImageView imgView = new ImageView(imageback);
                imgView.setY(i * 0.2);
                imgView.setX(i * 0.2);

                if (i == Main.tisch.getNachziehStapelSize() - 1) {
                    imgView.setOnMouseClicked(mouseEvent -> {
                        if (Main.playMode == 2) {
                            try {
                                server.karteNachziehen(tisch.getSpielerList()[ich]);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            System.out.println("\t Ziehe Karte");
                        } else if (Main.spiellogik.karteNachziehen(tisch.getSpielerList()[ich]))
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
            ablagestapel = new Pane();

            for (int i = 0; i < Main.tisch.getAblageStapelSize(); i++) {
                ImageView imgView = new ImageView(Main.cardsArray[Main.tisch.ablageStapel.stapel.get(i).getValue() - 1]);
                if (i >= x.size()) {
                    y.add(i, Math.random() * 3);
                    x.add(i, 15 - 30 * Math.random());
                    deg.add(i, 15 - 30 * Math.random());
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
            if(tutorialAn){//Tutorialansicht
                gridPane.add(makepanel(0), 1, 4, 3, 1);

                Node player1 = makepanel(1);
                player1.setRotate(90);
                gridPane.add(player1, 0, 2, 1, 1);

                Node player2 = makepanel(2);
                player2.setRotate(-90);
                gridPane.add(player2, 4, 2, 2, 1);


            }else {//normale Ansicht
                gridPane.add(makepanel(ich), 1, 4, 3, 1);

                Node player1 = makepanel((1 + ich) % anzSpieler);
                player1.setRotate(90);
                gridPane.add(player1, 0, 2, 1, 1);

                if (Main.anzSpieler > 2) {
                    Node player2 = makepanel((2 + ich) % anzSpieler);
                    gridPane.add(player2, 0, 0, 2, 1);
                    player2.setRotate(155);
                }

                if (Main.anzSpieler > 3) {
                    Node player3 = makepanel((3 + ich) % anzSpieler);
                    player3.setRotate(180);
                    gridPane.add(player3, 2, 0, 1, 1);
                }

                if (Main.anzSpieler > 4) {
                    Node player4 = makepanel((4 + ich) % anzSpieler);
                    player4.setRotate(205);
                    gridPane.add(player4, 3, 0, 2, 1);
                }

                if (Main.anzSpieler > 5) {
                    Node player5 = makepanel((5 + ich) % anzSpieler);
                    player5.setRotate(-90);
                    gridPane.add(player5, 4, 2, 2, 1);
                }
            }


            //Scoreboard
            GridPane score = new GridPane();
            score.setTranslateY(10);
            score.setTranslateX(-5 * zoomfactor);
            score.setMaxWidth(90 * zoomfactor);
            VBox names = new VBox();
            VBox sc = new VBox();
            names.setAlignment(Pos.TOP_LEFT);
            sc.setAlignment(Pos.TOP_RIGHT);
            Map<Spieler, Integer> rangliste;
            if (Main.server == null) {
                rangliste = Main.spiellogik.ranglisteErstellen();
            } else rangliste = server.getRangliste();

            for (Map.Entry<Spieler, Integer> entry : rangliste.entrySet()) {
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


            if (playMode > 0) {
                Button chatButton = new Button(chatOpened ? "Chat <<" : "Chat >>");
                chatButton.setTranslateX(30 * zoomfactor);
                chatButton.setTranslateY(40 * zoomfactor);
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
                chatButton.setPrefHeight(15 * zoomfactor);
                chatButton.setPrefWidth(80 * zoomfactor);
                chatButton.setStyle(
                        "-fx-text-fill: black;\n" +
                                "    -fx-background-color: rgba(255,255,255,0.4);\n" +
                                "    -fx-font-size: 100%;\n" +
                                "    -fx-alignment: center;"
                );

                chatbox.reposition(classPrimaryStage);
                chatbox.scaleTransparenz();
                gridPane.add(chatButton, 0, 4, 1, 1);

            }

            VBox options = new VBox();
            //Einstellungen
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

            //Spiel verlassen
            //if (playerId == Main.ich) {
            ImageView beenden = new ImageView(Main.loader.getImg("GUI/images/exit.svg", zoomfactor * 0.25));
            //beenden.setTranslateY(-5);
            //beenden.setTranslateX(125 * zoomfactor);
            //bottom.setViewOrder(0.0);

            beenden.setOnMouseEntered(e -> beenden.setStyle(HOVERED_BUTTON_STYLE));
            beenden.setOnMouseExited(e -> beenden.setStyle(IDLE_BUTTON_STYLE));
            //bottom.getChildren().add(beenden);


            beenden.setOnMouseClicked(mouseEvent -> {
                if (Main.playMode <= 1) {
                    resizecheck.cancel();
                    Main.gameRunning = false;
                    Main.bots.cancel();
                    Main.joined = false;
                    Main.myTurnUpdate = true;
                    spiellogik = null;
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

                } else if (Main.playMode == 2) {//Multiplaymodus
                    resizecheck.cancel();
                    joined = false;
                    try {
                        Main.server.replaceSpielerDurchBot(uniqueID);
                    } catch (RemoteException e) {
                    }
                    hauptmenuGui.update.cancel();
                    server = null;
                    hauptmenuGui.status = new Label("Server verlassen");

                    Main.hauptmenuGui.showSettingsMenu(Main.classPrimaryStage);

                } else {//lokaler Spielmodus
                    Main.gameRunning = false;
                    Main.bots.cancel();
                    Main.hauptmenuGui.showSettingsMenu(classPrimaryStage);
                }

            });
            options.getChildren().addAll(beenden,settings);
            options.setAlignment(Pos.TOP_RIGHT);
            options.setSpacing(20);
            options.setMaxWidth(35*zoomfactor);
            options.setPadding(new Insets(10));
            //}
            gridPane.add(options, 4, 0, 1, 1);
            gridPane.setValignment(options, VPos.TOP);
            gridPane.setHalignment(options, HPos.RIGHT);


            //Notice wenn man am Zug ist
            if((myTurnNotice  && tisch.aktiv == ich )&& tutorialAn==false) {


                turnNotice = new VBox();
                turnNotice.setMouseTransparent(true);
                turnNotice.setAlignment(Pos.CENTER);
                turnNotice.setStyle("-fx-background-image:url('/GUI/images/oberflaeche.jpg'); -fx-background-insets: 20; " +
                        "-fx-background-radius: 50; " +
                        "-fx-cursor:hand;" +
                        "-fx-effect: dropshadow(three-pass-box, black, 50, 0, 0, 0);");
                Label turnLabel = new Label("- Du bist dran -");
                turnLabel.setStyle("-fx-effect: dropshadow( gaussian , black ,10 ,0.7 ,0 ,0 ); -fx-font-weight: bolder");
                turnLabel.setTextFill(Color.LIGHTGREEN);
                turnLabel.setFont(Font.font("Ink Free", FontWeight.BOLD, 25 * zoomfactor));
                turnNotice.getChildren().add(turnLabel);

                FadeTransition ft = new FadeTransition(Duration.millis(6000), turnNotice);
                ft.setFromValue(1.0);
                ft.setToValue(0);
                ft.play();
                table.add(turnNotice, 0, 0, 3, 1);
                myTurnNotice = false;
            }





            setGridpane(gridPane);
            root.getChildren().add(gridPane);
            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an

            scene.getStylesheets().add("GUI/Chat.css");
            //scene.getStylesheets().add("GUI/einstellung.css");


        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        primaryStage.setWidth(primaryStage.getWidth()+sceneWidth-primaryStage.getScene().getWidth());
        primaryStage.setHeight(primaryStage.getHeight()+sceneHeight-primaryStage.getScene().getHeight());

        primaryStage.show();
    }

    /**
     * @param playerId Index der Spielerliste am Tisch
     * @param karte    Wird ausgeführt wenn ein Spieler auf einer seiner Karten klickt bzw
     *                 diese auf dem Ablagestapel droppt.
     */

    void kartelegen(int playerId, int karte) {
        //Multiplayermodus

        if (Main.playMode == 2) {
            try {
                server.karteLegen(server.updateTisch().getSpielerList()[playerId],
                        server.updateTisch().getSpielerList()[playerId].getCardHand().getKarte(karte));

                System.out.println("karte abgelegt");

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        } else {//lokaler Spielmodus
            Main.spiellogik.karteLegen(tisch.getSpielerList()[playerId],
                    tisch.getSpielerList()[playerId].getCardHand().getKarte(karte));
        }


        if (playMode == 2) {
            try {
                if (server.updateTisch().getSpielerList()[playerId].getCardHand().getHandKarte().isEmpty()) {
                    if (!tisch.getSpielerList()[playerId].getCardHand().getHandKarte().isEmpty()) {
                        tisch.getSpielerList()[playerId].getCardHand().removeKarte(tisch.getSpielerList()[playerId].getCardHand().getKarte(0));
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (tisch.getSpielerList()[playerId].getCardHand().getHandKarte().isEmpty() && (tisch.getSpielerList()[playerId].getBlackChips() > 0 || tisch.getSpielerList()[playerId].getWhiteChips() > 0)) {
            Chip tausch = null;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("     ");
            alert.initStyle(StageStyle.TRANSPARENT);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("GUI/alert.css");

            ButtonType buttonTypeWhite = new ButtonType("weiß");
            ButtonType buttonTypeBlack = new ButtonType("schwarz");
            ButtonType buttonTypeCancel = new ButtonType("schließen", ButtonBar.ButtonData.CANCEL_CLOSE);

            if(autochips) {
                if(tisch.getSpielerList()[playerId].getBlackChips() >= 1) {
                    tausch = new BlackChip();

                } else if(tisch.getSpielerList()[playerId].getWhiteChips() >= 1) {
                    tausch = new WhiteChip();
                }
                if (Main.playMode == 2) {  //Multiplayermodus
                    try {
                        server.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else { //lokaler Spielmodus
                    Main.spiellogik.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                }

            } else {
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
                    //alert.setHeaderText("Du hast keine Chips zum abgeben");
                    alert.getButtonTypes().setAll(buttonTypeCancel);
                }

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeWhite) {
                    // ... user chose "weiß"
                    tausch = new WhiteChip();
                    if (Main.playMode == 2) { //Multiplayermodus
                        try {
                            server.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {//lokaler Spielmodus
                        Main.spiellogik.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                    }
                } else if (result.get() == buttonTypeBlack) {
                    // ... user chose "schwarz"
                    tausch = new BlackChip();
                    if (Main.playMode == 2) {  //Multiplayermodus
                        try {
                            server.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else { //lokaler Spielmodus
                        Main.spiellogik.chipAbgeben(tisch.getSpielerList()[playerId], tausch);
                    }

                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        }

        buildStage(Main.classPrimaryStage);

    }


    /** positioniert den Chat und die Einstellung relativ zum Window
     * @param primaryStage
     */
    public void reposition(Stage primaryStage){
        if(chatbox != null && chatbox.chat != null){
            chatbox.reposition(primaryStage);
        }

        if(einstellung != null && einstellung.getSettings() != null){
            einstellung.reposition(primaryStage);
        }


    }
    private void setGridpane(GridPane p){
        spielfeld=p;
    }
    public GridPane getGridPane() {
        return spielfeld;
    }
}