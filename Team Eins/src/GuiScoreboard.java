import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Map;

public class GuiScoreboard {
    static Scene showRangliste(Map<Spieler, Integer> ranking) throws InterruptedException {
        System.out.println("gibt Rangliste aus");
        int p = 1;

        //Scoreboard
        VBox names = new VBox(new Label(""));
        VBox score = new VBox(new Label(""));
        VBox differ = new VBox(new Label(""));
        VBox platz = new VBox(new Label(""));

        GridPane center = new GridPane();

        for (Map.Entry<Spieler, Integer> entry : Main.spiellogik.ranglisteErstellen().entrySet()) {
            //Platz
            Label rang = new Label("\t" + p + ". Platz: " + "\t");
            rang.setFont(new Font("Ink Free", 19 * Main.zoomfactor));
            rang.setTextFill(Color.WHITE);
            platz.getChildren().add(rang);
            //platz.setStyle("-fx-effect: dropshadow( gaussian , black ,10 ,0.4 ,0 ,0 )");
            //Spieler
            Label name = new Label(entry.getKey().getName() + "    \t");
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

            System.out.println(entry.getKey().getName() + ":  -  alt:" + entry.getKey().getOldScore() + "   neu:" + entry.getKey().getPoints() + "   dif:" + dif);
        }

        center.addRow(0, platz, names, score, differ);
        center.setHgap(30 * Main.zoomfactor);
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('images/oberflaeche.jpg')");
        center.setMinHeight(250 * Main.zoomfactor);
        center.setMinWidth(300 * Main.zoomfactor);

        HBox bottom = new HBox();
        bottom.setSpacing(15);
        //bottom.setMinHeight(sceneHeight / 8);
        bottom.setAlignment(Pos.CENTER);

        Button nextRound;

        if (!Main.spiellogik.spielBeendet) {
            nextRound = new Button("Nächste Runde");
            nextRound.setOnAction(e -> {
                Main.spiellogik.initNeueRunde();
                Main.spieltischGui.buildStage(Main.classPrimaryStage);
                    }
            );

        } else {
            Button endGame = new Button("Spiel beenden");
            endGame.setTranslateY(-15);
            endGame.setOnAction(e -> {
                Main.classPrimaryStage.close();
                Main.bots.cancel();
                Main.resizecheck.cancel();
                    }
            );
            bottom.getChildren().add(endGame);

            nextRound = new Button("Hauptmenü");
            nextRound.setOnAction(e -> {
                Main.hauptmenuGui.showSettingsMenu(Main.classPrimaryStage);

                    }
            );
        }
        nextRound.setTranslateY(-15);
        bottom.getChildren().add(nextRound);

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


        //Hintergrund
         /*BackgroundImage myBI2 = new BackgroundImage(score,
                 BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                 new BackgroundSize(100, 100, true, true, false, true));
         liste.setBackground(new Background(myBI2));
        */

        BackgroundImage myBI = new BackgroundImage(Main.table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        String css = Main.class.getResource("Rangliste.css").toExternalForm();
        Scene rangliste = new Scene(root, Main.sceneWidth, Main.sceneHeight);
        rangliste.getStylesheets().add(css);

        Main.classPrimaryStage.setScene(rangliste);

        return rangliste;
    }
}
