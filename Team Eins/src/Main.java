import SVG.TestLoadImageUsingClass;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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
import java.util.TimerTask;
import java.util.Timer;


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
    volatile boolean resizeFlag;

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
     * Erstellt Tisch, Nachzieh- und Abalgestapel, Spieler
     * und startet die erste Runde im Spiel
     *
     * @throws Exception
     */
    private static void initGame() throws Exception {


        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        for (int i = 0; i < anzSpieler; i++) {
            spieler[i] = new Spieler("Spieler " + (i + 1));

        }
        tisch = new Tisch(spieler);
        spiellogik = new Spiellogik(tisch);

        spiellogik.initNeueRunde();


    }


    //GUI


    Image image = loader.getImg("images/SVG/Back.svg");
    Image card1 = loader.getImg("images/SVG/Card1.svg");
    Image card2 = loader.getImg("images/SVG/Card2.svg");
    Image card3 = loader.getImg("images/SVG/Card3.svg");
    Image card4 = loader.getImg("images/SVG/Card4.svg");
    Image card5 = loader.getImg("images/SVG/Card5.svg");
    Image card6 = loader.getImg("images/SVG/Card6.svg");
    Image lama = loader.getImg("images/SVG/Lama.svg");
    Image table1 = loader.getImg("images/table2.svg");

    Image[] cardsArray = {card1, card2, card3, card4, card5, card6, null, null, null, lama};

    Image whiteChipImage = new Image("/images/chips/white.png");
    Image blackChipImage = new Image("/images/chips/black.png");

    public void resize() {
        if(System.currentTimeMillis() > resize+500) {
            System.out.println("bup");
            getZoomedImages();
            buildStage(classPrimaryStage);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");
        buildStage(primaryStage);

        Timer timer = new Timer();
        timer.schedule(new MyTask1(), 1000, 300);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            if (resize < System.currentTimeMillis() - 500)
                getZoomedImages();
        };

        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.heightProperty().addListener(stageSizeListener);
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
        VBox.setMargin(pane, new Insets(0, 0 ,5 ,0));

        Label plr = new Label(spieler[playerId].getName());
        plr.setTextFill(Color.WHITE);
        plr.setTranslateY(-15);
        pane.getChildren().add(plr);

        int cardcount = spieler[playerId].getCardCount();
        if (cardcount > 7 && playerId != 0) {
            plr.setText(spieler[playerId].getName() + " (" + cardcount + ")");
            cardcount = 7;
        }

        plr.setFont(Font.font("Verdana", 12 * zoomfactor));

        StackPane cards = new StackPane();
        cards.setAlignment(Pos.BASELINE_CENTER);
        //cards.setStyle("-fx-background-color:#cccccc;");

        //verdeckte Karten


        for (int i = 0; i < cardcount; i++) {
            ImageView imgView = new ImageView(image);
            if (playerId == 0) {
                imgView = new ImageView(
                        cardsArray[spieler[playerId].getCardHand().getKarte(i).getValue() - 1]);
            } else if (i > 6)
                continue;


            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);
            imgView.setFitWidth(playerId == 0 ? 55 * zoomfactor : 40 * zoomfactor);


            if (playerId != 0) {
                imgView.setTranslateX(-cardcount/2*10+10*i);
                imgView.setTranslateY(-10);
                imgView.setRotate(-cardcount / 2 * 15 + i * 15);
            } else {
                int finalI = i;
                imgView.setOnMouseClicked(mouseEvent -> {
                    spiellogik.karteLegen(spieler[playerId],
                            spieler[playerId].getCardHand().getKarte(finalI));
                    buildStage(classPrimaryStage);
                });
            }
            if (playerId == 0) {
                if (cardcount > 8) {
                    imgView.setTranslateX(10 * (cardcount % 2) + cardcount / 2 * 20 * zoomfactor - 20 * zoomfactor * i);
                } else {
                    imgView.setTranslateX((cardcount%2==0?55/2 * zoomfactor:0)+55 * zoomfactor * (i - cardcount / 2));
                }
            }

            cards.getChildren().add(imgView);
        }


       /* if (playerId != 0) {
            cards.setTranslateX(+(min(6, spieler[playerId].getCardCount()) * 30) / 2 - 20);
        }*/
        pane.getChildren().add(cards);
        Pane chips = new VBox();
        chips.setMaxWidth(60 * zoomfactor);
        chips.setStyle("-fx-background-color:rgba(255,255,255,0.5); -fx-margin-top:5px;");
        Text text = new Text();
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText("⚪: " + spieler[playerId].getWhiteChips() +
                "    ⚫: " + spieler[playerId].getBlackChips());
        //chips.setY(102);
        chips.getChildren().add(text);
        pane.getChildren().add(chips);

        switch(playerId) {
            case 1: pane.setTranslateY(+30*zoomfactor); break;
            case 3: pane.setTranslateX(-30*zoomfactor); pane.setTranslateY(10*zoomfactor); break;
            case 5: pane.setTranslateX(30*zoomfactor); pane.setTranslateY(10*zoomfactor); break;
            case 2: pane.setTranslateY(+30*zoomfactor); break;
        }
        return pane;
    }

    Stage classPrimaryStage;




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
                imgView.setFitWidth(60 * zoomfactor);
                nachziehstapel.getChildren().add(imgView);
            }
            table.add(nachziehstapel, 0, 0, 1, 1);

            //Ablagestapel
            ImageView imgView = new ImageView(
                    cardsArray[tisch.getObereKarteAblagestapel().getValue() - 1]);

            //TEST_EVENT:


            imgView.setPreserveRatio(true);
            imgView.setFitWidth(60 * zoomfactor);
            table.add(imgView, 2, 0, 1, 1);

            GridPane chipsgrid = new GridPane();
            chipsgrid.setAlignment(Pos.CENTER);

            imgView = new ImageView(blackChipImage);
            imgView.setFitHeight(20 * zoomfactor);
            imgView.setFitWidth(20 * zoomfactor);
            chipsgrid.add(imgView, 0, 0, 1, 1);
            Text ChipText = new Text();
            ChipText.setText(" " + tisch.getBlackChips());
            ChipText.setFont(Font.font("Verdana", 20 * zoomfactor));
            ChipText.setFill(Color.WHITE);
            chipsgrid.add(ChipText, 1, 0, 1, 1);

            imgView = new ImageView(whiteChipImage);
            imgView.setFitHeight(20 * zoomfactor);
            imgView.setFitWidth(20 * zoomfactor);
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
                player2.setRotate(-90);
                gridPane.add(player2, 4, 2, 1, 1);
            }

            if (anzSpieler > 3) {
                Node player3 = makepanel(3);
                player3.setRotate(155);
                gridPane.add(player3, 0, 0, 2, 1);
            }

            if (anzSpieler > 4) {
                Node player4 = makepanel(4);
                player4.setRotate(180);
                gridPane.add(player4, 2, 0, 1, 1);
            }

            if (anzSpieler > 5) {
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

    void getZoomedImages() {

        if (resize < System.currentTimeMillis()-500) {
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
            resize = System.currentTimeMillis();
        }


    }

    class MyTask1 extends TimerTask {
        @Override
        public void run() {
            sceneWidth = classPrimaryStage.getScene().getWidth();
            sceneHeight = classPrimaryStage.getScene().getHeight();
            getZoomedImages();
        }
    }


}
