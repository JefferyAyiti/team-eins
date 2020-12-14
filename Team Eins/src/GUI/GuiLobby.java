    package GUI;

    import Main.*;
    import Main.Spieler;
    import RMI.ClientThread;
    import RMI.RMIClient;
    import RMI.RunServer;
    import javafx.application.Application;
    import javafx.geometry.Pos;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.stage.Stage;

    import java.rmi.RemoteException;
    import java.util.Map;

    import static Main.Main.*;

    public class GuiLobby {
    public String action;
    public static String host;

    public static Scene lobby() {
        Label titel = new Label("Lobby");
        titel.setFont(new Font("Script MT Bold", 36 * Main.zoomfactor));
        titel.setId("titel");

        VBox teilnehmer = new VBox();
        teilnehmer.setSpacing(10);
        teilnehmer.setPrefHeight(150);


        try {
            for (Map.Entry<String, String> entry : server.getClients().entrySet()) {
                Label spieler;
                if(server.getHost().equals(entry.getKey())){
                    spieler = new Label ("HOST: "+entry.getValue());
                }else {
                    spieler = new Label(entry.getValue() + " ist beigetreten");
                }
                spieler.setId("LabelCenter");
                spieler.setAlignment(Pos.TOP_CENTER);
                teilnehmer.getChildren().add(spieler);
                if(uniqueID.equals(entry.getKey())){
                    spieler.setId("Spieler");
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Label difficulty = new Label();
        //System.out.println(level);
        switch(Main.botlevel){
            case 0:
                difficulty = new Label("Schwierigkeit: Zufällig");
                break;
            case 1:
                difficulty = new Label("Schwierigkeit: Einfach");
                break;
            case 2:
                difficulty = new Label("Schwierigkeit: Mittel");
                break;
            case 3:
                difficulty = new Label("Schwierigkeit: Schwer");
                break;
        }
        Label max = new Label("Spieleranzahl: "+ Main.anzSpieler);
        Label status =hauptmenuGui.status;
        HBox info = new HBox(status,max,difficulty);
        status.setStyle("-fx-text-fill: white; -fx-font-size: 15");
        max.setStyle("-fx-text-fill: white; -fx-font-size: 15");
        max.setTranslateY(30);
        difficulty.setStyle("-fx-text-fill: white; -fx-font-size: 15");
        difficulty.setTranslateY(30);
        info.setSpacing(50);

        GridPane center = new GridPane();
        teilnehmer.setAlignment(Pos.TOP_CENTER);
        center.setId("LCenter");
        center.addRow(0,teilnehmer);
        center.addRow(1,info);
        center.setHgap(30 * Main.zoomfactor);
        center.setMinHeight(250 * Main.zoomfactor);
        center.setAlignment(Pos.TOP_CENTER);


        HBox bottom = new HBox();
        bottom.setId("LUnten");
        bottom.setSpacing(15);
        bottom.setAlignment(Pos.CENTER);
        bottom.setMinWidth(sceneHeight / 10);
        Button close;
        if(playMode==1){
            Button start = new Button("Spiel starten");
            start.setOnAction(e -> hauptmenuGui.setSettings("startserver"));
            close = new Button("Server schließen");
            close.setOnAction(e -> hauptmenuGui.setSettings("close"));
            start.setTranslateY(-10);
            bottom.getChildren().addAll(start,close);

        }else {
            close = new Button("Raum verlassen");
            close.setOnAction(e -> hauptmenuGui.setSettings("leave"));
            bottom.getChildren().add(close);
        }
       close.setTranslateY(-10);



        Button nextRound = new Button();


        HBox top = new HBox(titel);
        top.setId("LTop");
        //top.setMinHeight(sceneHeight/8);
        top.setAlignment(Pos.CENTER);

        center.setMaxHeight(center.getHeight());
        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(center);

        root.setBottom(bottom);


        BackgroundImage myBI = new BackgroundImage(Main.table1,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        root.setBackground(new Background(myBI));


        //neue Scene
        Scene lobby = new Scene(root, Main.sceneWidth, Main.sceneHeight);
        lobby.getStylesheets().add("GUI/Lobby.css");

        return lobby;

    }
    public String getAction(){
        return action;
    }

    }
