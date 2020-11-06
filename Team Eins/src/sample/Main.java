package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application implements EventHandler {

    // Hier legen wir einen Button an
    Button button;
    Button button2;

    Image image = new Image("/cards/back.png");
    Image card1 = new Image("/cards/card1.png");
    Image card2 = new Image("/cards/card2.png");
    Image card3 = new Image("/cards/card3.png");
    Image card4 = new Image("/cards/card4.png");
    Image card5 = new Image("/cards/card5.png");
    Image card6 = new Image("/cards/card6.png");
    Image[] cardsArray = {card1,card2,card3,card4,card5,card6};

    Image whiteChipImage = new Image("/chips/white.png");
    Image blackChipImage = new Image("/chips/black.png");


    @Override
    public void start(Stage primaryStage) {
        try {

            primaryStage.setTitle("L.A.M.A - Team Eins");

            StackPane root = new StackPane();

            Scene scene = new Scene(root,800,600);


            int playerCount = (int)((Math.random()) * 4 + 2);


            GridPane gridPane = new GridPane();
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
            table.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
            //karten auf dem Tisch
            for(int i=0;i<50;i++) {
                ImageView imgView = new ImageView(image);
                imgView.setY(120+i*0.1);
                imgView.setX(100+i*0.3);
                imgView.setOnMouseClicked(this::ablegen);
                table.getChildren().add(imgView);
            }


            ImageView imgView = new ImageView(blackChipImage);
            imgView.setY(120);
            imgView.setX(180);
            imgView.setFitHeight(20);
            imgView.setFitWidth(20);
            //imgView.setOnMouseClicked(this::ablegen);
            table.getChildren().add(imgView);
            Text ChipText = new Text();
            ChipText.setText("14");
            ChipText.setFont(Font.font ("Verdana", 20));
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
            ChipText.setText("23");
            ChipText.setFont(Font.font ("Verdana", 20));
            ChipText.setFill(Color.WHITE);
            ChipText.setX(205);
            ChipText.setY(168);
            table.getChildren().add(ChipText);


            //button1.setOnAction(this);
            gridPane.add(table, 1, 1, 3, 3);

            if(playerCount <= 4) {
                gridPane.add(makepanel(0), 2, 0, 1, 1);
                gridPane.add(makepanel(1), 2, 4, 1, 1);

            } else {
                gridPane.add(makepanel(0), 1, 0, 1, 1);
                gridPane.add(makepanel(1), 1, 4, 1, 1);



                gridPane.add(makepanel(4), 3, 0, 1, 1);
                gridPane.add(makepanel(5), 3, 4, 1, 1);
            }
            if(playerCount > 2) {
                gridPane.add(makepanel(2), 0, 2, 1, 1);
            }
            if(playerCount > 3) {
                gridPane.add(makepanel(3), 4, 2, 1, 1);

            }


            root.getChildren().add(gridPane);



            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void ablegen(Object karte) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Karte ablegen?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            System.out.println("Karte abgelegt");
        }
    }
    public void handle(Event arg0) {
        // Hier kommt der Code rein, der ausgeführt werden soll, wenn Button(s) geklickt werden.
        System.out.println(arg0.getSource());

    }



    private Pane makepanel(int PlayerId) {

        VBox pane = new VBox();

        pane.getChildren().add(new Label("Spieler "+(PlayerId+1)));
        pane.setAlignment(Pos.CENTER);

        Pane cards = new Pane();
        //verdeckte Karten
        int zahl = (int)((Math.random()) * 6 + 1);
        for(int i=0;i<zahl;i++) {
            ImageView imgView = new ImageView(image);
            if(PlayerId == 0) {
                imgView = new ImageView(cardsArray[i]);
            }


            imgView.setX(10+i*13);
            imgView.setOnMouseClicked(this::ablegen);
            cards.getChildren().add(imgView);
        }

        pane.getChildren().add(cards);
        Text chips = new Text();
        chips.setText("⚪: "+(int)((Math.random()) * 11)+"    ⚫: "+(int)((Math.random()) * 4));
        chips.setY(102);
        pane.getChildren().add(chips);

        return pane;
    }

}