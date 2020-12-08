package GUI;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import Main.*;

import static Main.Main.classPrimaryStage;
import static Main.Main.zoomfactor;


public class GuiSpieltisch {



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

        Label plr = new Label(Main.spieler[playerId].getName());
        if (playerId > 1 && playerId < 5) {
            plr.setRotate(180);
        }
        plr.setTextFill(Color.WHITE);
        plr.setFont(Font.font("Verdana", 12 * zoomfactor));
        if (Main.spieler[playerId] == Main.tisch.getAktivSpieler()) {
            plr.setTextFill(Color.YELLOW);
            plr.setFont(Font.font("Verdana", FontWeight.BOLD, 14 * zoomfactor));
        }
        plr.setTranslateY(-15);
        pane.getChildren().add(plr);

        int cardcount = Main.spieler[playerId].getCardCount();
        if (cardcount > 7 && playerId != 0) {
            plr.setText(Main.spieler[playerId].getName() + " (" + cardcount + ")");
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

            if (playerId == 0) {
                imgView = new ImageView(
                        Main.cardsArray[Main.spieler[playerId].getCardHand().getKarte(i).getValue() - 1]);
                ImageView finalImgView = imgView;
                imgView.setOnMouseEntered(e -> finalImgView.setStyle(HOVERED_BUTTON_STYLE));
                ImageView finalImgView1 = imgView;
                imgView.setOnMouseExited(e -> finalImgView1.setStyle(IDLE_BUTTON_STYLE));
            } else if (i > 6)
                continue;


            imgView.setPreserveRatio(true);
            imgView.setSmooth(true); //Visuelle Große der Handkarte ändern
            imgView.setFitWidth(playerId == 0 ? 80 * zoomfactor : 40 * zoomfactor);


            if (playerId != 0) {
                imgView.setTranslateX(-cardcount / 2 * 10*zoomfactor + 10*zoomfactor * i);
                imgView.setTranslateY(-10);
                imgView.setRotate(-cardcount / 2 * 15 + i * 15);
            } else {
                int finalI = i;
                imgView.setOnMouseClicked(mouseEvent -> {
                    Main.spiellogik.karteLegen(Main.spieler[playerId],
                            Main.spieler[playerId].getCardHand().getKarte(finalI));
                    //Chip tausch
                    if (Main.spieler[playerId].getCardHand().getHandKarte().isEmpty()) {
                        Chip tausch;
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Glückwunsch");
                        alert.setHeaderText("Willst du einen Chip abgeben?");

                        ButtonType buttonTypeWhite = new ButtonType("weiß");
                        ButtonType buttonTypeBlack = new ButtonType("schwarz");
                        ButtonType buttonTypeCancel = new ButtonType("schließen", ButtonBar.ButtonData.CANCEL_CLOSE);

                        //spieler hat weiße und Schwarze Chips
                        if (Main.spieler[playerId].getWhiteChips() >= 1 && Main.spieler[playerId].getBlackChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeBlack, buttonTypeCancel);
                            //nur weiße
                        } else if (Main.spieler[playerId].getWhiteChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeCancel);
                            //nur schwarze
                        } else if (Main.spieler[playerId].getBlackChips() >= 1) {
                            alert.getButtonTypes().setAll(buttonTypeBlack, buttonTypeCancel);
                        } else {
                            alert.setHeaderText("Du hast keine Chips zum abgeben");
                            alert.getButtonTypes().setAll(buttonTypeCancel);
                        }

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeWhite) {
                            // ... user chose "weiß"
                            tausch = new WhiteChip();
                            Main.spiellogik.chipAbgeben(Main.spieler[playerId], tausch);
                        } else if (result.get() == buttonTypeBlack) {
                            // ... user chose "schwarz"
                            tausch = new BlackChip();
                            Main.spiellogik.chipAbgeben(Main.spieler[playerId], tausch);
                        } else {
                            // ... user chose CANCEL or closed the dialog
                        }

                    }
                    buildStage(Main.classPrimaryStage);


                });
            }
            if (playerId == 0) {
                if (cardcount > 7) {
                    imgView.setTranslateX(10 * (cardcount % 2) + cardcount / 2 * 20 * zoomfactor - 20 * zoomfactor * i);
                } else {
                    imgView.setTranslateX((cardcount % 2 == 0 ? 55 / 2 * zoomfactor : 0) + 55 * zoomfactor * (i - cardcount / 2));
                }
            }

            if (!Main.spieler[playerId].inGame())
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
        ImageView blChip = new ImageView(Main.blackChipImage);
        blChip.setFitHeight(chipsize * zoomfactor);
        blChip.setFitWidth(chipsize * zoomfactor);
        //Chip counter unter die Karte

        ImageView whChip = new ImageView(Main.whiteChipImage);
        whChip.setFitHeight(chipsize * zoomfactor);
        whChip.setFitWidth(chipsize * zoomfactor);

        if (Main.ich == playerId) {
            chips.setOnMouseClicked(mouseEvent -> {
                Main.spiellogik.chipsTauschen(Main.spieler[playerId]);
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
        text.setText("" + Main.spieler[playerId].getBlackChips());
        chips.add(text, 1, 0);

        text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText(Main.spieler[playerId].getWhiteChips() + "");

        chips.add(text, 1, 1);
        //chips.setY(102);


        HBox bottom = new HBox(chips);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(30);

        //Aussteigen
        if (playerId == Main.ich) {

            ImageView exit = new ImageView(Main.loader.getImg("GUI/images/SVG/no-touch.svg", zoomfactor * 0.45));
            exit.setTranslateY(-7);
            chips.setTranslateY(7);
            if (playerId > 1 && playerId < 5) {

            }

            exit.setOnMouseEntered(e -> exit.setStyle(HOVERED_BUTTON_STYLE));
            exit.setOnMouseExited(e -> exit.setStyle(IDLE_BUTTON_STYLE));
            bottom.getChildren().add(exit);

            exit.setOnMouseClicked(mouseEvent -> {
                Main.spiellogik.aussteigen(Main.spieler[playerId]);
                buildStage(Main.classPrimaryStage);
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
            } else
            if (Main.spiellogik.getRundeBeendet()) {
                Main.scoreboardGui.showRangliste(Main.spiellogik.ranglisteErstellen());
                return;
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
                        if (Main.spiellogik.karteNachziehen(Main.spieler[0]))
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
                imgView.setY(Math.random()*3);
                imgView.setX(15-30*Math.random());
                imgView.setRotate(15-30*Math.random());
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

            gridPane.add(makepanel(0), 1, 4, 3, 1);

            Node player1 = makepanel(1);
            player1.setRotate(90);
            gridPane.add(player1, 0, 2, 1, 1);

            if (Main.anzSpieler > 2) {
                Node player2 = makepanel(2);
                gridPane.add(player2, 0, 0, 2, 1);
                player2.setRotate(155);
            }

            if (Main.anzSpieler > 3) {
                Node player3 = makepanel(3);
                player3.setRotate(180);
                gridPane.add(player3, 2, 0, 1, 1);
            }

            if (Main.anzSpieler > 4) {
                Node player4 = makepanel(4);
                player4.setRotate(205);
                gridPane.add(player4, 3, 0, 2, 1);
            }

            if (Main.anzSpieler > 5) {
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
            for (Map.Entry<Spieler, Integer> entry : Main.spiellogik.ranglisteErstellen().entrySet()) {
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

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
