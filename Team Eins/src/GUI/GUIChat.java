package GUI;


import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.util.ArrayList;


public class GUIChat {
    static ArrayList<String> chatrecord = new ArrayList<>();
    Popup chat;
    private TextField input = new TextField();
    private ScrollPane scroll = new ScrollPane();
    private TextArea messages = new TextArea();
    VBox chatlog;



    public GUIChat(){
        chat = new Popup();
    }

    public void openChat(Stage owner){

        chat.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);
        chat.setAnchorX(50);
        chat.setAnchorY(900);
        chat.setAutoHide(true);
        VBox content = new VBox();
        content.setMaxHeight(300);
        messages.setEditable(false);
        onEnter();
        scroll.setContent(messages);
        content.getChildren().addAll(scroll,input);
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
                        input.clear();
                    } });
    }


}
