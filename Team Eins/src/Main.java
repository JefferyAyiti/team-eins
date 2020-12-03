import SVG.SvgImageLoaderFactory;
import SVG.TestLoadImageUsingClass;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.*;


public class Main extends Application {


    static Hand[] haende;
    static Spieler[] spieler;
    static TestLoadImageUsingClass loader;
    static double sceneWidth = 0;
    static double sceneHeight = 0;
    static Tisch tisch;
    static Spiellogik spiellogik;
    double zoomfactor = 1;
    volatile long resize = 0;
    int ich = 0;
    long botPlayTime = 2000;
    private static int botlevel = 0;
    Stage classPrimaryStage;

    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE =
            "-fx-background-insets: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-cursor:hand;" +
                    "-fx-effect: dropshadow(three-pass-box, yellow, 10, 0, 0, 0);";

    //Wird später im Menü festgelegt
    private static int anzSpieler = 6;//2 + (int) (Math.random() * 4);


    public static void main(String[] args) {
        loader = new TestLoadImageUsingClass();
        loader.installSvgLoader();
        try {
            initGame();

        } catch (Exception e) {

        }
        launch(args);

    }

    private static Image image;
    private static Image card1;
    private static Image card3;
    private static Image card4;
    private static Image card2;
    private static Image card5;
    private static Image card6;
    private static Image lama;
    private static Image table1;


    Image[] cardsArray;

    private static Image blackChipImage;
    private static Image whiteChipImage;

    /**
     * Erstellt Tisch, Nachzieh- und Abalgestapel, Spieler
     * und startet die erste Runde im Spiel
     */
    private static void initGame() {

        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        //spieler[0]= new Bot("Spieler",2);
        spieler[0] = new Spieler(myName);
        int level;
        String[] botname = {"EZ-", "Mid-", "Hard-"};
        for (int i = 1; i < anzSpieler; i++) {
            level = botlevel == 0 ? (int) (Math.random() * 3 + 1) : botlevel;
            System.out.println(level);
            spieler[i] = new Bot(botname[level - 1] + "Bot " + (i + 1), level);

        }
        tisch = new Tisch(spieler);
        spiellogik = new Spiellogik(tisch);
        spiellogik.initNeueRunde();


    }


    //GUI


