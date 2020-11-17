import SVG.TestLoadImageUsingClass;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main_Maik extends Application{

    static Hand[] haende;
    static Spieler[] spieler;
    static TestLoadImageUsingClass loader;


    //Wird später im Menü festgelegt
    private static int anzSpieler = 2 + (int) (Math.random() * 4);
    static Tisch tisch = new Tisch(anzSpieler);

    public static void main(String[] args) {
        loader = new TestLoadImageUsingClass();
        loader.installSvgLoader();
        try {
            initGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }

    private static void initGame() throws Exception {
        //initialisiere GrundTisch

        tisch.initNachziehstapel();
        tisch.mischenNachziehstapel();

        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        for (int i = 0; i < anzSpieler; i++) {
            haende[i] = new Hand();
            spieler[i] = new Spieler(haende[i], "Spieler " + (i + 1), tisch);

            initSpieler(spieler[i], haende[i], tisch);
        }


        //gebe jeden Spieler (anzSpieler) 6 Karten in Reihenfolge
        for (int i = 0; i < 6; i++) {
            for (int s = 0; s < anzSpieler; s++) {
                haende[s].addKarte(tisch.karteZiehen());
            }

        }

        tisch.karteAblegen(tisch.karteZiehen()); //Ablagestapel
    }

    private static void initSpieler(Spieler spielerI, Hand hand, Tisch tisch) {
        hand.setSpieler(spielerI);
        hand.setTisch(tisch);
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
    Image table1 = loader.getImg("images/table.svg");

    Image[] cardsArray = {card2, card3, card4, card5, card6, card7, null, null, null, lama};

    Image whiteChipImage = new Image("/images/chips/white.png");
    Image blackChipImage = new Image("/images/chips/black.png");


    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");
        buildStage(primaryStage);


    }


    private Pane makepanel(int playerId) {


        VBox pane = new VBox();

        pane.getChildren().add(new Label(spieler[playerId].getName()));
        pane.setAlignment(Pos.CENTER);

        Pane cards = new Pane();
        //verdeckte Karten

        for (int i = 0; i < spieler[playerId].getCardCount(); i++) {
            ImageView imgView = new ImageView(image);
            if (playerId == 0) {
                imgView = new ImageView(
                        cardsArray[spieler[playerId].getCardHand().getKarte(i).getValue() - 1]);
            }


            imgView.setX(10 + i * 13);
            imgView.setPreserveRatio(true);
            imgView.setSmooth(true);
            imgView.setFitWidth(80);

            cards.getChildren().add(imgView);
        }

        pane.getChildren().add(cards);
        Text chips = new Text();
        chips.setText("⚪: " + spieler[playerId].getWhiteChips() +
                "    ⚫: " + spieler[playerId].getBlackChips());
        chips.setY(102);
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

    public void buildStage(Stage primaryStage) {
        try {
            StackPane root = new StackPane();
            classPrimaryStage = primaryStage;

            Scene scene = new Scene(root, 600, 400);
            BackgroundImage myBI = new BackgroundImage(table1,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(100, 100, true, true, true, false));




            Pane table = new Pane();
            //karten auf dem Tisch
            for (int i = 0; i < tisch.getAblageStapelSize(); i++) {
                ImageView imgView = new ImageView(image);
                imgView.setY(120 + i * 0.1);
                imgView.setX(100 + i * 0.3);
                imgView.setPreserveRatio(true);
                imgView.setSmooth(true);
                imgView.setFitWidth(100);
                table.getChildren().add(imgView);
            }

            //Ablagestapel
            ImageView imgView = new ImageView(
                    cardsArray[tisch.getObereKarteAblagestapel().getValue() - 1]);
            //TEST_EVENT:
            imgView.setOnMouseClicked(event_ablage);
            imgView.setY(120);
            imgView.setX(50);
            imgView.setPreserveRatio(true);
            imgView.setFitWidth(100);
            table.getChildren().add(imgView);


            imgView = new ImageView(blackChipImage);
            imgView.setY(120);
            imgView.setX(180);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            table.getChildren().add(imgView);
            Text ChipText = new Text();
            ChipText.setText(Integer.toString(tisch.getBlackChips()));
            ChipText.setFont(Font.font("Verdana", 20));
            ChipText.setFill(Color.WHITE);
            ChipText.setX(205);
            ChipText.setY(138);
            table.getChildren().add(ChipText);

            imgView = new ImageView(whiteChipImage);
            imgView.setY(150);
            imgView.setX(180);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            table.getChildren().add(imgView);
            ChipText = new Text();
            ChipText.setText(Integer.toString(tisch.getWhiteChips()));
            ChipText.setFont(Font.font("Verdana", 20));
            ChipText.setFill(Color.WHITE);
            ChipText.setX(205);
            ChipText.setY(168);
            table.getChildren().add(ChipText);


            GridPane gridPane = new GridPane();
            gridPane.setBackground(new Background(myBI));
            gridPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            gridPane.setGridLinesVisible(false);
            gridPane.setAlignment(Pos.CENTER);



            for (int i = 0; i < 5; i++) {
                ColumnConstraints column = new ColumnConstraints(130);
                gridPane.getColumnConstraints().add(column);
                RowConstraints row = new RowConstraints(110);
                gridPane.getRowConstraints().add(row);
            }

            //button1.setOnAction(this);
            gridPane.add(table, 1, 1, 3, 3);

            if (anzSpieler <= 4) {
                Pane pl1 = makepanel(0);
                gridPane.add(makepanel(0), 2, 0, 1, 1);
                gridPane.add(makepanel(1), 2, 4, 1, 1);

            } else {
                gridPane.add(makepanel(0), 1, 0, 1, 1);
                gridPane.add(makepanel(1), 1, 4, 1, 1);

                gridPane.add(makepanel(4), 3, 0, 1, 1);
                gridPane.add(makepanel(5), 3, 4, 1, 1);
            }
            if (anzSpieler > 2) {
                gridPane.add(makepanel(2), 0, 2, 1, 1);
            }
            if (anzSpieler > 3) {
                gridPane.add(makepanel(3), 4, 2, 1, 1);

            }


            root.getChildren().add(gridPane);


            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }




}
