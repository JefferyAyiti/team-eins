import SVG.SvgImageLoaderFactory;
import SVG.TestLoadImageUsingClass;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.security.auth.callback.Callback;
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
    Stage classPrimaryStage;

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

    Image[] cardsArray = {card1, card2, card3, card4, card5, card6, null, null, null, lama};

    private static Image blackChipImage;
    private static Image whiteChipImage;

    /**
     * Erstellt Tisch, Nachzieh- und Abalgestapel, Spieler
     * und startet die erste Runde im Spiel
     */
    private static void initGame(){
        SvgImageLoaderFactory.install();
        image = new Image("images/SVG/Back.svg");
        card1 = new Image("images/SVG/Card1.svg");
        card3 = new Image("images/SVG/Card3.svg");
        card4 = new Image("images/SVG/Card4.svg");
        card2 = new Image("images/SVG/Card2.svg");
        card5 = new Image("images/SVG/Card5.svg");
        card6 = new Image("images/SVG/Card6.svg");
        lama =  new Image("images/SVG/Lama.svg");
        table1 =new Image("images/table2.svg");
        blackChipImage = new Image("/images/SVG/blackChip.svg");
        whiteChipImage = new Image("/images/SVG/whiteChip.svg");


        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        //spieler[0]= new Bot("Spieler",2);
       spieler[0] = new Spieler("Spieler 1");
        for (int i = 1; i < anzSpieler; i++) {
            spieler[i] = new Bot("Bot " + (i + 1), 3);

        }
        tisch = new Tisch(spieler);
        spiellogik = new Spiellogik(tisch);
        spiellogik.initNeueRunde();


    }


    //GUI




    public void resize() {

        if(System.currentTimeMillis() < resize+500) {
            getZoomedImages();
            buildStage(classPrimaryStage);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");
        buildStage(primaryStage);


        Timer timer = new Timer();
        timer.schedule(new MyTask1(), 3000, 500);

        Timer timer2 = new Timer();
        timer2.schedule(new moveCheck(), 3000, 100);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            resize = System.currentTimeMillis();

        };


        primaryStage.widthProperty().addListener(stageSizeListener);
        primaryStage.heightProperty().addListener(stageSizeListener);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                timer.cancel();
                timer2.cancel();
            }
        });
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
        if(playerId > 1 && playerId < 5) {
            plr.setRotate(180);
        }
        plr.setTextFill(Color.WHITE);
        plr.setFont(Font.font("Verdana", 12 * zoomfactor));
        if(spieler[playerId] == tisch.getAktivSpieler()) {
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
            } else if (i > 6)
                continue;


            imgView.setPreserveRatio(true);
            imgView.setSmooth(true); //Visuelle Große der Handkarte ändern
            imgView.setFitWidth(playerId == 0 ? 80 * zoomfactor : 50 * zoomfactor);


            if (playerId != 0) {
                imgView.setTranslateX(-cardcount/2*10+10*i);
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
                        alert.setHeaderText("Willst du einen Chip tauschen?");

                        ButtonType buttonTypeWhite = new ButtonType("weiß");
                        ButtonType buttonTypeBlack = new ButtonType("schwarz");
                        ButtonType buttonTypeCancel = new ButtonType("schließen", ButtonBar.ButtonData.CANCEL_CLOSE);

                        //spieler hat weiße und Schwarze Chips
                        if(spieler[playerId].getWhiteChips()>=1 && spieler[playerId].getBlackChips()>=1 ) {
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeBlack, buttonTypeCancel);
                            //nur weiße
                        }else if (spieler[playerId].getWhiteChips()>=1){
                            alert.getButtonTypes().setAll(buttonTypeWhite, buttonTypeCancel);
                            //nur schwarze
                        }else if(spieler[playerId].getBlackChips()>=1){
                            alert.getButtonTypes().setAll(buttonTypeBlack, buttonTypeCancel);
                        }else{
                            alert.setHeaderText("Du hast keine Chips zum abgeben");
                            alert.getButtonTypes().setAll(buttonTypeCancel);
                        }

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == buttonTypeWhite){
                            // ... user chose "weiß"
                            tausch=new WhiteChip();
                            spiellogik.chipAbgeben(spieler[playerId], tausch);
                        } else if (result.get() == buttonTypeBlack) {
                            // ... user chose "schwarz"
                            tausch=new BlackChip();
                            spiellogik.chipAbgeben(spieler[playerId], tausch);
                        }else {
                            // ... user chose CANCEL or closed the dialog
                        }

                    }
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

            if(!spieler[playerId].inGame())
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
        blChip.setOnMouseClicked( mouseEvent -> {
            BlackChip bye = new BlackChip();
            spiellogik.chipsTauschen(spieler[playerId]);
            buildStage(classPrimaryStage);
        });

        ImageView whChip = new ImageView(whiteChipImage);
        whChip.setFitHeight(chipsize * zoomfactor);
        whChip.setFitWidth(chipsize* zoomfactor);



        chips.add(blChip,0,0);
        chips.add(whChip,0,1);

        Text text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText(""+spieler[playerId].getBlackChips());
        chips.add(text, 1,0);

        text = new Text();
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Verdana", 12 * zoomfactor));
        text.setText(spieler[playerId].getWhiteChips() +"");

        chips.add(text, 1,1);
        //chips.setY(102);


        HBox bottom = new HBox(chips);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);

        //Aussteigen
        if(playerId == ich) {

        ImageView exit = new ImageView(loader.getImg("images/exit.svg", zoomfactor*0.4));
        exit.setTranslateY(1);
        chips.setTranslateY(1);
            if(playerId > 1 && playerId < 5) {
                chips.setRotate(180);
            }

            bottom.getChildren().add(exit);

            exit.setOnMouseClicked(mouseEvent -> {
            spiellogik.aussteigen(spieler[playerId]);
            buildStage(classPrimaryStage);
        });

        }

        pane.getChildren().add(bottom);

        switch(playerId) {
            case 1:
            case 5:
                pane.setTranslateY(+30*zoomfactor); break;
            case 4: pane.setTranslateX(30*zoomfactor); pane.setTranslateY(10+10*zoomfactor); break;
            case 3: pane.setTranslateY(10*zoomfactor); break;
            case 2: pane.setTranslateX(-30*zoomfactor); pane.setTranslateY(10+10*zoomfactor); break;
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
            //karten auf dem Tisch
            Pane nachziehstapel = new Pane();
            for (int i = 0; i < tisch.getNachziehStapelSize(); i++) {
                ImageView imgView = new ImageView(image);
                imgView.setY(i * 0.2);
                imgView.setX(i * 0.2);
                imgView.setOnMouseClicked(mouseEvent -> {
                    spiellogik.karteNachziehen(spieler[0]);
                    buildStage(classPrimaryStage);
                });
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
            for (Map.Entry<Spieler, Integer> entry : spiellogik.ranglisteErstellen().entrySet()) {
                //System.out.println(entry.getKey().getName() + ":" + entry.getValue());
                Label name = new Label(entry.getKey().getName());
                name.setTextFill(Color.WHITE);
                name.setFont(new Font(10*zoomfactor));
                names.getChildren().add(name);
                Label sco = new Label(Integer.toString(entry.getValue()));
                sco.setFont(new Font(10*zoomfactor));
                sco.setTextFill(Color.WHITE);
                sc.getChildren().add(sco);
            }

            score.addRow(0, names, sc);
            score.setHgap(15);
            score.setAlignment(Pos.CENTER_RIGHT);
            gridPane.add(score, 4, 4, 1 ,1);

            root.getChildren().add(gridPane);


            // nun Setzen wir die Scene zu unserem Stage und zeigen ihn an
            primaryStage.setScene(scene);
            if(spiellogik.getRundeBeendet()) {
                showRangliste(spiellogik.ranglisteErstellen());

            }
            sceneWidth = scene.getWidth();
            sceneHeight = scene.getHeight();
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

        int platz=1;

        // create an ListView based on key items in the map.
        ObservableMap<Spieler, Integer> observableExtensionToMimeMap = FXCollections.observableMap(ranking);
        ListView<String> liste = new ListView<>();
         for (Map.Entry<Spieler, Integer> r : ranking.entrySet()) {
             liste.getItems().add("\t"+"Platz "+platz+":\t\t" +r.getKey().getName()+"\t\t\t" + r.getValue()+"\t");
             platz++;
         }
         //textgröße
         liste.setCellFactory(cell -> {
             return new ListCell<String>() {
                 @Override
                 protected void updateItem(String item, boolean empty) {
                     super.updateItem(item, empty);
                     if (item != null) {
                         setText(item);

                         // decide to add a new styleClass
                         // getStyleClass().add("costume style");
                         // decide the new font size
                         setFont(Font.font(18*zoomfactor));
                     }
                 }
             };
         });

         Button nextRound;
         if(!spiellogik.spielBeendet) {
             nextRound = new Button("nächste Runde");
             nextRound.setOnAction(e -> {
                        spiellogik.initNeueRunde();
                        buildStage(classPrimaryStage);
                     }
             );

         } else {
             nextRound = new Button("Spiel beenden");
             nextRound.setOnAction(e -> {
                         classPrimaryStage.close();
                     }
             );
         }

        //Darstellung
        Label titel= new Label("Rangliste");
        HBox top= new HBox(titel);
        top.setMinHeight(sceneHeight/6);
        top.setAlignment(Pos.CENTER);

        HBox bottom= new HBox(nextRound);
        bottom.setMinHeight(sceneHeight/6);
        bottom.setAlignment(Pos.CENTER);

        VBox left = new VBox();
        left.setMinWidth(sceneWidth/10);

        VBox right = new VBox();
        right.setMinWidth(sceneWidth/10);

        Pane center= new Pane (liste);
        center.setMaxHeight(liste.getHeight());
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(liste);
        root.setRight(right);
        root.setBottom(bottom);
        root.setLeft(left);


        //Hintergrund
         BackgroundImage myBI = new BackgroundImage(table1,
                 BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                 new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        String css = Main.class.getResource("Rangliste.css").toExternalForm();
        Scene rangliste = new Scene(root, sceneWidth, sceneHeight);
        rangliste.getStylesheets().add(css);

        System.out.println("gibt Rangliste aus");
        classPrimaryStage.setScene(rangliste);

        return rangliste;
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

    class moveCheck extends TimerTask {
        @Override
        public void run() {
            if(tisch.getAktivSpieler() instanceof Bot && !spiellogik.getRundeBeendet()) {
                ((Bot) tisch.getAktivSpieler()).play();
                Platform.runLater(() -> {
                    buildStage(classPrimaryStage);
                });


            }
        }
    }


}
