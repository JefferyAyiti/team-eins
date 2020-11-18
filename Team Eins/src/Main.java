import SVG.TestLoadImageUsingClass;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application{

    static Hand[] haende;
    static Spieler[] spieler;
    static TestLoadImageUsingClass loader;
    static double sceneWidth = 0;
    static double sceneHeight = 0;
    static  Tisch tisch;
    static Spiellogik spiellogik;

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

    /**
     *
     * Erstellt Stapel, Spieler und deren initialen handkarten
     * @throws Exception
     */
    private static void initGame() throws Exception {


        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        for (int i = 0; i < anzSpieler; i++) {
            haende[i] = new Hand();
            spieler[i] = new Spieler(haende[i], "Spieler " + (i + 1));

        }
        tisch = new Tisch(spieler);
        spiellogik = new Spiellogik(tisch);
        tisch.initNachziehstapel();
        tisch.mischenNachziehstapel();

        //gebe jeden Spieler (anzSpieler) 6 Karten in Reihenfolge
        for (int i = 0; i < 6; i++) {
            for (int s = 0; s < anzSpieler; s++) {
                haende[s].addKarte(tisch.karteZiehen());
            }

        }

        tisch.karteAblegen(tisch.karteZiehen()); //Ablagestapel
    }


    //GUI

    Image image = loader.getImg("images/SVG/Back.svg");
    Image card7 = loader.getImg("images/SVG/Card7.svg");
    Image card2 = loader.getImg("images/SVG/Card2.svg");
    Image card3 = loader.getImg("images/SVG/Card3.svg");
    Image card4 = loader.getImg("images/SVG/Card4.svg");
    Image card5 = loader.getImg("images/SVG/Card5.svg");
    Image card6 = loader.getImg("images/SVG/Card6.svg");
    Image lama = loader.getImg("images/SVG/Lama.svg");
    Image table1 = loader.getImg("images/table2.svg");

    Image[] cardsArray = {card2, card3, card4, card5, card6, card7, null, null, null, lama};

    Image whiteChipImage = new Image("/images/chips/white.png");
    Image blackChipImage = new Image("/images/chips/black.png");


    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");
        buildStage(primaryStage);


    }


    /**
     * Erstellt die Fläche auf dem Tisch für einen einzelnen Spieler,
     * d.h. Karten, Chips, Name etc
     * @param playerId
     * @return Gibt ein Panel zurück
     */
    private Pane makepanel(int playerId) {


        VBox pane = new VBox();
        pane.setAlignment(Pos.TOP_CENTER);

        Label plr = new Label(spieler[playerId].getName());
        plr.setTextFill(Color.WHITE);
        plr.setTranslateY(-5);
        pane.getChildren().add(plr);

        Pane cards = new HBox();

        //verdeckte Karten

        for (int i = 0; i < spieler[playerId].getCardCount(); i++) {
            ImageView imgView = new ImageView(image);
            if (playerId == 0) {
                imgView = new ImageView(
                        cardsArray[spieler[playerId].getCardHand().getKarte(i).getValue() - 1]);
            }


            //imgView.setTranslateX(left + i * 60);
            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);
            imgView.setFitWidth(playerId==0?55:40);


            if(playerId!=0) {
                imgView.setTranslateX(-30*i);
                imgView.setRotate(-spieler[playerId].getCardCount()/2*10+i*10);
            } else {
                int finalI = i;
                imgView.setOnMouseClicked(mouseEvent -> {
                    spiellogik.karteLegen(spieler[playerId],
                            spieler[playerId].getCardHand().getKarte(finalI) );
                    buildStage(classPrimaryStage);
                });
            }
            cards.getChildren().add(imgView);
        }


        if(playerId!=0) {
            cards.setTranslateX(+(spieler[playerId].getCardCount()*30)/2-20);
        }
        pane.getChildren().add(cards);
        Pane chips = new VBox();
        chips.setMaxWidth(60);
        chips.setStyle("-fx-background-color:rgba(255,255,255,0.5); -fx-margin-top:5px;");
        Text text = new Text();

        text.setText("⚪: " + spieler[playerId].getWhiteChips() +
                "    ⚫: " + spieler[playerId].getBlackChips());
        //chips.setY(102);
        chips.getChildren().add(text);
        pane.getChildren().add(chips);

        return pane;
    }

    Stage classPrimaryStage;


    EventHandler event_ablage = new EventHandler<>() {
        @Override
        public void handle(Event mouseEvent) {
            //was passiert beim Klick auf den Ablagestapel

            buildStage(classPrimaryStage);
        }};


    /**
     * Bildet die Stage (neu), sodass Änderungen im Spiel dargestellt werden
     * @param primaryStage
     */
    private void buildStage(Stage primaryStage) {
        try {
            StackPane root = new StackPane();
            classPrimaryStage = primaryStage;

            if(sceneWidth == 0) {
                sceneWidth = 600;
            }
            else
                sceneWidth = classPrimaryStage.getScene().getWidth();
            if(sceneHeight == 0) {
                sceneHeight = 400;
            } else
                sceneHeight = classPrimaryStage.getScene().getHeight();

            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            BackgroundImage myBI = new BackgroundImage(table1,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, false, true));



            GridPane table = new GridPane();
            //karten auf dem Tisch
            Pane nachziehstapel = new Pane();
            for (int i = 0; i < tisch.getAblageStapelSize(); i++) {
                ImageView imgView = new ImageView(image);
                imgView.setY(i * 0.1);
                imgView.setX(i * 0.3);
                imgView.setOnMouseClicked(mouseEvent -> {
                    spiellogik.karteNachziehen(spieler[0]);
                    buildStage(classPrimaryStage);
                });
                imgView.setPreserveRatio(true);
                imgView.setSmooth(true);
                imgView.setFitWidth(60);
                nachziehstapel.getChildren().add(imgView);
            }
            table.add(nachziehstapel, 0,0, 1, 1);

            //Ablagestapel
            ImageView imgView = new ImageView(
                    cardsArray[tisch.getObereKarteAblagestapel().getValue() - 1]);

            //TEST_EVENT:


            imgView.setPreserveRatio(true);
            imgView.setFitWidth(60);
            table.add(imgView, 2,0, 1, 1);

            GridPane chipsgrid = new GridPane();
            chipsgrid.setAlignment(Pos.CENTER);

            imgView = new ImageView(blackChipImage);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            chipsgrid.add(imgView, 0,0,1, 1);
            Text ChipText = new Text();
            ChipText.setText(" "+ tisch.getBlackChips());
            ChipText.setFont(Font.font("Verdana", 20));
            ChipText.setFill(Color.WHITE);
            chipsgrid.add(ChipText, 1,0,1, 1);

            imgView = new ImageView(whiteChipImage);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            chipsgrid.add(imgView, 0,1,1, 1);
            ChipText = new Text();
            ChipText.setText(" "+ tisch.getWhiteChips());
            ChipText.setFont(Font.font("Verdana", 20));
            ChipText.setFill(Color.WHITE);

            chipsgrid.add(ChipText, 1,1,1, 1);


            GridPane gridPane = new GridPane();
            gridPane.setBackground(new Background(myBI));
            gridPane.setGridLinesVisible(false);
            gridPane.setAlignment(Pos.TOP_CENTER);

            table.getColumnConstraints().add(new ColumnConstraints()); // column 0 is 100 wide
            table.getColumnConstraints().add(new ColumnConstraints(80)); // column 0 is 100 wide

            table.add(chipsgrid, 1,0, 1, 1);

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
                switch(i) {
                    case 0: row.setPercentHeight(33); row.setMinHeight(80); break;
                    case 1: case 2: case 3: row.setPercentHeight(40/3); row.setMinHeight(30); break;
                    case 4: row.setPercentHeight(33); row.setMinHeight(150); break;

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

            if(anzSpieler > 2) {
                Node player2 = makepanel(2);
                player2.setRotate(-90);
                gridPane.add(player2, 4, 2, 1, 1);
            }

            if(anzSpieler > 3) {
                Node player3 = makepanel(3);
                player3.setRotate(155);
                gridPane.add(player3, 0, 0, 2, 1);
            }

            if(anzSpieler > 4) {
                Node player4 = makepanel(4);
                player4.setRotate(180);
                gridPane.add(player4, 2, 0, 1, 1);
            }

            if(anzSpieler > 5) {
                Node player5 = makepanel(5);
                player5.setRotate(205);
                gridPane.add(player5, 3, 0, 2, 1);
            }



            root.getChildren().add(gridPane);


            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an
            primaryStage.setScene(scene);
            sceneWidth = scene.getWidth();
            sceneHeight = scene.getHeight();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
