import org.junit.jupiter.api.Test;

import Main.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class  SpiellogikTest {
    Main main;
    Tisch tisch;
    Spiellogik spiellogik;
    Spieler[] spielerListe;
    Stapel nachziehstapel;
    Stapel ablagestapel;

    @BeforeEach
    void setUp() {
        this.main = new Main();
        this.spielerListe = new Spieler[3];
        spielerListe[0] = new Spieler("test1","1");
        spielerListe[1] = new Spieler("test2","2");
        spielerListe[2] = new Spieler("test3","3");
        this.tisch = new Tisch(spielerListe);
        Hand spielerEinsHand = new Hand();
        Hand spielerZweiHand = new Hand();
        Hand spielerDreiHand = new Hand();
        spielerEinsHand.addKarte(new Karte(1,false));
        spielerEinsHand.addKarte(new Karte(2,false));
        spielerZweiHand.addKarte(new Karte(3,false));
        spielerDreiHand.addKarte(new Karte(4,false));
        spielerDreiHand.addKarte(new Karte(5,false));
        spielerListe[0].setCardHand(spielerEinsHand);
        spielerListe[1].setCardHand(spielerZweiHand);
        spielerListe[2].setCardHand(spielerDreiHand);
        spielerListe[0].einsteigen();
        spielerListe[1].einsteigen();
        spielerListe[2].einsteigen();
        ablagestapel = new Stapel(false);
        nachziehstapel = new Stapel(true);
        ablagestapel.ablegen(new Karte(1,false));
        nachziehstapel.addCard(new Karte(4,true));
        nachziehstapel.addCard(new Karte(5,true));
        nachziehstapel.addCard(new Karte(10,true));
        tisch.nextDurchgang();
        tisch.setAktiv(0);
        tisch.setAblageStapel(ablagestapel);
        tisch.setNachziehstapel(nachziehstapel);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setAnzSpieler(3);

    }
    @Test
    public void karteLegenTest(){
        assertEquals(true,spiellogik.karteLegen(spielerListe[0],spielerListe[0].getCardHand().getKarte(0)));
        assertEquals(1,ablagestapel.getTopCard().getValue());  //Karte 1 liegt auf dem Ablagestapel
        assertEquals(1,spielerListe[0].getCardCount()); //spieler hat nur noch 1 Karte in der Hand

        //spieler1 versucht nochmal zu legen, obwohl dieser nicht dran ist
        assertEquals(false,spiellogik.karteLegen(spielerListe[0],spielerListe[0].getCardHand().getKarte(0)));


    }
    @Test
    public void kartelegeTest2(){
        //Spieler2 versucht zu legen
        assertEquals(false,spiellogik.karteLegen(spielerListe[1],spielerListe[0].getCardHand().getKarte(0)));
        //Spieler1 legt seine zweite Karte
        assertEquals(true,spiellogik.karteLegen(spielerListe[0],spielerListe[0].getCardHand().getKarte(1)));
        //Karte 2 liegt auf dem Ablagestapel
        assertEquals(2,ablagestapel.getTopCard().getValue());


    }

    @Test
    public void karteZiehenTest1(){
        assertEquals(10,nachziehstapel.getTopCard().getValue()); //Karte 10 liegt auf den Nachziehstapel
        assertEquals(true,spiellogik.karteNachziehen(spielerListe[0]));  //Karte konnte gezogen werden
        assertEquals(3,spielerListe[0].getCardCount());  //Spieler hat jetzt 3 Karten
        assertEquals(10,spielerListe[0].getCardHand().getKarte(2).getValue()); //Neue Karte ist die 10
        assertEquals(1,tisch.aktiv); //nächster Spieler ist dran
    }

    @Test
    public void karteZiehenTest2(){
        //Spieler2 versucht zu ziehen, obwohl dieser noch nicht dran ist
        assertFalse(spiellogik.karteNachziehen(spielerListe[1]));
    }

    @Test
    public void aussteigenTest1(){
        spiellogik.aussteigen(spielerListe[0]); //Spieler1 steigt aus
        assertFalse(spielerListe[0].inGame());  //Spieler1 nicht mehr in der Runde
        System.out.println(tisch.getAktivSpieler().getName());
        assertEquals(1,tisch.aktiv); //nächerster Spieler ist dran
        assertTrue(spiellogik.karteNachziehen(spielerListe[1]));
        assertTrue(spiellogik.karteNachziehen(spielerListe[2]));
        assertEquals(1,tisch.aktiv);

    }

    @Test
    public void chipsKassierenTest1(){
        //Chipskassieren für Spieler1
        spiellogik.chipsKassieren(spielerListe[0]);
        assertEquals(-3,spielerListe[0].getPoints());
    }

    @Test
    public void chipsKassierenTest2(){
        spiellogik.karteNachziehen(spielerListe[0]);
        spiellogik.karteLegen(spielerListe[1],spielerListe[1].getCardHand().getKarte(0));
        spiellogik.chipsKassieren(spielerListe[1]);
        assertEquals(0,spielerListe[0].getPoints());
    }

    @Test
    public void chipsTauschenTest(){
        //spieler hat 12 weiße chips und 0 schwarze chips
        spielerListe[0].setWhiteChips(12);
        spielerListe[0].setPoints(-12);
        spiellogik.chipsTauschen(0);
        assertEquals(-12,spielerListe[0].getPoints());  //nach dem Tauschen gleiche Punktzahl
        assertEquals(1,spielerListe[0].getBlackChips()); //aber einen schwarzen Chip dazu bekommen
        assertEquals(2,spielerListe[0].getWhiteChips()); //10 weiße Chips abgezogen
    }

    @Test
    public void chipsTauschenTest2(){
       spielerListe[0].setWhiteChips(5); //Spieler hat nur 5 weiße Chips
       assertEquals(false,spiellogik.chipsTauschen(0));  //kann somit nicht tauschen
       assertEquals(0,spielerListe[0].getBlackChips());
       assertEquals(5,spielerListe[0].getWhiteChips());

    }

    @Test
    public void chipAbgeben1(){
        spielerListe[1].setWhiteChips(5);  //spieler2 hat 5 weiße Chips
        spielerListe[1].setPoints(-5);
        spiellogik.karteLegen(spielerListe[0],spielerListe[0].getCardHand().getKarte(1));
        //spieler2 legt seine Karten ab und hat jetzt keine Karten mehr
        spiellogik.karteLegen(spielerListe[1],spielerListe[1].getCardHand().getKarte(0));
        //-> darf Chip abgeben
        assertEquals(true,spiellogik.chipAbgeben(spielerListe[1],new WhiteChip()));
        assertEquals(4,spielerListe[1].getWhiteChips());
        assertEquals(-4,spielerListe[1].getPoints());

    }

    @Test
    public void chipAbgeben2(){
        spielerListe[1].setWhiteChips(5);  //spieler2 hat 5 weiße Chips
        spielerListe[1].setBlackChips(2);  //zwei schwarze Chips
        spielerListe[1].setPoints(-25);
        spiellogik.karteLegen(spielerListe[0],spielerListe[0].getCardHand().getKarte(1));
        spiellogik.karteLegen(spielerListe[1],spielerListe[1].getCardHand().getKarte(0));
        assertEquals(true,spiellogik.chipAbgeben(spielerListe[1],new BlackChip()));
        assertEquals(1,spielerListe[1].getBlackChips());
        assertEquals(-15,spielerListe[1].getPoints());

    }

    @Test
    public void ranglisteErstellen1(){
        spiellogik.chipsKassieren(spielerListe[0]);
        spiellogik.chipsKassieren(spielerListe[1]);
        spiellogik.chipsKassieren(spielerListe[2]);
        Map<Spieler,Integer> rangliste = spiellogik.ranglisteErstellen();
        for (Map.Entry<Spieler, Integer> entry : rangliste.entrySet()) {
            if(entry.getKey().getName().equals("test1")){
                assertEquals(-3,entry.getValue());
            }
            else if(entry.getKey().getName().equals("test2")){
                assertEquals(-3,entry.getValue());

            }
            else if(entry.getKey().getName().equals("test3")){
                assertEquals(-9,entry.getValue());

            }
        }


    }

    @Test
    public void ranglisteErstellenNurWeißeChips(){
        spiellogik.karteNachziehen(spielerListe[0]);
        spiellogik.karteNachziehen(spielerListe[1]);
        spiellogik.karteNachziehen(spielerListe[2]);
        spiellogik.chipsKassieren(spielerListe[0]);
        spiellogik.chipsKassieren(spielerListe[1]);
        spiellogik.chipsKassieren(spielerListe[2]);
        Map<Spieler,Integer> rangliste = spiellogik.ranglisteErstellenNurWeißeChips();
        for (Map.Entry<Spieler, Integer> entry : rangliste.entrySet()) {
            if(entry.getKey().getName().equals("test1")){
                assertEquals(3,entry.getValue());
            }
            else if(entry.getKey().getName().equals("test2")){
                assertEquals(8,entry.getValue());

            }
            else if(entry.getKey().getName().equals("test3")){
                assertEquals(9,entry.getValue());

            }
        }

    }

    @Test
    public void alleAussteigen(){
        spiellogik.alleAussteigen();
        for(Spieler s:spielerListe){
            assertEquals(false,s.inGame());
        }
    }

    @Test
    public void initNeueRunde(){
        Main.haende = new Hand[3];
        spiellogik.initNeueRunde();
        assertEquals(37,tisch.getNachziehStapelSize());
        for(Spieler s: spielerListe){
            assertEquals(6,s.getCardCount());
        }
        assertEquals(2,tisch.getDurchgangNr());  //2 weil oben bereits die erste Runde gestartet wurde

    }

    @Test
    public void einSpielerUebrig(){
        spiellogik.aussteigen(spielerListe[0]);
        spiellogik.aussteigen(spielerListe[1]);
        ablagestapel.ablegen(new Karte(4,false));
        assertEquals(true,spiellogik.karteLegen(spielerListe[2],spielerListe[2].getCardHand().getKarte(0)));
        assertEquals(false, spiellogik.karteNachziehen(spielerListe[2]));
        assertEquals(true,spiellogik.karteLegen(spielerListe[2],spielerListe[2].getCardHand().getKarte(0)));




    }





}
