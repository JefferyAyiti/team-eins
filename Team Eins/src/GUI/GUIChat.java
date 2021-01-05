package GUI;


import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import static Main.Main.*;

import java.rmi.RemoteException;
import java.util.ArrayList;


public class GUIChat {
    static ArrayList<String> chatrecord = new ArrayList<>();
    Popup chat;
    public TextField input = new TextField();
    private ScrollPane scroll = new ScrollPane();
    public  TextFlow messages = new TextFlow();
    private Button sendButton = new Button("Send");




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



        VBox content = new VBox();
        content.setMaxHeight(300);
        messages.setPrefHeight(width+50);
        messages.setPrefWidth(width+70);
        messages.setMaxWidth(600);
        //messages.setStyle("-fx-background-color:black;");
        messages.setStyle("-fx-background-image: url('/GUI/images/oberflaeche.jpg');");
        HBox inputBox = new HBox();
        input.setPrefWidth(width+28);
        inputBox.getChildren().addAll(input, sendButton);
        onEnter();
        onPress();
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
                        chatrecord.add(input.getText());
                        Text newMsg = new Text(input.getText() + "\n");
                        newMsg.setFill(Color.WHITE);
                        messages.getChildren().add(newMsg);
                        //messages.setStyle("-fx-background-color:black;");
                        messages.setStyle("-fx-background-image: url('/GUI/images/oberflaeche.jpg');");
                        //to change
                        /*try {
                            server.updateClients(input.getText());
                        } catch (RemoteException e) {
                        }*/
                        input.clear();
                    } });
    }

    public void addText(String msg){
        messages.getChildren().add(new Text(msg + "\n"));
    } // to change

    public void onPress(){
        sendButton.setOnMouseClicked( mouseEvent -> {
            chatrecord.add(input.getText());
            //messages.getChildren().add(new Text(input.getText() + "\n")); //to change
            Text newMsg = new Text(input.getText() + "\n");
            newMsg.setFill(Color.WHITE);
            messages.getChildren().add(newMsg);
            messages.setStyle("-fx-background-image: url('/GUI/images/oberflaeche.jpg');");
            //messages.setStyle("-fx-background-color:black;");
            /*try {
                server.updateClients(input.getText());
            } catch (RemoteException e) {

            }*/
            input.clear();
        });
    }

}