    public void resize() {

        if (System.currentTimeMillis() < resize + 500) {
            getZoomedImages();
            buildStage(classPrimaryStage);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");

        image = new Image("images/SVG/Back.svg");
        card1 = new Image("images/SVG/Card1.svg");
        card3 = new Image("images/SVG/Card3.svg");
        card4 = new Image("images/SVG/Card4.svg");
        card2 = new Image("images/SVG/Card2.svg");
        card5 = new Image("images/SVG/Card5.svg");
        card6 = new Image("images/SVG/Card6.svg");
        lama = new Image("images/SVG/Lama.svg");
        table1 = new Image("images/table2.svg");
        blackChipImage = new Image("/images/SVG/blackChip.svg");
        whiteChipImage = new Image("/images/SVG/whiteChip.svg");
        cardsArray = new Image[]{card1, card2, card3, card4, card5,
                card6, null, null, null, lama};

        classPrimaryStage = primaryStage;
        showSettingsMenu(classPrimaryStage);


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

        Label plr = new Label(spieler[playerId].getName());
        if (playerId > 1 && playerId < 5) {
            plr.setRotate(180);
        }
        plr.setTextFill(Color.WHITE);
        plr.setFont(Font.font("Verdana", 12 * zoomfactor));
        if (spieler[playerId] == tisch.getAktivSpieler()) {
            plr.setTextFill(Color.YELLOW);
            plr.setFont(Font.font("Verdana", FontWeight.BOLD, 14 * zoomfactor));
        }
        plr.setTranslateY(-15);
        pane.getChildren().add(plr);

        int cardcount = spieler[playerId].getCardCount();
        if (cardcount > 7 && playerId != 0) {
            plr.setText(spieler[playerId].getName() + " (" + cardcount + ")");
            cardcount = 7;
        }


        ColorAdjust desaturate = new ColorAdjust();
        desaturate.setSaturation(-1);


        StackPane cards = new StackPane();
        cards.setAlignment(Pos.BASELINE_CENTER);
        //cards.setStyle("-fx-background-color:#cccccc;");

        //verdeckte Karten


        for (int i = 0; i < cardcount; i++) {
            ImageView imgView = new ImageView(image);

            if (playerId == 0) {
                imgView = new ImageView(
                        cardsArray[spieler[playerId].getCardHand().getKarte(i).getValue() - 1]);
                ImageView finalImgView = imgView;
                imgView.setOnMouseEntered(e -> finalImgView.setStyle(HOVERED_BUTTON_STYLE));
                ImageView finalImgView1 = imgView;
                imgView.setOnMouseExited(e -> finalImgView1.setStyle(IDLE_BUTTON_STYLE));
            } else if (i > 6)
                continue;


            imgView.setPreserveRatio(true);
            imgView.setSmooth(true); //Visuelle Große der Handkarte ändern
            imgView.setFitWidth(playerId == 0 ? 80 * zoomfactor : 50 * zoomfactor);


            if (playerId != 0) {
                imgView.setTranslateX(-cardcount / 2 * 10 + 10 * i);
                imgView.setTranslateY(-10);
                imgView.setRotate(-cardcount / 2 * 15 + i * 15);
            } else {
                int finalI = i;
                imgView.setOnMouseClicked(mouseEvent -> {
                    spiellogik.karteLegen(spieler[playerId],
                            spieler[playerId].getCardHand().getKarte(finalI));
                    //Chip tausch
                    if (spieler[playerId].getCardHand().getHandKarte().isEmpty()) {
                        Chip tausch;
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Glückwunsch");
                        alert.setHeaderText("Willst du einen Chip abgeben?");

                        ButtonType buttonTypeWhite = new ButtonType("weiß");
                        ButtonType buttonTypeBlack = new ButtonType("schwarz");
                        ButtonType buttonTypeCancel = new ButtonType("schließen", ButtonBar.ButtonData.CANCEL_CLOSE);

                        //spieler hat weiße und Schwarze Chips
                        if (spieler[playerId].getWhiteChips() >= 1 && spieler[playerId].getBlackChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeBlack, buttonTypeCancel);
                            //nur weiße
                        } else if (spieler[playerId].getWhiteChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeCancel);
                            //nur schwarze
                        } else if (spieler[playerId].getBlackChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeBlack, buttonTypeCancel);
                        } else {
                            alert.setHeaderText("Du hast keine Chips zum abgeben");
                            alert.getButtonTypes().setAll(buttonTypeCancel);
                        }

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeWhite) {
                            // ... user chose "weiß"
                            tausch = new WhiteChip();
                            spiellogik.chipAbgeben(spieler[playerId], tausch);
                        } else if (result.get() == buttonTypeBlack) {
                            // ... user chose "schwarz"
                            tausch = new BlackChip();
                            spiellogik.chipAbgeben(spieler[playerId], tausch);
                        } else {
                            // ... user chose CANCEL or closed the dialog
                        }

                    }
                    buildStage(classPrimaryStage);


                });
            }
            if (playerId == 0) {
                if (cardcount > 7) {
                    imgView.setTranslateX(10 * (cardcount % 2) + cardcount / 2 * 20 * zoomfactor - 20 * zoomfactor * i);
                } else {
                    imgView.setTranslateX((cardcount % 2 == 0 ? 55 / 2 * zoomfactor : 0) + 55 * zoomfactor * (i - cardcount / 2));
                }
            }

            if (!spieler[playerId].inGame())
                imgView.setEffect(desaturate);
            cards.getChildren().add(imgView);
        }


       /* if (playerId != 0) {
            cards.setTranslateX(+(min(6, spieler[playerId].getCardCount()) * 30) / 2 - 20);
        }*/
        int chipsize = 15;
        pane.getChildren().add(cards);
        GridPane chips = new GridPane();
        //chips.setMaxWidth(60 * zoomfactor);
        ImageView blChip = new ImageView(blackChipImage);
        blChip.setFitHeight(chipsize * zoomfactor);
        blChip.setFitWidth(chipsize * zoomfactor);
        //Chip counter unter die Karte

        ImageView whChip = new ImageView(whiteChipImage);
        whChip.setFitHeight(chipsize * zoomfactor);
        whChip.setFitWidth(chipsize * zoomfactor);

        if (ich == playerId) {
            chips.setOnMouseClicked(mouseEvent -> {
                spiellogik.chipsTauschen(spieler[playerId]);
                buildStage(classPrimaryStage);
            });


            chips.setOnMouseEntered(e -> chips.setStyle(HOVERED_BUTTON_STYLE));
            chips.setOnMouseExited(e -> chips.setStyle(IDLE_BUTTON_STYLE));
        }

        chips.add(blChip, 0, 0);
        chips.add(whChip, 0, 1);

        Text text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText("" + spieler[playerId].getBlackChips());
        chips.add(text, 1, 0);

        text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText(spieler[playerId].getWhiteChips() + "");

        chips.add(text, 1, 1);
        //chips.setY(102);


        HBox bottom = new HBox(chips);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(30);

        //Aussteigen
        if (playerId == ich) {

            ImageView exit = new ImageView(loader.getImg("images/exit.svg", zoomfactor * 0.4));
            exit.setTranslateY(1);
            chips.setTranslateY(1);
            if (playerId > 1 && playerId < 5) {

            }

            exit.setOnMouseEntered(e -> exit.setStyle(HOVERED_BUTTON_STYLE));
            exit.setOnMouseExited(e -> exit.setStyle(IDLE_BUTTON_STYLE));
            bottom.getChildren().add(exit);

            exit.setOnMouseClicked(mouseEvent -> {
                spiellogik.aussteigen(spieler[playerId]);
                buildStage(classPrimaryStage);
            });

        }

        pane.getChildren().add(bottom);

        switch (playerId) {
            case 1:
            case 5:
                pane.setTranslateY(+30 * zoomfactor);
                break;
            case 4:
                pane.setTranslateX(30 * zoomfactor);
                pane.setTranslateY(10 + 10 * zoomfactor);
                break;
            case 3:
                pane.setTranslateY(10 * zoomfactor);
                break;
            case 2:
                pane.setTranslateX(-30 * zoomfactor);
                pane.setTranslateY(10 + 10 * zoomfactor);
                break;
        }
        return pane;

    }


    /**
     * Bildet die Stage (neu), sodass Änderungen im Spiel dargestellt werden
     *
     * @param primaryStage
     */
    private void buildStage(Stage primaryStage) {
        try {
            StackPane root = new StackPane();
            classPrimaryStage = primaryStage;

            if (sceneWidth == 0) {
                sceneWidth = 600;
            } else
                sceneWidth = classPrimaryStage.getScene().getWidth();
            if (sceneHeight == 0) {
                sceneHeight = 400;
            } else
                sceneHeight = classPrimaryStage.getScene().getHeight();

            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            BackgroundImage myBI = new BackgroundImage(table1,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, false, true));


            GridPane table = new GridPane();
            //Nachziehstapel
            Pane nachziehstapel = new Pane();
            for (int i = 0; i < tisch.getNachziehStapelSize(); i++) {
                ImageView imgView = new ImageView(image);
                imgView.setY(i * 0.2);
                imgView.setX(i * 0.2);

                if (i == tisch.getNachziehStapelSize()-1) {
                    imgView.setOnMouseClicked(mouseEvent -> {
                        spiellogik.karteNachziehen(spieler[0]);
                        buildStage(classPrimaryStage);
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
            for (int i = 0; i < tisch.getAblageStapelSize(); i++) {
                ImageView imgView = new ImageView(cardsArray[tisch.getObereKarteAblagestapel().getValue() - 1]);
                imgView.setY(i * 0.3);
                imgView.setX(i * 0.2);
                imgView.setFitWidth(90 * zoomfactor); //Visuelle Große der Ablagestapel ändern
                imgView.setPreserveRatio(true);
                imgView.setSmooth(true);
                ablagestapel.getChildren().add(imgView);
            }
            table.add(ablagestapel, 2, 0, 1, 1);

            GridPane chipsgrid = new GridPane();
            chipsgrid.setAlignment(Pos.CENTER);

            ImageView imgView = new ImageView(blackChipImage);
            imgView.setFitHeight(25 * zoomfactor);
            imgView.setFitWidth(25 * zoomfactor);
            chipsgrid.add(imgView, 0, 0, 1, 1);
            Text ChipText = new Text();
            ChipText.setText(" " + tisch.getBlackChips());
            ChipText.setFont(Font.font("Verdana", 20 * zoomfactor));
            ChipText.setFill(Color.WHITE);
            chipsgrid.add(ChipText, 1, 0, 1, 1);

            imgView = new ImageView(whiteChipImage);
            imgView.setFitHeight(25 * zoomfactor);
            imgView.setFitWidth(25 * zoomfactor);
            chipsgrid.add(imgView, 0, 1, 1, 1);
            ChipText = new Text();
            ChipText.setText(" " + tisch.getWhiteChips());
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

            gridPane.add(makepanel(0), 1, 4, 3, 1);

            Node player1 = makepanel(1);
            player1.setRotate(90);
            gridPane.add(player1, 0, 2, 1, 1);

            if (anzSpieler > 2) {
                Node player2 = makepanel(2);
                gridPane.add(player2, 0, 0, 2, 1);
                player2.setRotate(155);
            }

            if (anzSpieler > 3) {
                Node player3 = makepanel(3);
                player3.setRotate(180);
                gridPane.add(player3, 2, 0, 1, 1);
            }

            if (anzSpieler > 4) {
                Node player4 = makepanel(4);
                player4.setRotate(205);
                gridPane.add(player4, 3, 0, 2, 1);
            }

            if (anzSpieler > 5) {
                Node player5 = makepanel(5);
                player5.setRotate(-90);
                gridPane.add(player5, 4, 2, 2, 1);
            }


            //Scoreboard
            GridPane score = new GridPane();
            score.setTranslateY(10);
            score.setTranslateX(-10);
            VBox names = new VBox();
            VBox sc = new VBox();
            names.setAlignment(Pos.TOP_LEFT);
            sc.setAlignment(Pos.TOP_RIGHT);
            for (Map.Entry<Spieler, Integer> entry : spiellogik.ranglisteErstellen().entrySet()) {
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

            root.getChildren().add(gridPane);


            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an
            primaryStage.setScene(scene);
            if (spiellogik.getRundeBeendet()) {
                showRangliste(spiellogik.ranglisteErstellen());

            }
            sceneWidth = scene.getWidth();
            sceneHeight = scene.getHeight();
            lastmove = System.currentTimeMillis();
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt die SVG-Grafiken mit einem Zoom-Faktor neu, sodass
     * diese immer scharf aussehen
     */
    void getZoomedImages() {


        double factor;
        if (sceneWidth / sceneHeight > 1.5) {
            factor = sceneHeight / 400;
        } else
            factor = sceneWidth / 600;


        factor = Double.min(factor, 2);
        zoomfactor = factor;
        //System.out.println(factor);

        image = loader.getImg("images/SVG/Back.svg", factor);
        card1 = loader.getImg("images/SVG/Card1.svg", factor);
        card2 = loader.getImg("images/SVG/Card2.svg", factor);
        card3 = loader.getImg("images/SVG/Card3.svg", factor);
        card4 = loader.getImg("images/SVG/Card4.svg", factor);
        card5 = loader.getImg("images/SVG/Card5.svg", factor);
        card6 = loader.getImg("images/SVG/Card6.svg", factor);
        lama = loader.getImg("images/SVG/Lama.svg", factor);
        table1 = loader.getImg("images/table2.svg", factor);
    }


    Scene showRangliste(Map<Spieler, Integer> ranking) throws InterruptedException {
        System.out.println("gibt Rangliste aus");
        int p = 1;

        //Scoreboard
        VBox names = new VBox(new Label(""));
        VBox score = new VBox(new Label(""));
        VBox differ = new VBox(new Label(""));
        VBox platz = new VBox(new Label(""));

        GridPane center = new GridPane();

        for (Map.Entry<Spieler, Integer> entry : spiellogik.ranglisteErstellen().entrySet()) {
            //Platz
            Label rang = new Label("\t" + p + ". Platz: " + "\t");
            rang.setFont(new Font("Ink Free", 18 * zoomfactor));
            rang.setTextFill(Color.WHITE);
            platz.getChildren().add(rang);
            //Spieler
            Label name = new Label(entry.getKey().getName() + "\t");
            name.setTextFill(Color.WHITE);
            name.setFont(new Font("Ink Free", 18 * zoomfactor));
            names.getChildren().add(name);
            //Punktestand
            int dif = entry.getKey().getPoints() - entry.getKey().getOldScore();


            Label kassiert;
            if (dif < 0) {
                kassiert = new Label(Integer.toString(dif) + "\t");
                kassiert.setTextFill(Color.RED);
            } else {
                kassiert = new Label("+" + Integer.toString(dif) + "\t");
                kassiert.setTextFill(Color.LIGHTGREEN);

            }
            kassiert.setFont(new Font("Ink Free", 18 * zoomfactor));
            kassiert.setStyle("-fx-font-weight: bold");


            differ.getChildren().add(kassiert);

            Label sco = new Label(Integer.toString(entry.getValue()));
            sco.setFont(new Font("Ink Free", 18 * zoomfactor));
            sco.setTextFill(Color.WHITE);
            score.getChildren().add(sco);

            p++;

            System.out.println(entry.getKey().getName() + ":  -  alt:" + entry.getKey().getOldScore() + "   neu:" + entry.getKey().getPoints() + "   dif:" + dif);
        }

        center.addRow(0, platz, names, score, differ);
        center.setHgap(30 * zoomfactor);
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('images/oberflaeche.jpg')");
        center.setMinHeight(250 * zoomfactor);
        center.setMinWidth(200 * zoomfactor);

        HBox bottom = new HBox();
        bottom.setSpacing(15);
        bottom.setMinHeight(sceneHeight / 8);
        bottom.setAlignment(Pos.CENTER);

        Button nextRound;
        if (!spiellogik.spielBeendet) {
            nextRound = new Button("Nächste Runde");
            nextRound.setOnAction(e -> {
                        spiellogik.initNeueRunde();
                        buildStage(classPrimaryStage);
                    }
            );

        } else {
            Button endGame = new Button("Spiel beenden");
            endGame.setOnAction(e -> {
                        classPrimaryStage.close();
                        bots.cancel();
                        resizecheck.cancel();
                    }
            );
            bottom.getChildren().add(endGame);

            nextRound = new Button("Hauptmenü");
            nextRound.setOnAction(e -> {
                        showSettingsMenu(classPrimaryStage);

                    }
            );
        }

        bottom.getChildren().add(nextRound);

        //Darstellung
        Label titel = new Label("Rangliste");
        titel.setFont(new Font("Script MT Bold", 50 * zoomfactor));
        titel.setTextFill(Color.WHITE);

        HBox top = new HBox(titel);
        top.setMinHeight(sceneHeight / 8);
        top.setAlignment(Pos.CENTER);



        VBox left = new VBox();
        left.setMinWidth(sceneWidth / 7);
        left.setAlignment(Pos.TOP_LEFT);

        VBox right = new VBox();
        right.setMinWidth(sceneWidth / 7);
        right.setAlignment(Pos.TOP_RIGHT);

        center.setAlignment(Pos.TOP_LEFT);
        center.setMaxHeight(center.getHeight());
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(center);
        root.setRight(right);
        root.setBottom(bottom);
        root.setLeft(left);


        //Hintergrund
         /*BackgroundImage myBI2 = new BackgroundImage(score,
                 BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                 new BackgroundSize(100, 100, true, true, false, true));
         liste.setBackground(new Background(myBI2));
        */

        BackgroundImage myBI = new BackgroundImage(table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        String css = Main.class.getResource("Rangliste.css").toExternalForm();
        Scene rangliste = new Scene(root, sceneWidth, sceneHeight);
        rangliste.getStylesheets().add(css);

        classPrimaryStage.setScene(rangliste);

        return rangliste;
    }

    static String myName;

    /**
     * @param PrimaryStage Erzeug und zeit das Hauptmenü zu Beginn des Spiels an
     */
    void showSettingsMenu(Stage PrimaryStage) {
        GridPane center = new GridPane();
        center.setVgap(10);

        //Spielername
        TextField namefield = new TextField(myName);
        //namefield.setStyle("-fx-background-color:rgba(255,255,255,0.3);");
        center.addRow(0, new Label("Spielername: "), namefield);

        //Spieleranzahl
        ObservableList<Integer> ploptions =
                FXCollections.observableArrayList(
                        2,
                        3,
                        4,
                        5,
                        6
                );
        ComboBox playeranzselect = new ComboBox(ploptions);
        playeranzselect.getSelectionModel().select(4);
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
        ComboBox botselect = new ComboBox(botoptions);
        botselect.getSelectionModel().select(0);
        center.addRow(2, new Label("Bot-Schwierigkeit: "), botselect);

        center.setHgap(60 * zoomfactor);
        center.setId("MMcenter");
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('images/oberflaeche.jpg')");
        center.setMinHeight(250 * zoomfactor);
        center.setMinWidth(200 * zoomfactor);

        //Geschwindigkeit
        Slider slider = new Slider();
        slider.setMin(500);
        slider.setMax(5000);
        slider.setValue((slider.getMax() - slider.getMin()) / 2 + slider.getMin());
        System.out.println(slider.getValue());
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        slider.setMinorTickCount(1000);
        slider.setMajorTickUnit(1000);
        slider.setBlockIncrement(10);
        slider.setPrefSize(150, 5);

        center.addRow(3, new Label("Bot-Bedenkzeit: "), slider);


        //Darstellung
        Label titel = new Label("Hauptmenü");
        titel.setTextFill(Color.WHITE);
        titel.setFont(new Font("Script MT Bold", 36 * zoomfactor));

        HBox top = new HBox(titel);
        top.setMinHeight(sceneHeight / 8);
        top.setAlignment(Pos.CENTER);

        Button start = new Button("Spiel starten");
        start.setTranslateY(-10);
        start.setOnAction(e -> {
                    botPlayTime = (long) slider.getValue();
                    botlevel = botselect.getSelectionModel().getSelectedIndex();
                    myName = namefield.getText();
                    if (myName == null || myName.equals("")) myName = "Spieler";
                    anzSpieler = (int) playeranzselect.getValue();
                    initGame();
                    sceneWidth = 600;
                    sceneHeight = 400;
                    runTimers(PrimaryStage);
                    getZoomedImages();
                    buildStage(PrimaryStage);
                }
        );

        HBox bottom = new HBox(start);
        bottom.setMinHeight(sceneHeight / 8);
        bottom.setAlignment(Pos.CENTER);

        center.setAlignment(Pos.TOP_LEFT);
        center.setMaxHeight(center.getHeight());
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);


        BackgroundImage myBI = new BackgroundImage(table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        String css = Main.class.getResource("Rangliste.css").toExternalForm();
        Scene menu = new Scene(root, 600, 400);
        menu.getStylesheets().add(css);

        PrimaryStage.setScene(menu);
        PrimaryStage.show();
    }


    class MyTask1 extends TimerTask {
        @Override
        public void run() {
            sceneWidth = classPrimaryStage.getScene().getWidth();
            sceneHeight = classPrimaryStage.getScene().getHeight();
            Platform.runLater(() -> {
                resize();
            });

        }
    }

    long lastmove = 0;

    class moveCheck extends TimerTask {
        @Override
        public void run() {
            if (tisch.getAktivSpieler() instanceof Bot && !spiellogik.getRundeBeendet()) {
                if (System.currentTimeMillis() - lastmove < botPlayTime) {
                    try {
                        Thread.sleep(botPlayTime - (System.currentTimeMillis() - lastmove));
                    } catch (InterruptedException e) {
                    }
                }
                ((Bot) tisch.getAktivSpieler()).play();
                Platform.runLater(() -> {
                    buildStage(classPrimaryStage);
                });
                lastmove = System.currentTimeMillis();


            }
        }
    }

    /**
     * Startet die Timer für Bots und Resize-Check, sobald das Hauptmenü verlassen wurde
     *
     * @param ps Stage
     */
    Timer bots;
    Timer resizecheck;

    void runTimers(Stage ps) {
        resizecheck = new Timer();
        resizecheck.schedule(new MyTask1(), 3000, 500);

        bots = new Timer();
        bots.schedule(new moveCheck(), botPlayTime * 2, botPlayTime);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            resize = System.currentTimeMillis();

        };


        ps.widthProperty().addListener(stageSizeListener);
        ps.heightProperty().addListener(stageSizeListener);

        ps.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                bots.cancel();
                resizecheck.cancel();
            }
        });
    }

}
