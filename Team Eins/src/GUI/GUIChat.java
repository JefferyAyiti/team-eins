package GUI;



/*import com.pavlobu.emojitextflow.EmojiTextFlow;
import com.pavlobu.emojitextflow.EmojiTextFlowParameters;*///emoji chat
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import static Main.Main.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class GUIChat {
    Popup chat;
    TextField input = new TextField();
    public ScrollPane scroll = new ScrollPane();
    public VBox messages = new VBox();
    Button sendButton = new Button("Senden");

   /* final static double EMOJI_SCALE_FACTOR = 1D; // used to adjust emoji size and position in relation to other text
    final static int textSize = 15;

    EmojiTextFlowParameters emojiTextFlowParameters;
    {
        emojiTextFlowParameters = new EmojiTextFlowParameters();
        emojiTextFlowParameters.setEmojiScaleFactor(EMOJI_SCALE_FACTOR);
        emojiTextFlowParameters.setTextAlignment(TextAlignment.CENTER);
        emojiTextFlowParameters.setFont(Font.font("Verdana",
                FontWeight.BOLD,
                textSize));
        emojiTextFlowParameters.setTextColor(Color.WHITE);
    }

    private TextFlow emojiTextParser( String message){
        EmojiTextFlow dialogContainer = new EmojiTextFlow(emojiTextFlowParameters);
        dialogContainer.parseAndAppend(message);
        return dialogContainer;
    }
*///emoji chat


    public GUIChat(){
        chat = new Popup();
    }

    public void openChat(Stage owner, double X, double Y, double width){
        chat.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);
        chat.setAnchorX(X+5);
        chat.setAnchorY(Y);
        chat.setAutoHide(true);
        scroll.setOpacity(0.9);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //sendButton.setOpacity(0.8);
        //sendButton.setFont(Font.font())
        sendButton.setStyle("-fx-text-fill: black;\n" +
                "    -fx-background-color: rgba(255,255,255,0.4);\n" +
                "    -fx-fit-to-height: true;\n" +
                "    -fx-font-size: 100%;\n" +
                "    -fx-alignment: center;" +
                "    -fx-font-weight: bold;");

        VBox content = new VBox();
        content.setMaxHeight(300);
        messages.setPrefHeight(width+50);
        messages.setPrefWidth(width+70);
        messages.setMaxWidth(600);
        messages.setStyle("-fx-background-image: url('/GUI/images/oberflaeche.jpg');");
        HBox inputBox = new HBox();
        input.setPrefWidth(width+28);
        inputBox.getChildren().addAll(input, sendButton);
        onEnter();
        onPress();
        messages.getChildren().clear();
        messages.getChildren().addAll(buildChat());
        scroll.setContent(messages);
        content.getChildren().addAll(scroll, inputBox);
        chat.getContent().addAll(content);
        //chat.setAutoHide(true);
        chat.show(owner);
    }

    public void onEnter(){
        input.setOnKeyPressed(
                keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        send();
                    } });
    }


    void send() {
        try {
            server.sendMessage(input.getText(), uniqueID);
            messages.getChildren().clear();
            messages.getChildren().addAll(buildChat());
            scroll.setContent(messages);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        input.clear();
    }

    public void onPress(){
        sendButton.setOnMouseClicked( mouseEvent -> {
            send();
        });
    }

    public VBox buildChat() {
        VBox cbox = new VBox();
        List<List<String>> rec = null;


        if(server != null) {
            try {
                rec = server.getChat();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (rec != null) {
                for (List<String> zeile : rec) {
                    if (!mutelist.contains(zeile.get(0))) {
                        if (zeile.size() == 2) { // normale Nachricht
                            TextFlow flow = new TextFlow();
                            Text text1 = new Text(zeile.get(0) + ": ");
                            text1.setStyle("-fx-font-weight: bold;");

                            Text text2 = new Text(zeile.get(1));
                            text2.setStyle("-fx-font-weight: normal; ");
                            flow.getChildren().addAll(text1, text2); //Delete text2 for emoji chat
                            cbox.getChildren().add(flow);
                            /*TextFlow emoteText = emojiTextParser(zeile.get(1));  //emoji chat
                            cbox.getChildren().add(emoteText);*/
                        } else if (zeile.get(1).equals("/coinflip")) {
                            TextFlow flow = new TextFlow();
                            Text text1 = new Text("\uD83D\uDCB0    " + zeile.get(0) + " ");
                            text1.setStyle("-fx-font-weight: bold;");

                            Text text2 = new Text("wirft eine Münze und es ist ");
                            text2.setStyle("-fx-font-weight: normal; ");

                            Text text3 = new Text(zeile.get(2));
                            text3.setStyle("-fx-font-weight: bold;");

                            flow.getChildren().addAll(text1, text2, text3);
                            cbox.getChildren().add(flow);
                        } else {
                            TextFlow flow = new TextFlow();
                            Text text1 = new Text("\uD83C\uDFB2    " + zeile.get(0) + " ");
                            text1.setStyle("-fx-font-weight: bold;");

                            Text text2 = new Text(" würfelt mit einem " + zeile.get(1) + "er-Würfel eine ");
                            text2.setStyle("-fx-font-weight: normal; ");

                            Text text3 = new Text(zeile.get(2));
                            text3.setStyle("-fx-font-weight: bold;");

                            flow.getChildren().addAll(text1, text2, text3);
                            cbox.getChildren().add(flow);
                        }

                    }
                }
            }
        }
        return cbox;
    }

}
