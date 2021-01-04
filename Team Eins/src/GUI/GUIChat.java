package GUI;


import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public TextArea messages = new TextArea();
    private Button sendButton = new Button("Send");




    public GUIChat(){
        chat = new Popup();
    }

    public void openChat(Stage owner, double X, double Y, double width){

        chat.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
        chat.setAnchorX(X);
        chat.setAnchorY(Y);
        chat.setAutoHide(true);
        scroll.setOpacity(0.7);

        VBox content = new VBox();
        content.setMaxHeight(300);
        messages.setEditable(false);
        messages.setWrapText(true);
        messages.setPrefWidth(width+50);
        messages.setMaxWidth(600);
        HBox inputBox = new HBox();
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
                        messages.appendText(input.getText() + "\n");
                        /*try {
                            server.updateClients(input.getText());
                        } catch (RemoteException e) {

                        }*/
                        input.clear();
                    } });
    }

    public void addText(String msg){
        messages.appendText(msg + "\n");
    }

    public void onPress(){
        sendButton.setOnMouseClicked( mouseEvent -> {
            chatrecord.add(input.getText());
            messages.appendText(input.getText() + "\n");
            /*try {
                server.updateClients(input.getText());
            } catch (RemoteException e) {

            }*/
            input.clear();
        });
    }

}
