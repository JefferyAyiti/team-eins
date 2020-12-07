import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GuiHauptmenu {
    /**
     * @param PrimaryStage Erzeugt und zeigt das Hauptmenü zu Beginn des Spiels an
     */
    void showSettingsMenu(Stage PrimaryStage) {
        GridPane center = new GridPane();
        center.setVgap(10);

        //Spielername
        TextField namefield = new TextField(Main.myName);
        center.addRow(0, new Label("Spielername: "), namefield);

        TextField ip = new TextField("localhost");;
        TextField port = new TextField("50099");
        if(Main.playMode == 2) {
            //IP:Port
            center.addRow(1, new Label("Server-IP: "), ip);
            center.addRow(2, new Label("Server-Port: "), port);

        }


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
        if (Main.playMode < 2)
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
        if (Main.playMode < 2)
            center.addRow(2, new Label("Bot-Schwierigkeit: "), botselect);


        center.setHgap(60 * Main.zoomfactor);
        center.setId("MMcenter");
        center.setStyle("-fx-border-width:5 ; -fx-border-color:black;-fx-background-image: url('images/oberflaeche.jpg')");
        center.setMinHeight(250 * Main.zoomfactor);
        center.setMinWidth(200 * Main.zoomfactor);

        //Geschwindigkeit
        Slider slider = new Slider();
        slider.setMin(500);
        slider.setMax(5000);
        slider.setValue(Main.botPlayTime == 0?
                (slider.getMax() - slider.getMin()) / 2 + slider.getMin():
                Main.botPlayTime);
        slider.setShowTickMarks(false);
        slider.setShowTickLabels(false);
        slider.setMinorTickCount(1000);
        slider.setMajorTickUnit(1000);
        slider.setBlockIncrement(10);
        slider.setPrefSize(150, 5);
        if (Main.playMode < 2)
            center.addRow(3, new Label("Bot-Bedenkzeit: "), slider);


        //Darstellung
        Label titel = new Label("Hauptmenü");
        titel.setTextFill(Color.WHITE);
        titel.setFont(new Font("Script MT Bold", 36 * Main.zoomfactor));

        VBox top = new VBox(titel);
        top.setMinHeight(Main.sceneHeight / 8);
        top.setAlignment(Pos.CENTER);

        //Singlepalyer / Host / Join
        HBox multiplayer = new HBox();
        multiplayer.setSpacing(50);
        multiplayer.setTranslateY(15);
        multiplayer.setAlignment(Pos.BOTTOM_CENTER);
        Label single = new Label("Einzelspieler");
        single.setOnMouseClicked(e -> {
            Main.playMode = 0;
            showSettingsMenu(PrimaryStage);
        });

        Pane host = new Pane(new Label("Server erstellen"));
        host.setOnMouseClicked(mouseEvent -> {
            Main.playMode = 1;
            showSettingsMenu(PrimaryStage);
        });

        Label join = new Label("Server joinen");
        join.setOnMouseClicked(e -> {
            Main.playMode = 2;
            showSettingsMenu(PrimaryStage);
        });

        multiplayer.getChildren().addAll(single, host, join);
        multiplayer.setId("MMtop");
        BorderPane root = new BorderPane();

        top.getChildren().add(multiplayer);
        HBox bottom = new HBox();
        bottom.setMinHeight(Main.sceneHeight / 8);
        bottom.setAlignment(Pos.CENTER);
        root.setBottom(bottom);


        Button start = new Button(Main.playMode < 2?"Spiel starten":"Spiel beitreten");
        start.setTranslateY(-10);
        if (Main.playMode < 2) {
            start.setOnAction(e -> {
                Main.botPlayTime = (long) slider.getValue();
                Main.botlevel = botselect.getSelectionModel().getSelectedIndex();
                Main.myName = namefield.getText();
                        if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
                Main.anzSpieler = (int) playeranzselect.getValue();
                        Main.initGame();
                Main.sceneWidth = 600;
                Main.sceneHeight = 400;
                Main.runTimers(PrimaryStage);
                GuiZoomLoader.getZoomedImages();
                Main.spieltischGui.buildStage(PrimaryStage);
                    }
            );
        } else {
            start.setOnAction(e -> {
                Main.myName = namefield.getText();
                        if (Main.myName == null || Main.myName.equals("")) Main.myName = "Spieler";
                        System.out.println(ip.getText());
                        System.out.println(port.getText());
                GuiZoomLoader.getZoomedImages();
                        showSettingsMenu(PrimaryStage);
                    }
            );
        }

        bottom.getChildren().add(start);



        center.setAlignment(Pos.TOP_LEFT);
        center.setMaxHeight(center.getHeight());

        root.setTop(top);
        root.setCenter(center);


        BackgroundImage myBI = new BackgroundImage(Main.table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        Scene menu = new Scene(root, Main.sceneWidth, Main.sceneHeight);
        menu.getStylesheets().add("MainMenu.css");

        PrimaryStage.setScene(menu);
        PrimaryStage.show();
    }

}
