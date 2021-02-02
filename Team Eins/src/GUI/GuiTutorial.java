package GUI;

import Main.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;



import static Main.Main.*;

public class GuiTutorial {

    private HBox info;
    private boolean next=false;
    private static Spieler[] spielerM;
    boolean chips = false;





    public void initTutorial() {
        intro=true;
        Main.anzSpieler = 3;
        Main.ich = 0;

        //initialisiere Spieler mit handkarten
        Main.haende = new Hand[Main.anzSpieler];
        spielerM = new Spieler[Main.anzSpieler];

        //spieler[0]= new Bot("Spieler",2);

        spielerM[0] = new Spieler(Main.myName, Main.uniqueID);
        spielerM[1] = new Bot("Karl ", 4);
        spielerM[2] = new Bot("Paul ", 4);

        //Punkte
        spielerM[0].setBlackChips(2);
        spielerM[0].setWhiteChips(10);
        spielerM[0].setPoints(-30);
        //Karl
        spielerM[1].setBlackChips(2);
        spielerM[1].setWhiteChips(3);
        spielerM[1].setPoints(-23);
        //Paul
        spielerM[2].setBlackChips(2);
        spielerM[2].setWhiteChips(8);
        spielerM[2].setPoints(-28);



        Main.tisch = new Tisch(spielerM);
        Main.spiellogik = new Spiellogik(Main.tisch);
        Main.spiellogik.initNeueRunde();
        Main.resize(true);
        Main.tisch.takeChips(21, 5);
    }

