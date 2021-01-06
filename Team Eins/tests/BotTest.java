package Main;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import Main.*;

import static org.junit.jupiter.api.Assertions.*;
import static Main.Main.*;

class BotTest {
    Stapel nachziehstapel;
    Stapel ablagestapel;
    Main main;
    Tisch tisch;
    Spiellogik spiellogik;
    Bot[] botListe;

    @BeforeEach
    void setUp() {
        main = new Main();
        this.botListe = new Bot[3];
        botListe[0] = new Bot("test1",1);
        botListe[1] = new Bot("test2",2);
        botListe[2] = new Bot("test3",3);
        this.tisch = new Tisch(botListe);
        int a = 1;
        Hand TestHand= new Hand();
        for (int s = 0; s < 6; s++) {
            TestHand.addKarte(new HandKarte(a, false));
            a++;
        }
        Hand TestDoppelte = new Hand();
        TestDoppelte.addKarte(new HandKarte(5,false));
        TestDoppelte.addKarte(new HandKarte(4,false));
        TestDoppelte.addKarte(new HandKarte(10, false));
        TestDoppelte.addKarte(new HandKarte(6, false));
        TestDoppelte.addKarte(new HandKarte(6, false));
        TestDoppelte.addKarte(new HandKarte(10, false));


        botListe[0].setCardHand(TestHand);
        botListe[1].setCardHand(TestHand);
        botListe[2].setCardHand(TestDoppelte);

        botListe[0].einsteigen();
        botListe[1].einsteigen();
        botListe[2].einsteigen();

        for(int i= 0; i<3; i++) {
            botListe[i].setBlackChips(1);
            botListe[i].setWhiteChips(1);
            botListe[i].setPoints(-11);
        }

        ablagestapel = new Stapel(false);
        nachziehstapel = new Stapel(true);


        tisch.nextDurchgang();
        tisch.setAktiv(0);
        tisch.setAblageStapel(ablagestapel);
        tisch.setNachziehstapel(nachziehstapel);
        Main.spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setAnzSpieler(3);

    }

    @Test
    void playSchwierigkeitLeicht() {
        ablagestapel.ablegen(new Karte(1,false));
        botListe[0].play();

        assertEquals(1,ablagestapel.getTopCard().getValue());//Karte 5 liegt auf dem Ablagestapel
        assertEquals(5,botListe[0].getCardCount()); //Bot hat nur noch 5 Karte in der Hand

    }

    @Test
    void playSchwierigkeitMittel() {
        ablagestapel.ablegen(new Karte(2,false));
        botListe[1].play();

        assertEquals(2,ablagestapel.getTopCard().getValue());  //Karte 5 liegt auf dem Ablagestapel
        assertEquals(5,botListe[1].getCardCount()); //spieler hat nur noch 5 Karte in der Hand
    }

    @Test
    void playSchwierigkeitSchwer() {
        ablagestapel.ablegen(new Karte(5,false));
        botListe[2].play();

        assertEquals(5,ablagestapel.getTopCard().getValue());  //Karte 1 liegt auf dem Ablagestapel
        //assertEquals(5,botListe[2].getCardCount()); //spieler hat nur noch 5 Karte in der Hand
    }
    @Test
    void playSchwierigkeitSchwer2() {

        ablagestapel.ablegen(new Karte(6,false));
        botListe[2].play();

        assertEquals(10,ablagestapel.getTopCard().getValue());  //Karte 10 liegt auf dem Ablagestapel
        //assertEquals(5,botListe[2].getCardCount()); //spieler hat nur noch 5 Karte in der Hand


    }

    @Test
    void chipAbgeben() {
        ablagestapel.ablegen(new Karte(1,false));
        Hand test = new Hand();
        test.addKarte(new Karte(1,false));
        botListe[0].setCardHand(test);
        botListe[0].play();

        //-> darf Chip abgeben
        assertEquals(0,botListe[0].getBlackChips());
        assertEquals(-1,botListe[0].getPoints());

    }

    @Test
    void playerCardCount1() {
        assertEquals(6,botListe[0].playerCardCount());
    }


    @Test
    void setCardHand() {
        Bot test = new Bot ("Tester 1", 1);
        Hand TestHand= new Hand();
        for (int s = 0; s < 6; s++) {
                TestHand.addKarte(new HandKarte(s + 1, true));
        }
        test.setCardHand(TestHand);
        assertEquals(TestHand, test.getCardHand());
    }

    @Test
    void getName() {
        Bot test = new Bot ("Tester1", 1);
        assertEquals("Tester1",test.getName());
    }

    @Test
    void getPoints() {
        Bot test = new Bot ("Tester1", 1);
        test.setPoints(10);
        assertEquals(10,test.getPoints());
    }

    @Test
    void setPoints() {
        Bot test = new Bot ("Tester1", 1);
        test.setPoints(-15);
        assertEquals(-15,test.getPoints());
    }


    @Test
    void getCardHand() {
        Bot test = new Bot ("Tester 1", 1);
        Hand TestHand= new Hand();
        for (int s = 0; s < 6; s++) {
            TestHand.addKarte(new HandKarte(s + 1, true));
        }
        test.setCardHand(TestHand);
        assertEquals(TestHand, test.getCardHand());
    }

    @Test
    void setBlackChips() {
        Bot test = new Bot ("Tester 1", 1);
        test.setBlackChips(2);
        assertEquals(2, test.getBlackChips());
    }

    @Test
    void getWhiteChips() {
        Bot test = new Bot ("Tester 1", 1);
        test.setWhiteChips(2);
        assertEquals(2, test.getWhiteChips());
    }



}