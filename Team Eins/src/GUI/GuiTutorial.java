package GUI;

import Main.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.bcel.generic.GOTO;


import static Main.Main.*;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class GuiTutorial {

    private HBox info;

    private static Spieler[] spielerM;
    boolean chips = false;
    Text txt;
    private boolean intro = true;
    private boolean karte = false;
    private boolean next = false;
    private boolean nachziehen = false;
    private boolean karte3 = false;
    private boolean lamaKarte = false;
    private boolean karte6 = false;
    private boolean ausgestiegen;

    public void initTutorial() {
        intro = true;
        chips = false;
        Main.anzSpieler = 3;
        Main.ich = 0;
        tooltip = false;


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
        nachziehStapel.addCard(new Karte(6, true));
        nachziehStapel.addCard(new Karte(10, true));
        nachziehStapel.addCard(new Karte(2, true));


        Main.tisch.setNachziehstapel(nachziehStapel);

    }

    public void tutKartenGeben() {
        //Spieler
        Main.haende[0].addKarte(kartenErstellen(3));
        Main.haende[0].addKarte(kartenErstellen(5));
        Main.haende[0].addKarte(kartenErstellen(7));
        Main.haende[0].addKarte(kartenErstellen(6));
        Main.haende[0].addKarte(kartenErstellen(5));
        Main.haende[0].addKarte(kartenErstellen(7));

        //Karl
        Main.haende[1].addKarte(kartenErstellen(4));
        Main.haende[1].addKarte(kartenErstellen(6));
        Main.haende[1].addKarte(kartenErstellen(1));
        Main.haende[1].addKarte(kartenErstellen(3));
        Main.haende[1].addKarte(kartenErstellen(2));
        Main.haende[1].addKarte(kartenErstellen(1));

        //Paul
        Main.haende[2].addKarte(kartenErstellen(5));
        Main.haende[2].addKarte(kartenErstellen(2));
        Main.haende[2].addKarte(kartenErstellen(4));
        Main.haende[2].addKarte(kartenErstellen(4));
        Main.haende[2].addKarte(kartenErstellen(5));
        Main.haende[2].addKarte(kartenErstellen(3));


    }

    private Karte kartenErstellen(int i) {

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

    public void startTutorial() {

        System.out.println("Karl: " + spielerM[1].getCardHand().getKarte(0) + "Paul: " + spielerM[2].getCardHand().getKarte(0));
        if (intro) {
            System.out.println("intro");
            initTutorial();
            txt = new Text("Willkommen beim Tutorial "
                    + spielerM[0].getName()
                    + "! Ich bin Karl. Ich helfe dir das Spiel zu verstehen.\n"
                    + "Du kannst das Tutorial jederzeit über das Türsymbol rechts oben verlasssen."
                    + "Drücke eine beliebige Taste wenn du breit bist, dann fangen wir an...");
            //Anfang
            popUp(txt, 5, 0);
            //Chips
            spieltischGui.scene.onKeyTypedProperty().set(a -> {
                closeDialog();
                txt = new Text("""
                        Am Ende einer Runde kassierst du für die Karten auf deiner Hand Minuspunkte.
                        Also halte dich immer an die L.A.M.A.-Regel: Lege alle Minuspunkte ab.\s
                        Hast du am Ende die wenigsten Minuspunkte gewinnst du das Spiel.""");
                popUp(txt, 5, 0);
                spieltischGui.scene.onKeyTypedProperty().set(k -> {
                    closeDialog();
                    txt = new Text("""
                            Diese erhälst du am Ende einer Runde für deine verbliebenen Karten.
                            Jede Karte zählt ihren Wert als Minuspunkte. Dabei zählt jeder Wert nur einmal, 
                            beispielsweise erhältst du für alle 4er nur einmal vier Minuspunkte.
                            Die Lama Karte gibt -10 Punkte.""");
                    popUp(txt, 5, 0);

                    spieltischGui.scene.onKeyTypedProperty().set(b -> {
                        closeDialog();
                        txt = new Text("Minuspunkte werden als Chips vergeben. Ein weißer Chip gibt -1 Punkte und ein schwarzer -10.\n");
                        popUp(txt, 5, 0);

                        spieltischGui.scene.onKeyTypedProperty().set(c -> {
                            closeDialog();
                            txt = new Text("""
                                    Um dir das Spiel besser zu zeigen beginnen wir das Spiel nicht bei null.\s
                                    Paul hat breits 2 schwarze Chips und 8 weiße. Somit hat er -28 Punkte.
                                    Ich habe 2 schwarze und 3 weiße Chips und somit -23 Punkte.""");
                            popUp(txt, 5, 0);

                            spieltischGui.scene.onKeyTypedProperty().set(d -> {
                                closeDialog();
                                txt = new Text("""
                                        Du kannst jeder Zeit 10 weiße Chips gegen einen schwarzen Chip tauschen.
                                        Hast du keine Karten mehr auf der Hand darfst du nämlich einen beliebigen Chip abgeben
                                        und durch einen schwarzen Chip kannst du mehr Minuspunkte loswerden.""");
                                popUp(txt, 5, 0);

                                spieltischGui.scene.onKeyTypedProperty().set(h -> {
                                    chips = true;
                                    reloadGui();
                                    txt = new Text("Wie ich sehe hast du momentan 10 weiße Chips. Klicke auf deine Chips um sie zu tauschen.");
                                    popUp(txt, 5, 0);
                                    spieltischGui.scene.onMouseClickedProperty().set(i -> {
                                        if (spielerM[0].getBlackChips() == 3) {
                                            chips = false;
                                            karte = true;
                                            intro = false;
                                            reloadGui();
                                            startTutorial();
                                        }
                                    });


                                });
                            });
                        });
                    });
                });
            });
        }

        //legen
        else if (karte) {

            System.out.println("kartelegen");
            txt = new Text("Toll! ");
            popUp(txt, 5, 0);
            spieltischGui.scene.onKeyTypedProperty().set(x -> {
                txt = new Text("""
                        Bist du am zug, kannst du immer eine der drei Aktionen auswählen: \s
                        eine Karte ablegen,
                        eine Katze nachziehen oder
                        Aussteigen""");
                popUp(txt, 5, 0);

                spieltischGui.scene.onKeyTypedProperty().set(c -> {
                    closeDialog();
                    txt = new Text("""
                            Der Ablage bestimmt, was du ablegen darfst.\n
                            Du kannst immer nur eine Karte mit dem gleichen Wert oder einen Wert höher ablegen.
                            Das Lama ist die höhste Karte, auf sie kann nur ein Lama oder eine 1 gelegt werden.""");
                    popUp(txt, 5, 0);

                    spieltischGui.scene.onKeyTypedProperty().set(d -> {
                        closeDialog();
                        Main.tooltip = true;
                        reloadGui();
                        txt = new Text("""
                                Auf dem Ablagestapel liegt eine 2 also kannst du nur eine 2 oder eine 3 darauf legen.
                                Du kannst also nur deine 3 auf den Stapel legen.""");
                        popUp(txt, 5, 0);

                        //Karl legt 4
                        spieltischGui.scene.onMouseClickedProperty().set(z -> {
                            if (tisch.getObereKarteAblagestapel().getValue() == 3) {
                                closeDialog();
                                txt = new Text("Klasse! Jetzt bin ich am Zug ");
                                popUp(txt, 5, 0);

                                spieltischGui.scene.onKeyTypedProperty().set(e -> {
                                    spiellogik.karteLegen(spielerM[1], spielerM[1].getCardHand().getKarte(0));
                                    //reloadGui();
                                    closeDialog();
                                    txt = new Text("Ich habe eine 4 gelegt und jetzt ist Paul an der Reihe.");
                                    popUp(txt, 5, 0);

                                    //Paul legt 5
                                    spieltischGui.scene.onKeyTypedProperty().set(f -> {
                                        if (tisch.getObereKarteAblagestapel().getValue() == 4) {
                                            System.out.println("Paul");
                                            spiellogik.karteLegen(spielerM[2], spielerM[2].getCardHand().getKarte(0));
                                            reloadGui();

                                            if (tisch.getObereKarteAblagestapel().getValue() == 5) {
                                                karte = false;
                                                next = true;
                                                karte3 = true;
                                                karte6 = true;
                                                //weiter
                                                startTutorial();
                                            }
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
        else if (next) {
            System.out.println("2.Zug");
            closeDialog();
            txt = new Text("""
                    Du bist wieder an der Reihe.
                    Bedenke bei deinem nächsten Zug, dass du für Karten die du nicht ablegen kannst Minuspunkte erhälst. 
                    versuche also am besten die Karten los zu werden, die dir mehr Minuspunkte geben.
                    Doppelte Karten zählen nur einmal ihren Wert, jedoch bedeutet das auch, dass du erst beide 5er 
                    loswerden musst bevor du keine Minuspunkte dafür erhälst. 
                    Es ist also schlauer jetzt die 6 abzulegen, da du so weniger Punkte auf der Hand hast.""");
            popUp(txt, 5, 0);

            spieltischGui.scene.onMouseClickedProperty().set(a -> {
                //Karl legt 6
                if (tisch.getObereKarteAblagestapel().getValue() == 6) {
                    karte6 = false;
                    lamaKarte = true;
                    closeDialog();
                    txt = new Text("Klasse! ");
                    popUp(txt, 5, 0);
                    spieltischGui.scene.onKeyTypedProperty().set(b -> {
                        System.out.println(spielerM[1].getCardHand().getKarte(0).getValue());
                        spiellogik.karteLegen(spielerM[1], spielerM[1].getCardHand().getKarte(0));
                        //reloadGui();
                        closeDialog();
                        txt = new Text("Paul ist jetzt wieder an der Reihe.");
                        popUp(txt, 5, 0);

                        //Paul zieht nach
                        if (tisch.getAktivSpieler() == spielerM[2]) {
                            spieltischGui.scene.onKeyTypedProperty().set(x -> {
                                spiellogik.karteNachziehen(spielerM[2]);
                                //reloadGui();
                                closeDialog();
                                txt = new Text("""
                                        Er hat eine Karte gezogen, also darf er in diese Zug keine Karte mehr ablegen und du bist wieder dran.""");
                                popUp(txt, 5, 0);

                                //Spieler sollte Lama legen
                                spieltischGui.scene.onKeyTypedProperty().set(c -> {
                                    if (tisch.getAktivSpieler() == spielerM[0]) {
                                        closeDialog();
                                        txt = new Text("""
                                                Du kannst jetzt wieder eine 6 oder ein Lama legen. 
                                                Das Lama hat einen wert von 10. Hast du also mindestens ein Lama auf der Hand, erhälst du 10 Minuspunkte.
                                                Versuche als Lamas immer loszuwerden """);
                                        popUp(txt, 5, 0);

                                        //Karl legt 1
                                        spieltischGui.scene.onMouseClickedProperty().set(d -> {
                                            if (tisch.getObereKarteAblagestapel().getValue() == 10) {
                                                lamaKarte = false;
                                                spiellogik.karteLegen(spielerM[1], spielerM[1].getCardHand().getKarte(0));
                                                //reloadGui();
                                                closeDialog();
                                                txt = new Text("Auf ein Lama, kann wieder ein Lama folgen oder eine 1.");
                                                popUp(txt, 5, 0);

                                                //Paul legt 2
                                                spieltischGui.scene.onKeyTypedProperty().set(f -> {
                                                    if (tisch.getObereKarteAblagestapel().getValue() == 1) {
                                                        spiellogik.karteLegen(spielerM[2], spielerM[2].getCardHand().getKarte(0));
                                                        //reloadGui();
                                                        if (tisch.getObereKarteAblagestapel().getValue() == 2) {
                                                            nachziehen = true;
                                                            next = false;
                                                            lamaKarte = false;
                                                            startTutorial();  //weiter

                                                        }

                                                    }
                                                });

                                            }
                                        });
                                    }

                                });
                            });

                        }


                    });
                }
            });


        } else if (nachziehen) {
            System.out.println("nachziehen");
            closeDialog();
            txt = new Text("""
                    Sieht so aus als könntest du keine Karte ablegen. 
                    Du hast jetzt möglichkeit ausszusteigen oder eine Karte zu ziehen. 
                    Da ich und Paul beide noch ein paar Karten haben und die Summe deiner Hand noch sehr hoch ist, ist es besser eine Karte zu ziehen.
                    Klicke auf den Stapel um eine neue Karte aufzunehmen. Dein Zug ist danach beendet. """);
            popUp(txt, 5, 0);
            //Karl legt 3
            spieltischGui.scene.onMouseClickedProperty().set(a -> {
                if (tisch.getAktivSpieler() == spielerM[1]) {
                    spiellogik.karteLegen(spielerM[1], spielerM[1].getCardHand().getKarte(0));
                    //reloadGui();

                    //Paul legt 4
                    spieltischGui.scene.onKeyTypedProperty().set(b -> {
                        if (tisch.getAktivSpieler() == spielerM[2]) {
                            spiellogik.karteLegen(spielerM[2], spielerM[2].getCardHand().getKarte(0));
                            //reloadGui();
                            //Spieler
                            spieltischGui.scene.onKeyTypedProperty().set(c -> {
                                txt=new Text("Du bist dran");
                                popUp(txt,5,0);
                                spieltischGui.scene.onMouseClickedProperty().set(d->{
                                    if(tisch.getAktivSpieler()==spielerM[1]){
                                        closeDialog();
                                        txt=new Text("""
                                        Ich kann keine Karte mehr ablegen. 
                                        Da ich nur eine 1 und 2 auf der Hand habe, steige ich lieber sicherheitshalber aus.
                                        Aussteigen kannst du in dem du auf die Hand neben deinen Chips klickst.""");

                                        popUp(txt,5,0);

                                        spieltischGui.scene.onMouseClickedProperty().set(e -> {
                                        spielerM[1].aussteigen();
                                        //reloadGui();
                                        closeDialog();
                                        txt=new Text(""" 
                                        Wenn ein Spieler ausgestiegen ist, kannst du das daran erkennen, dass seine Karten grau sind.
                                        Ein Spieler der ausgestiegen ist, kann er den Rest einer Runde keine Aktionen mehr ausführen.
                                        Sind alle Spieler ausgestiegen, endet eine Runde.
                                        """);
                                        popUp(txt,5,0);
                                        spieltischGui.scene.onMouseClickedProperty().set(f -> {
                                            if(tisch.getAktivSpieler()==spielerM[2]){
                                                spielerM[2].aussteigen();
                                                //reloadGui();
                                                closeDialog();
                                                txt=new Text(""" 
                                                Paul ist also auch ausgestiegen. 
                                                Dadurch darfst du so viele Karten ablegen wie du kannst, darfst aber keine Karten mehr nachziehen.
                                                """);
                                                popUp(txt,5,0);
                                                spieltischGui.scene.onMouseClickedProperty().set(g->{
                                                    if(spielerM[0]==tisch.getAktivSpieler()){
                                                        nachziehen=false;
                                                        ausgestiegen = true;
                                                        startTutorial();
                                                    }
                                                });

                                            }

                                        });
                                    });
                                    }
                                });

                            });


                        }
                    });
                }
            });
        }else if(ausgestiegen){
            closeDialog();
            txt=new Text(""" 
            Vorsicht! Du hast jetzt noch eine 5,6 und ein Lama. 
            legst du sie in der Richtigen Reihenfolge ab, kannst du alle Karten loswerden und einen Chip abgeben.
            """);
            popUp(txt,5,0);
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
        info.setTranslateX(30 * zoomfactor);
        info.setTranslateY(10 * zoomfactor);
        info.setSpacing(10);
        info.getChildren().addAll(bild, flow);
        flow.setStyle("-fx-background-color: gray;");
        Main.spieltischGui.getGridPane().add(info, 0, 0, 4, 1);
    }

    void closeDialog() {
        spieltischGui.getGridPane().getChildren().remove(info);
    }

    void reloadGui() {
        spieltischGui.buildStage(Main.classPrimaryStage);
    }

    public boolean wrongCard(int i) {
        if (playMode == 0 && tutorialAn && spielerM[0] == tisch.getAktivSpieler()) {
            if (next && karte6 && spielerM[0].getCardHand().getKarte(i).getValue() != 6) {
                popUp(new Text("lege die 6 Ab."), 5, 0);
                return true;
            } else if (karte && !karte3 && spielerM[0].getCardHand().getKarte(i).getValue() != 3) {
                popUp(new Text("lege die 3 Ab."), 5, 0);
                return true;

            } else if (next && lamaKarte && spielerM[0].getCardHand().getKarte(i).getValue() != 10) {
                popUp(new Text("lege ein Lama Ab."), 5, 0);
                return true;
            } else {
                return false;
            }
        } else {// Tutorial aus
            return false;

        }
    }


}

