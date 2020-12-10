    package GUI;

    import Main.*;
    import Main.Spieler;
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

    public static Scene lobby() {
        Label titel = new Label("Lobby");
        titel.setFont(new Font("Script MT Bold", 36 * Main.zoomfactor));
        titel.setId("titel");

        VBox teilnehmer = new VBox();
        teilnehmer.setSpacing(10);
        teilnehmer.setPrefHeight(150);

        try {
            for (Map.Entry<String, String> entry : server.getClients().entrySet()) {
                Label spieler = new Label(entry.getValue()+ " ist beigetreten");
                spieler.setId("LabelCenter");
                teilnehmer.getChildren().add(spieler);
                if(uniqueID.equals(entry.getKey())){
                    spieler.setId("Spieler");
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        GridPane center = new GridPane();

        center.setId("LCenter");
        center.addRow(0,teilnehmer);
        center.setHgap(30 * Main.zoomfactor);
        center.setMinHeight(250 * Main.zoomfactor);


        HBox bottom = new HBox();
        bottom.setId("LUnten");
        bottom.setSpacing(15);
        bottom.setAlignment(Pos.CENTER);
        bottom.setMinWidth(sceneHeight / 10);
        Button close = new Button("Raum verlassen");
       close.setOnAction(e -> hauptmenuGui.setSettings("close"));
       close.setTranslateY(-10);
       bottom.getChildren().add(close);

        Button nextRound = new Button();


        HBox top = new HBox(titel);
        top.setId("LTop");
        //top.setMinHeight(sceneHeight/8);
        top.setAlignment(Pos.CENTER);

        center.setAlignment(Pos.TOP_LEFT);
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
