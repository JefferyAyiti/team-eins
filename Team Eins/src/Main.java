import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    static Hand[] haende;
    static Spieler[] spieler;


    //Wird später im Menü festgelegt
    private static int anzSpieler = (int) 2 + (int) (Math.random() * ((6 - 2) + 1));
    static Tisch tisch = new Tisch(anzSpieler);

    public static void main(String[] args) {
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

    private static void initSpieler(Spieler spieler, Hand hand, Tisch tisch) {
        hand.setSpieler(spieler);
        hand.setTisch(tisch);
    }


    //GUI

    Image image = new Image("/cards/back.png");
    Image card1 = new Image("/cards/card1.png");
    Image card2 = new Image("/cards/card2.png");
    Image card3 = new Image("/cards/card3.png");
    Image card4 = new Image("/cards/card4.png");
    Image card5 = new Image("/cards/card5.png");
    Image card6 = new Image("/cards/card6.png");
    Image lama = new Image("/cards/lama.png");
    Image table = new Image("/table.png");
    Image[] cardsArray = {card1, card2, card3, card4, card5, card6, null, null, null,lama};

    Image whiteChipImage = new Image("/chips/white.png");
    Image blackChipImage = new Image("/chips/black.png");


    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");
        primaryStage.setResizable(false);

        StackPane root = new StackPane();

        Scene scene = new Scene(root, 999, 700);
        BackgroundImage myBI = new BackgroundImage(table,
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);


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

        Pane table = new Pane();
        //karten auf dem Tisch
        for (int i = 0; i < tisch.getAblageStapelSize(); i++) {
            ImageView imgView = new ImageView(image);
            imgView.setY(120 + i * 0.1);
            imgView.setX(100 + i * 0.3);
            table.getChildren().add(imgView);
        }

        //Ablagestapel
        ImageView imgView = new ImageView(
                cardsArray[tisch.getObereKarteAblagestapel().getValue()]);
        imgView.setY(120);
        imgView.setX(250);
        table.getChildren().add(imgView);


        imgView = new ImageView(blackChipImage);
        imgView.setY(120);
        imgView.setX(180);
        imgView.setFitHeight(20);
        imgView.setFitWidth(20);
        //imgView.setOnMouseClicked(this::ablegen);
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
        // imgView.setOnMouseClicked(this::ablegen);
        table.getChildren().add(imgView);
        ChipText = new Text();
        ChipText.setText(Integer.toString(tisch.getWhiteChips()));
        ChipText.setFont(Font.font("Verdana", 20));
        ChipText.setFill(Color.WHITE);
        ChipText.setX(205);
        ChipText.setY(168);
        table.getChildren().add(ChipText);


        //button1.setOnAction(this);
        gridPane.add(table, 1, 1, 3, 3);

        if (anzSpieler <= 4) {
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

    }

    private Pane makepanel(int playerId) {

        VBox pane = new VBox();

        pane.getChildren().add(new Label(spieler[playerId].getName()));
        pane.setAlignment(Pos.CENTER);

        Pane cards = new Pane();
        //verdeckte Karten
        int zahl = (int) ((Math.random()) * 6 + 1);
        for (int i = 0; i < spieler[playerId].getCardCount(); i++) {
            ImageView imgView = new ImageView(image);
            if (playerId == 0) {
                imgView = new ImageView(
                        cardsArray[spieler[playerId].getCardHand().getKarte(i).getValue()-1]);
            }


            imgView.setX(10 + i * 13);
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

}
