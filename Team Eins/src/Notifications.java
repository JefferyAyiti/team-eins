import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Notifications {

    public static void anfang(){

    }

    public static void rundeEnde(){
        Stage prompt = new Stage();

        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initStyle(StageStyle.UNDECORATED);
        prompt.setResizable(false);
        prompt.setHeight(600);
        prompt.setWidth(500);
        prompt.setOpacity(0.9f);



        BorderPane layout = new BorderPane();


        //Header
        Label text = new Label("Die Runde ist beendet.");
        text.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        text.setTextFill(Color.ORANGERED);
        VBox textBox = new VBox(text);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(30f));
        layout.setTop(textBox);

        //Rangliste
        VBox rang = new VBox();

        Text list = new Text();
        list.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        rang.getChildren().add(list);
        layout.setCenter(rang);
        rang.setPadding(new Insets(30f));

        //Buttons
        Button button1 = new Button("Weiter");
        Button button2 = new Button("Spiel Verlassen");

        button1.setOnMouseClicked(mouseEvent -> prompt.close());
        button2.setOnMouseClicked(mouseEvent -> prompt.close());

        HBox buttons = new HBox();
        buttons.getChildren().addAll(button1,button2);
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.setPadding(new Insets(30f));

        buttons.setSpacing(32f);
        layout.setBottom(buttons);

        Scene scene = new Scene(layout);
        prompt.setScene(scene);
        prompt.showAndWait();
    }

    public static void spielEnde(){
        Stage prompt = new Stage();

        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initStyle(StageStyle.UNDECORATED);
        prompt.setResizable(false);
        prompt.setHeight(600);
        prompt.setWidth(500);
        prompt.setOpacity(0.9f);



        BorderPane layout = new BorderPane();


        //Header
        Label text = new Label("Das Spiel ist beendet.");
        text.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
        text.setTextFill(Color.ORANGERED);
        VBox textBox = new VBox(text);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPadding(new Insets(30f));
        layout.setTop(textBox);

        //Rangliste
        VBox rang = new VBox();

        Text list = new Text();
        list.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        rang.getChildren().add(list);
        layout.setCenter(rang);
        rang.setPadding(new Insets(30f));

        //Buttons
        Button button1 = new Button("Neues Spiel");
        Button button2 = new Button("Spiel Verlassen");

        button1.setOnMouseClicked(mouseEvent -> prompt.close());
        button2.setOnMouseClicked(mouseEvent -> prompt.close());

        HBox buttons = new HBox();
        buttons.getChildren().addAll(button1,button2);
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.setPadding(new Insets(30f));

        buttons.setSpacing(32f);
        layout.setBottom(buttons);

        Scene scene = new Scene(layout);
        prompt.setScene(scene);
        prompt.showAndWait();

    }
}
