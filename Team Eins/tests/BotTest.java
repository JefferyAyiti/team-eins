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

        Hand TestHand= new Hand();
        for (int s = 0; s < 6; s++) {
            TestHand.addKarte(new HandKarte(s + 1, true));
        }
        Hand TestDoppelte = new Hand();
        TestDoppelte.addKarte(new Karte(1,false));
        TestDoppelte.addKarte(new Karte(2,false));
        TestDoppelte.addKarte(new Karte(2,false));
        for (int s = 3; s < 6; s++) {
            TestDoppelte.addKarte(new HandKarte(s + 1, true));
        }

        botListe[0].setCardHand(TestHand);
        botListe[1].setCardHand(TestHand);
        botListe[2].setCardHand(TestDoppelte);

        botListe[0].einsteigen();
        botListe[1].einsteigen();
        botListe[2].einsteigen();

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
    void playSchwierigkeitLeicht() {
        assertEquals(1,botListe[0].getCardHand().getKarte(0));
        assertEquals(1,ablagestapel.getTopCard().getValue());  //Karte 1 liegt auf dem Ablagestapel
        assertEquals(5,botListe[0].getCardCount()); //Bot hat nur noch 5 Karte in der Hand

    }

    @Test
    void playSchwierigkeitMittel() {
        assertEquals(1,botListe[1].getCardHand().getKarte(0));
        assertEquals(2,ablagestapel.getTopCard().getValue());  //Karte 1 liegt auf dem Ablagestapel
        assertEquals(5,botListe[1].getCardCount()); //spieler hat nur noch 5 Karte in der Hand
    }

    @Test
    void playSchwierigkeitSchwer() {
        assertEquals(2,botListe[2].getCardHand().getKarte(0));
        assertEquals(1,ablagestapel.getTopCard().getValue());  //Karte 1 liegt auf dem Ablagestapel
        assertEquals(5,botListe[1].getCardCount()); //spieler hat nur noch 5 Karte in der Hand

        assertEquals(2,botListe[2].getCardHand().getKarte(0));
        assertEquals(2,ablagestapel.getTopCard().getValue());  //Karte 1 liegt auf dem Ablagestapel
        assertEquals(4,botListe[1].getCardCount()); //spieler hat nur noch 4 Karte in der Hand
    }

    @Test
    void chipAbgeben() {
       Bot test = new Bot ("Tester 1", 1);
       test.setBlackChips(2);
       test.chipAbgeben();
       assertEquals(1,test.getBlackChips());
    }

    @Test
    void playerCardCount1() {
        assertEquals(6,botListe[0].playerCardCount());
    }

    @Test
    void letzterSpieler() {
        for(int i = 1; i<6; i++){
            botListe[i].aussteigen();
        }
        assertEquals(true,botListe[0].letzterSpieler());
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

    @Ignore
    @Test
    //@RepeatedTest(100)
    void play() {


    }



}