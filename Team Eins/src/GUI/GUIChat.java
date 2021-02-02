package GUI;



import com.pavlobu.emojitextflow.EmojiTextFlow;
import com.pavlobu.emojitextflow.EmojiTextFlowParameters;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.transform.Translate;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import static Main.Main.*;
import Main.Main;
import javafx.stage.Window;


import java.rmi.RemoteException;
import java.util.List;


public class GUIChat {
    Popup chat;
    TextField input = new TextField();
    public ScrollPane scroll = new ScrollPane();
    public VBox messages = new VBox();
    Button sendButton = new Button("Senden");


    final static double EMOJI_SCALE_FACTOR = 1.5D; // used to adjust emoji size and position in relation to other text
    final static int textSize = 13;

    EmojiTextFlowParameters emojiTextFlowParameters;
    {
        emojiTextFlowParameters = new EmojiTextFlowParameters();
        emojiTextFlowParameters.setEmojiScaleFactor(EMOJI_SCALE_FACTOR);
        emojiTextFlowParameters.setTextAlignment(TextAlignment.LEFT);
        emojiTextFlowParameters.setFont(Font.font("Verdana",
                FontWeight.BOLD,
                textSize));
    }

    private TextFlow emojiTextParser( String message){
        EmojiTextFlow dialogContainer = new EmojiTextFlow(emojiTextFlowParameters);
        dialogContainer.parseAndAppend(message);
        return dialogContainer;
    }
//emoji chat


    public GUIChat(){
        chat = new Popup();
    }

    /**
     * Minimiert den geöffneten Chat
     */
    public void hideChat() {
        chat.hide();
    }


    public void openChat(Stage owner, double height, double width){
        //chat.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT);

        chat.setX(owner.getScene().getWindow().getX() + 5);
        chat.setY(owner.getScene().getWindow().getY() + 35);
        //chat.setAutoFix(true);
        scroll.setOpacity(einstellung.getSliderValue());
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sendButton.setStyle("-fx-text-fill: black;\n" +
                "    -fx-background-color: rgba(255,255,255,0.6);\n" +
                "    -fx-fit-to-height: true;\n" +
                "    -fx-font-size: 90%;\n" +
                "    -fx-alignment: center;" +
                "    -fx-font-weight: bold;");

        VBox content = new VBox();
        content.setPrefWidth(170 *zoomfactor);
        messages.setPrefHeight(240*zoomfactor);
        messages.setMinWidth(160 *zoomfactor);
        messages.setMaxWidth(160*zoomfactor);
        messages.setStyle("-fx-background-image: url('/GUI/images/oberflaeche.jpg');");
        HBox inputBox = new HBox();

        //input.setStyle("-fx-opacity: 0.9");
        sendButton.setPrefWidth(60);
        input.setPrefWidth(170*zoomfactor-60);
        //inputBox.minWidth(width);
        //inputBox.maxWidth(600*zoomfactor);
        input.setId("eingabe");
        //inputBox.prefWidthProperty().bind(content.prefWidthProperty().multiply(zoomfactor));
        inputBox.getChildren().addAll(input, sendButton);
        onEnter();
        onPress();
        messages.getChildren().clear();
        messages.getChildren().addAll(buildChat());
        scroll.setContent(messages);
        content.getChildren().addAll(scroll, inputBox);
        chat.getContent().addAll(content);
        //chat.setAutoHide(true);
        scroll.vvalueProperty().bind(messages.heightProperty());
        chat.show(owner);
    }

    public void onEnter(){
        input.setOnKeyPressed(
                keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER){
                        send();
                    } });
    }


    /**
     * Sendet den Inhalt des Textfeldes an den Server und updatet direkt die eigene Chatbox
     */
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

    /**
     * @return formatierte Chatnachrichten
     */
    public VBox buildChat() {
        VBox cbox = new VBox();
        List<List<String>> rec = null;


        if(server != null) {
            try {
                rec = server.getChat(schimpfFilter);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (rec != null) {
                for (List<String> zeile : rec) {
                    if (!mutelist.contains(zeile.get(0))) {
                        if (zeile.size() == 2) { // normale Nachricht
                            int sichtbarkeit = sichtbarFuerMich(zeile);

                            if(sichtbarkeit > 0) {
                            /*
                            TextFlow flow = new TextFlow();


                            Text text2 = new Text(zeile.get(1));
                            text2.setStyle("-fx-font-weight: normal; ");
                             */

                                String name = zeile.get(0);
                                String message = zeile.get(1);
                                if(sichtbarkeit == 2) {
                                    name = name+" (privat)";
                                    emojiTextFlowParameters.setTextColor(Color.rgb(245, 96, 66,1));
                                    try {
                                        message = message.substring(myName.length()+2);
                                    } catch (Exception e) {
                                        message = "";
                                    }

                                }else{
                                    emojiTextFlowParameters.setTextColor(Color.WHITE);
                                }
                                String text = (message);
                                TextFlow emoteText = emojiTextParser(text); //emoji chat
                                emoteText.setLineSpacing(10);
                                emoteText.setId("emoji");

                                Text nametext = new Text(name+ ": ");
                                nametext.setStyle("-fx-font-weight: bold;");
                                if(sichtbarkeit == 2) {
                                    nametext.setFill(Color.rgb(245, 96, 66,1));
                                }
                                TextFlow msg = new TextFlow(nametext, emoteText);
                                cbox.getChildren().add(msg);
                            }

                        } else if (zeile.get(1).equals("/coinflip")) {
                            TextFlow flow = new TextFlow();
                            flow.setId("coinflip");
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
                            flow.setId("roll");

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


    /**
     * @param msg Prüft ob eine Chatnachricht privat ist und falls ja,
     *            ob sie für mich oder von mir is
     * @return int das die sichtbarkeit anzeigt, siehe Code
     */
    int sichtbarFuerMich(List<String> msg) {
        String content = msg.get(1);
        if(content.charAt(0) == '@') { //private nachricht
            if(content.length() >= myName.length()+2 &&
                    content.substring(0, myName.length()+2).equals("@"+myName+" ") ||
            msg.get(0).equals(myName)) {
                return 2; //sichtbar nur für mich
            } else
                return 0; //nicht sichtbar für mich
        }
        return 1; //sichtbar für jeden
    }

    public void reposition(Stage owner){
        chat.setX(owner.getScene().getWindow().getX() + 5);
        chat.setY(owner.getScene().getWindow().getY() + 35);
    }

    public void scaleTransparenz(){
        scroll.setOpacity(einstellung.getSliderValue());
    }
}