    public void tutNachziehstapel() {
        Stapel nachziehStapel = new Stapel(true);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                switch (i) {
                    case 0 -> nachziehStapel.addCard(new Karte(1, true));
                    case 1 -> nachziehStapel.addCard(new Karte(2, true));
                    case 2 -> nachziehStapel.addCard(new Karte(3, true));
                    case 3 -> nachziehStapel.addCard(new Karte(4, true));
                    case 4 -> nachziehStapel.addCard(new Karte(5, true));
                    case 5 -> nachziehStapel.addCard(new Karte(6, true));
                    case 6 -> nachziehStapel.addCard(new Karte(10, true));
                }
            }
        }
        nachziehStapel.addCard(new Karte(5, true));
        nachziehStapel.addCard(new Karte(10, true));
        nachziehStapel.addCard(new Karte(2, true));


        Main.tisch.setNachziehstapel(nachziehStapel);

    }
    public void tutKartenGeben(){
        //Spieler
        Main.haende[0].addKarte(kartenErstellen(3));
        Main.haende[0].addKarte(kartenErstellen(4));
        Main.haende[0].addKarte(kartenErstellen(7));
        Main.haende[0].addKarte(kartenErstellen(6));
        Main.haende[0].addKarte(kartenErstellen(5));
        Main.haende[0].addKarte(kartenErstellen(7));

        //Karl
        Main.haende[1].addKarte(kartenErstellen(4));
        Main.haende[1].addKarte(kartenErstellen(6));
        Main.haende[1].addKarte(kartenErstellen(1));
        Main.haende[1].addKarte(kartenErstellen(3));
        Main.haende[1].addKarte(kartenErstellen(7));
        Main.haende[1].addKarte(kartenErstellen(1));


        //Paul
        Main.haende[2].addKarte(kartenErstellen(5));
        Main.haende[2].addKarte(kartenErstellen(4));
        Main.haende[2].addKarte(kartenErstellen(2));
        Main.haende[2].addKarte(kartenErstellen(4));
        Main.haende[2].addKarte(kartenErstellen(5));
        Main.haende[2].addKarte(kartenErstellen(3));


    }

    private Karte kartenErstellen(int i){
        
        return switch (i) {
            case 2 -> new Karte(2, true);
            case 3 -> new Karte(3, true);
            case 4 -> new Karte(4, true);
            case 5 -> new Karte(5, true);
            case 6 -> new Karte(6, true);
            case 7 -> new Karte(10, true);
            default -> new Karte(1, true);
        };






    }
    Text txt;
    boolean intro=true;
    boolean karte=false;
    boolean aussetzten=false;

    public void startTutorial() {
        System.out.println("Karl: "+spielerM[1].getCardHand().getKarte(0) + "Paul: "+ spielerM[2].getCardHand().getKarte(0));
        if(intro) {

            txt = new Text("Willkommen beim Tutorial "
                    + spielerM[0].getName()
                    + "! Ich bin Karl. Ich helfe dir das Spiel zu verstehen.\n"
                    + "Klicke einfach auf den Bildschirm wenn du breit bist, dann fangen wir an...");
            //Anfang
            popUp(txt, 5, 0);
            //Chips
            Main.spieltischGui.getGridPane().onMouseClickedProperty().set(a -> {
                closeDialog();
                txt = new Text("""
                        Am Ende einer Runde kassierst du für die Karten auf deiner Hand Minuspunkte.
                        Also halte dich immer an die L.A.M.A.-Regel: Lege alle Minuspunkte ab.\s
                        Hast du am Ende die wenigsten Minuspunkte gewinnst du das Spiel.""");
                popUp(txt, 5, 0);

                Main.spieltischGui.getGridPane().onMouseClickedProperty().set(b -> {
                    closeDialog();
                    txt = new Text("Minuspunkte werden als Chips vergeben. Ein weißer Chip gibt -1 Punkte und ein schwarzer -10.\n");
                    popUp(txt, 5, 0);

                    Main.spieltischGui.getGridPane().onMouseClickedProperty().set(c -> {
                        closeDialog();
                        txt = new Text("""
                                Um dir das Spiel besser zu zeigen beginnen wir das Spiel nicht bei null.\s
                                Paul hat breits 2 schwarze Chips und 8 weiße. Somit hat er -28 Punkte.
                                Ich habe 2 schwarze und 3 weiße Chips und somit -23 Punkte.""");
                        popUp(txt, 5, 0);

                        Main.spieltischGui.getGridPane().onMouseClickedProperty().set(d -> {
                            closeDialog();
                            txt = new Text("""
                                    Du kannst jeder Zeit 10 weiße Chips gegen einen schwarzen Chip tauschen.
                                    Hast du keine Karten mehr auf der Hand darfst du nämlich einen beliebigen Chip abgeben
                                    und durch einen schwarzen Chip kannst du mehr Minuspunkte loswerden.""");
                            popUp(txt, 5, 0);

                            Main.spieltischGui.getGridPane().onMouseClickedProperty().set(h -> {
                                chips = true;
                                reloadGui();
                                txt = new Text("Wie ich sehe hast du momentan 10 weiße Chips. Klicke auf deine Chips um sie zu tauschen.");
                                popUp(txt, 5, 0);

                                Main.spieltischGui.getGridPane().onMouseClickedProperty().set(e -> {
                                    if (spielerM[0].getBlackChips() == 3) {
                                        chips = false;
                                        karte = true;
                                        intro = false;
                                        reloadGui();
                                        txt = new Text("Toll! ");
                                        popUp(txt, 5, 0);

                                        Main.spieltischGui.getGridPane().onMouseClickedProperty().set(f -> startTutorial());

                                    }
                                });
                            });
                        });
                    });
                });
            });
        }
        
        //legen
       if(karte) {
           Main.spieltischGui.getGridPane().onMouseClickedProperty().set(b -> {
               txt = new Text("Du kannst immer eine der drei Aktionen auswählen: \n" +
                       "eine Karte ablegen," +
                       "eine Katze nachziehen oder" +
                       "Aussteigen");
               popUp(txt, 5, 0);

               Main.spieltischGui.getGridPane().onMouseClickedProperty().set(c -> {
                   closeDialog();
                   txt = new Text("versuchen wir erst eine Karte abzulegen. Der Ablage bestimmt, was du ablegen darfst.\n+" +
                           "Du kannst immer nur eine Karte mit dem gleichen Wert oder einen Wert höher ablegen." +
                           "Das Lama ist die höhste Karte, auf sie kann nur ein Lama oder eine 1 gelegt werden.");
                   popUp(txt, 5, 0);

                   Main.spieltischGui.getGridPane().onMouseClickedProperty().set(d -> {
                       closeDialog();
                       Main.tooltip = true;
                       reloadGui();
                       txt = new Text(" Auf dem Ablagestapel liegt eine 2 also kannst du nur eine 2 oder eine 3 darauf legen.\n" +
                               "Du kannst also nur deine 3 auf den Stapel legen.");
                       popUp(txt, 5, 0);

                       Main.spieltischGui.getGridPane().onMouseClickedProperty().set(i -> {
                           if(tisch.getObereKarteAblagestapel().getValue()==3) {
                           closeDialog();
                           txt = new Text("Klasse! Jetzt bin ich am Zug ");
                           popUp(txt, 5, 0);

                               Main.spieltischGui.getGridPane().onMouseClickedProperty().set(e -> {
                                   while (!spiellogik.karteLegen(spielerM[1], spielerM[1].getCardHand().getKarte(0)));


                                           Main.spieltischGui.getGridPane().onMouseClickedProperty().set(g -> {

                                               txt = new Text("Ich habe eine 4 gelegt und jetzt ist Paul an der Reihe.");
                                               popUp(txt, 5, 0);
                                                System.out.println(tisch.getAktivSpieler().getName()+ " " + spielerM[1].getName());

                                                   System.out.println(tisch.getAktivSpieler() == spielerM[1]);
                                                   while(!spiellogik.karteLegen(spielerM[2], spielerM[2].getCardHand().getKarte(0)));

                                                   if (tisch.getObereKarteAblagestapel().getValue() == 5) {
                                                       karte = false;
                                                       next = true;
                                                       startTutorial();
                                                   }


                                           });
                                       
                               });

                           }
                       });

                   });
               });
           });
       }
       // 2. Zug
        if (next){

        }


    }


    void popUp(Text text, double X, double Y) {

        TextFlow flow = new TextFlow();
        /* Image bubble = new Image("GUI/images/speechBubble.png",100,50,false,false);
        BackgroundImage myBI = new BackgroundImage(bubble,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, false, true));
        flow.setBackground(new Background(myBI));

        */



        Image avatar = new Image("GUI/images/clipart2498304.png", 50 * Main.zoomfactor, 70 * Main.zoomfactor, false, false);
        ImageView bild = new ImageView(avatar);

        //Label erklareung = new Label(text);
        text.setFont(new Font("arial black", 13));
        text.setStyle("-fx-fill: black;");
        text.setTranslateX(X);
        text.setTranslateY(Y);
        flow.getChildren().add(text);
        flow.setMinWidth(450 * Main.zoomfactor);

        info = new HBox();
        info.setTranslateX(30*zoomfactor);
        info.setTranslateY(10*zoomfactor);
        info.setSpacing(10);
        info.getChildren().addAll(bild, flow);
        flow.setStyle("-fx-background-color: gray;");
        Main.spieltischGui.getGridPane().add(info, 0, 0, 4, 1);
    }

    void closeDialog(){
        spieltischGui.getGridPane().getChildren().remove(info);
    }
    void reloadGui(){
        spieltischGui.buildStage(Main.classPrimaryStage);
    }




}

