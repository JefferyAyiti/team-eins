package GUI;

import Main.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import static Main.Main.*;
import static java.lang.Thread.sleep;

public class GuiTutorial {

    private HBox info;

    private static Spieler[] spielerM;
    boolean chips = false;
    boolean stapel = false;
    Text txt;
    boolean spielerZug = false;
    private boolean intro = true;
    private boolean karte = false;
    private boolean next = false;
    private boolean nachziehen = false;
    private boolean lamaKarte = false;
    private boolean karte6 = false;
    private boolean ausgestiegen;
    private Spieler karl;
    private Spieler paul;

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

        karl = tisch.getSpielerList()[1];
        paul = tisch.getSpielerList()[2];
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
        nachziehStapel.addCard(new Karte(1, true));
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
        if (intro) {
            System.out.println("intro");

            txt = new Text("Willkommen beim Tutorial "
                    + spielerM[0].getName()
                    + "! Ich bin Karl.\n" +
                    "\nDu kannst das Tutorial jederzeit über das Türsymbol rechts oben verlasssen.\n" +
                    "\nDrücke eine beliebige Taste wenn du soweit bist.");
            //Anfang
            popUp(txt, 25, 10, 145);
            //Chips
            spieltischGui.scene.onKeyTypedProperty().set(a -> {
                txt = new Text("""
                        Ziel des Spiels ist es, die wenigsten Minuspunkte zu haben.
                        Diese kassierst du am Ende jeder Runde für die übrigen Karten in deiner Hand.\n
                        Also halte dich immer an die L.A.M.A.-Regel: Lege alle Minuspunkte ab. """);
                popUp(txt, 25, 15, 150);
                spieltischGui.scene.onKeyTypedProperty().set(k -> {
                    txt = new Text("""
                            Jede Karte zählt ihren Wert als Minuspunkte. Dabei zählt jeder Wert aber nur einmal.
                             
                            Du würdest z.B. für alle 5er auf deiner Hand, nur einmal fünf Minuspunkte erhalten.
                                                        
                            Die Lama Karte gibt -10 Punkte.""");
                    popUp(txt, 25, 10, 150);

                    spieltischGui.scene.onKeyTypedProperty().set(b -> {
                        txt = new Text("""
                                Minuspunkte werden als Chips vergeben.                                
                                Ein weißer Chip gibt -1 und ein schwarzer -10 Punkte.
                                                                
                                Das Spiel endet sobald ein Mitspieler 40 oder mehr Minuspunkte hat.""");
                        popUp(txt, 15, 15, 130);

                        spieltischGui.scene.onKeyTypedProperty().set(c -> {
                            txt = new Text("""
                                    Um dir alles zu zeigen, fangen wir ausnahmsweise nicht bei der ersten Runden an.
                                    Wir haben also schon ein paar Runden gespielt. \n
                                    Paul hat breits 2 schwarze Chips und 8 weiße. Somit hat er -28 Punkte.
                                    Ich habe 2 schwarze und 3 weiße Chips und somit -23 Punkte.""");
                            popUp(txt, 25, 10, 150);

                            spieltischGui.scene.onKeyTypedProperty().set(d -> {
                                txt = new Text("""
                                        Hast du 10 weiße Chips, kannst du diese jederzeit gegen einen schwarzen Chip tauschen.
                                                                                
                                        Achte darauf, denn hast du keine Karten mehr auf der Hand darfst du einen beliebigen Chip abgeben.
                                        Hast du also einen schwarzen Chip kannst du mehr Minuspunkte loswerden.""");
                                popUp(txt, 25, 10, 150);

                                spieltischGui.scene.onKeyTypedProperty().set(h -> {
                                    chips = true;
                                    reloadGui();
                                    txt = new Text("""
                                            Wie ich sehe hast du momentan 10 weiße Chips. 
                                            Klicke auf deine Chips um sie zu tauschen.""");
                                    popUp(txt, 25, 13, 50);
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
            popUp(txt, 25, 10, 50);
            spieltischGui.scene.onKeyTypedProperty().set(x -> {
                txt = new Text("""
                        Bist du am Zug, kannst du immer eine der drei Aktionen auswählen:
                                                
                        - eine Karte ablegen,
                        - eine Karte nachziehen oder
                        - Aussteigen""");
                popUp(txt, 25, 10, 150);

                spieltischGui.scene.onKeyTypedProperty().set(c -> {
                    txt = new Text("""
                            Der Ablagestapel bestimmt, was du ablegen darfst.\n
                            Du kannst immer nur eine Karte mit dem gleichen Wert oder einen Wert höher ablegen.
                            Das Lama ist die höhste Karte, auf sie kann nur ein Lama oder eine 1 gelegt werden.""");
                    popUp(txt, 25, 10, 150);

                    spieltischGui.scene.onKeyTypedProperty().set(d -> {
                        closeDialog();
                        Main.tooltip = true;
                        spielerZug = true;
                        reloadGui();
                        txt = new Text("""
                                Auf dem Ablagestapel liegt momentan eine 2. Darauf folgen können nur 2er oder 3er.
                                Du kannst also nur deine 3 auf den Stapel legen.
                                
                                Um eine Karte abzulegen, klicke einfach auf die gewünschte Karte oder zieh sie zum Ablagestapel.""");
                        popUp(txt, 25, 15, 150);

                        //Karl legt 4
                        spieltischGui.scene.onMouseClickedProperty().set(z -> {
                            popUp(txt, 25, 10, 80);
                            if (tisch.getObereKarteAblagestapel().getValue() == 3) {
                                spielerZug = false;
                                reloadGui();
                                txt = new Text("Klasse! Jetzt bin ich am Zug ");
                                popUp(txt, 25, 10, 20);

                                spieltischGui.scene.onKeyTypedProperty().set(e -> {
                                    spiellogik.karteLegen(karl, karl.getCardHand().getKarte(0));
                                    reloadGui();
                                    txt = new Text("Ich habe eine 4 gelegt und jetzt ist Paul an der Reihe.");
                                    popUp(txt, 25, 10, 50);

                                    //Paul legt 5
                                    spieltischGui.scene.onKeyTypedProperty().set(g -> {
                                        popUp(txt, 25, 10, 50);
                                        if (tisch.getObereKarteAblagestapel().getValue() == 4) {
                                            spiellogik.karteLegen(paul, paul.getCardHand().getKarte(0));
                                            reloadGui();

                                            if (tisch.getObereKarteAblagestapel().getValue() == 5) {
                                                karte = false;
                                                next = true;
                                                karte6 = true;
                                                //weiter
                                                if (tisch.getAktivSpieler() == spielerM[0]) {
                                                    txt = new Text("""
                                                            Da Paul eine 5 gelegt hat, hast du jetzt zwei Auswahlmöglichkeiten.
                                                                                                                        
                                                            Aber bevor du legst...""");
                                                    popUp(txt, 25, 10, 50);
                                                    startTutorial();

                                                }

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
            popUp(txt, 25, 10, 50);

            spieltischGui.scene.onKeyTypedProperty().set(w -> {
                txt = new Text("Bedenke bei deinem nächsten Zug, dass du für Karten die du nicht ablegen kannst Minuspunkte erhälst.\n\n" +
                        "Da Doppelte Karten nur einmal ihren Wert zählen bedeutet das auch, dass du erst beide 5er loswerden musst bevor du keine Minuspunkte dafür erhälst.");
                popUp(txt, 25, 15, 150);

                spieltischGui.scene.onKeyTypedProperty().set(o -> {
                    spielerZug = true;
                    reloadGui();
                    txt = new Text("Es ist also schlauer jetzt die 6 abzulegen, da du so weniger Punkte auf der Hand hast.");
                    popUp(txt, 25, 10, 50);

                    spieltischGui.scene.onMouseClickedProperty().set(a -> {
                        popUp(txt, 25, 10, 50);
                        if (tisch.getObereKarteAblagestapel().getValue() == 6) {
                            spielerZug = false;
                            reloadGui();
                            karte6 = false;
                            lamaKarte = true;
                            txt = new Text("Klasse! ");
                            popUp(txt, 25, 10, 50);

                            //Karl legt 6
                            spieltischGui.scene.onKeyTypedProperty().set(b -> {
                                spiellogik.karteLegen(karl, karl.getCardHand().getKarte(0));
                                reloadGui();
                                closeDialog();
                                txt = new Text("Paul ist jetzt wieder an der Reihe.");
                                popUp(txt, 25, 10, 50);
                                spieltischGui.scene.onKeyTypedProperty().set(x -> {
                                    popUp(txt, 25, 10, 50);
                                    //Paul zieht nach
                                    if (tisch.getAktivSpieler() == paul) {
                                        spiellogik.karteNachziehen(paul);
                                        reloadGui();
                                        txt = new Text("""
                                                Er hat eine Karte gezogen, also darf er in diese Zug keine Karte mehr ablegen und du bist wieder dran.""");
                                        popUp(txt, 25, 10, 50);

                                        //Spieler sollte Lama legen
                                        spieltischGui.scene.onKeyTypedProperty().set(z -> {
                                            popUp(txt, 25, 10, 50);
                                            if (tisch.getAktivSpieler() == spielerM[0]) {
                                                spielerZug = true;
                                                reloadGui();
                                                txt = new Text("""
                                                        Möglich wäre jetzt wieder eine 6 oder ein Lama zu legen. 
                                                        Das Lama hat einen Wert von -10. Hast du
                                                        mindestens ein Lama auf der Hand, erhälst du somit 10 Minuspunkte.
                                                                                                                
                                                        Versuche Lamas immer loszuwerden """);
                                                popUp(txt, 15, 10, 130);

                                                spieltischGui.scene.onMouseClickedProperty().set(d -> {
                                                    popUp(txt, 15, 10, 130);
                                                    if (tisch.getObereKarteAblagestapel().getValue() == 10) {
                                                        spielerZug = false;
                                                        closeDialog();
                                                        lamaKarte = false;
                                                        txt = new Text("Wundervoll.");
                                                        popUp(txt, 25, 10, 50);

                                                        //Karl legt 1
                                                        spieltischGui.scene.onKeyTypedProperty().set(f -> {
                                                            closeDialog();
                                                            spiellogik.karteLegen(karl, karl.getCardHand().getKarte(0));
                                                            reloadGui();
                                                            txt = new Text("Da das Lama die höhste Karte ist, kann auf sie nur ein Lama oder eine 1 gelegt werden.");
                                                            popUp(txt, 25, 10, 50);
                                                            //Paul legt 2
                                                            spieltischGui.scene.onKeyTypedProperty().set(g -> {
                                                                if (tisch.getObereKarteAblagestapel().getValue() == 1) {
                                                                    spiellogik.karteLegen(paul, paul.getCardHand().getKarte(0));
                                                                    reloadGui();

                                                                    if (tisch.getObereKarteAblagestapel().getValue() == 2) {
                                                                        nachziehen = true;
                                                                        next = false;
                                                                        lamaKarte = false;

                                                                        startTutorial();  //weiter

                                                                    }
                                                                }
                                                            });
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            });
                        }
                    });
                });
            });
        } else if (nachziehen) {
            System.out.println("nachziehen");
            spielerZug = true;
            stapel = true;
            reloadGui();
            txt = new Text("""
                    Sieht so aus als könntest du keine Karte ablegen. 
                    Du hast jetzt die Möglichkeit ausszusteigen oder eine Karte zu ziehen. 
                    Da Paul und ich beide noch ein paar Karten haben und die Summe deiner Hand noch sehr hoch ist, ist es besser eine Karte zu ziehen.
                    Klicke auf den Stapel um eine neue Karte aufzunehmen.""");

            popUp(txt, 25, 8, 150);
            //Karl legt 3
            spieltischGui.scene.onMouseClickedProperty().set(a -> {
                popUp(txt, 25, 5, 150);
                if (tisch.getAktivSpieler() == karl) {
                    closeDialog();
                    stapel = false;
                    spielerZug = false;
                    reloadGui();
                    txt = new Text("Durch das Nachziehen ist dein Zug beendet und ich bin wieder dran");
                    popUp(txt, 25, 10, 50);

                    spiellogik.karteLegen(karl, karl.getCardHand().getKarte(0));

                    //Paul legt 4
                    if (tisch.getAktivSpieler() == paul) {
                        spieltischGui.scene.onKeyTypedProperty().set(b -> {
                            reloadGui();

                            spiellogik.karteLegen(paul, paul.getCardHand().getKarte(0));
                            reloadGui();
                            //Spieler
                            spielerZug = true;
                            reloadGui();
                            txt = new Text("Du bist dran");
                            popUp(txt, 25, 10, 50);

                            spieltischGui.scene.onMouseClickedProperty().set(d -> {
                                popUp(txt, 25, 10, 50);
                                if (tisch.getAktivSpieler() == karl) {
                                    spielerZug = false;
                                    reloadGui();
                                    txt = new Text("""
                                            Ich kann keine Karte mehr ablegen. 
                                            Da ich nur eine 1 und 2 auf der Hand habe, steige ich sicherheitshalber aus.
                                                                                            
                                            Aussteigen kannst du in dem du auf die Hand neben deinen Chips klickst.""");
                                    popUp(txt, 25, 15, 150);

                                    spieltischGui.scene.onKeyTypedProperty().set(e -> {
                                        //reloadGui();
                                        spiellogik.aussteigen(karl);
                                        reloadGui();
                                        txt = new Text(""" 
                                                Wenn ein Spieler ausgestiegen ist, erkennst du das daran, dass seine Karten grau sind.
                                                
                                                Ein Spieler der ausgestiegen ist, kann den Rest einer Runde keine Aktionen mehr ausführen.
                                                
                                                Sollten alle Spieler ausgestiegen sein, endet eine Runde.
                                                """);
                                        popUp(txt, 30, 10, 150);

                                        spieltischGui.scene.onKeyTypedProperty().set(f -> {
                                            popUp(txt, 25, 10, 150);
                                            if (tisch.getAktivSpieler() == paul) {
                                                spiellogik.aussteigen(paul);
                                                reloadGui();
                                                txt = new Text(""" 
                                                        Paul ist auch ausgestiegen. 
                                                        Du kannst jetzt noch so viele Karten ablegen wie 
                                                        möglich, eine Karte nachziehen ist aber
                                                        nicht mehr erlaubt.""");
                                                popUp(txt, 15, 15, 100);
                                                spieltischGui.scene.onKeyTypedProperty().set(g -> {
                                                    popUp(txt, 25, 01, 100);
                                                    if (spielerM[0] == tisch.getAktivSpieler()) {
                                                        nachziehen = false;
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
                }
            });
        } else if (ausgestiegen) {
            // closeDialog();
            txt = new Text(""" 
                    Aber Vorsicht! 
                    Du hast jetzt noch eine 5,6 und ein Lama. 
                    Legst du sie in der Richtigen Reihenfolge ab, 
                    kannst du alle Karten loswerden und einen Chip 
                    abgeben.
                    """);
            popUp(txt, 15, 15, 100);

            spieltischGui.scene.onKeyTypedProperty().set(a -> {
                txt = new Text("""
                        Wenn du es schaffst alle Karten abzulegen, kannst du einen schwarzen Chip abgeben und hast somit nur noch 20 Minuspunkte.
                        
                        In den Einstellungen kannst du jederzeit sagen, dass automatisch der bestmöglichste Chip abgegeben werden soll. """);

                popUp(txt, 25, 10, 150);
                spieltischGui.scene.onKeyTypedProperty().set(b -> {
                    txt = new Text("""
                            Nach dem die Runde beendet ist, wird dir die Rangliste angezeigt. 
                            Dort siehst du, wie viele Punkte jeder Spieler dazu gewonnen hat und auf welchem Platz du momentan stehst.
                            Hat ein Spieler mindestens -40 Punkte erreicht, ist das Spiel zu Ende. 
                            Viel Erfolg!""");
                    popUp(txt, 25, 25, 150);
                    spielerZug = true;
                    reloadGui();
                    spieltischGui.scene.onMouseClickedProperty().set(c -> {
                        closeDialog();
                    });

                });
            });
        }


    }


    void popUp(Text text, double X, double Y, double height) {
        closeDialog();
        TextFlow flow = new TextFlow();

        Image avatar = new Image("GUI/images/clipart2498304.png", 50 * Main.zoomfactor, 70 * Main.zoomfactor, false, false);
        ImageView bild = new ImageView(avatar);

        //Label erklareung = new Label(text);
        text.setFont(new Font("arial light", 10 * zoomfactor));
        text.setStyle("-fx-fill: black; -fx-font-weight: bold");
        text.setTranslateX(X*zoomfactor);
        text.setTranslateY(Y*zoomfactor);
        flow.getChildren().add(text);
        flow.setMinWidth(420 * Main.zoomfactor);
        flow.setMaxWidth(420 * zoomfactor);
        flow.setMaxHeight(height * zoomfactor);

        info = new HBox();
        info.setTranslateX(30 * zoomfactor);
        info.setTranslateY(10 * zoomfactor);
        info.setSpacing(10);
        info.setMinWidth(520 * zoomfactor);
        info.setMinHeight(height * zoomfactor);
        info.getChildren().addAll(bild, flow);
        Image bubble;

        if (height < 100) {
            bubble = new Image("GUI/images/speachbubble2.png");
        } else {
            bubble = new Image("GUI/images/speachbubble5.png");
            bild.setTranslateY(10);
        }
        BackgroundImage myBI = new BackgroundImage(bubble,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(450, 100, false, true, true, false));
        info.setBackground(new Background(myBI));
        //flow.setStyle("-fx-background-color: gray;");
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
            if (!spielerZug) {
                return true;
            } else if (next && karte6 && spielerM[0].getCardHand().getKarte(i).getValue() != 6) {
                txt = new Text("lege die 6 Ab.");
                popUp(txt, 25, 10, 50);
                return true;
            } else if (next && lamaKarte && spielerM[0].getCardHand().getKarte(i).getValue() != 10) {
                txt = new Text("lege ein Lama Ab.");
                popUp(txt, 25, 10, 50);
                return true;
            } else {
                return false;
            }
        } else {// Tutorial aus
            return false;

        }
    }


}

