import org.junit.jupiter.api.Test;

import Main.*;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TischTest {
    Tisch tisch;
    Spieler[] spielerListe;

    /**
     * Test für setup
     */
    @BeforeEach
    void setUp() {
        this.spielerListe = new Spieler[3];
        spielerListe[0] = new Spieler("test1","1");
        spielerListe[1] = new Spieler("test2","2");
        spielerListe[2] = new Spieler("test3","3");
        this.tisch = new Tisch(spielerListe);
    }

    /**
     * Test für nextDurchgang
     */
    @Test
    public void nextDurchgang(){
        tisch.nextDurchgang();
        assertEquals(1,tisch.getDurchgangNr());
        tisch.nextDurchgang();
        assertEquals(2,tisch.getDurchgangNr());
    }

    /**
     * Test für takeChips
     */
    @Test
    public void takeChips(){
        tisch.takeChips(2,1);
        assertEquals(48,tisch.getWhiteChips());
        assertEquals(19,tisch.getBlackChips());
        tisch.takeChips(0,0);
        assertEquals(48,tisch.getWhiteChips());
        assertEquals(19,tisch.getBlackChips());

    }

    /**
     * Test für initNachziehstapel
     */
    @Test
    public void initNachziehstapel(){
       tisch.initNachziehstapel();
        ArrayList<Integer> test = new ArrayList<Integer>();
       for(int i = 0; i < 56;i++){
           //alle Karten in array packen
           test.add(tisch.karteZiehen().getValue());
       }
       int sum = 0;
       //summe der Kartenwerte berechnen des ganzen Stapels
        for (int item : test) {
            sum += item;
        }
        assertEquals(248,sum);
    }

    /**
     * Test für karteAblegen
     */
    @Test
    public void karteAblegen(){
        tisch.karteAblegen(new Karte(4,true));
        assertEquals(4,tisch.getObereKarteAblagestapel().getValue());
    }

    /**
     * Test für getNachziehStapelSize
     */
    @Test
    public void getNachziehStapelSize(){
        tisch.initNachziehstapel();
        assertEquals(56,tisch.getNachziehStapelSize());
    }

    /**
     * Test für naechste
     */
    @Test
    public void naechste(){
        int aktiv = tisch.aktiv;
        switch (aktiv){
            case 0:
                tisch.naechste();
                assertEquals("test2",tisch.getAktivSpieler().getName());
            case 1:
                tisch.naechste();
                assertEquals("test3",tisch.getAktivSpieler().getName());

            case 2:
                tisch.naechste();
                assertEquals("test1",tisch.getAktivSpieler().getName());
        }

    }

    /**
     * Test für mischen
     */
    @Test
    public void mischen(){
        int count = 0;
        for(int i = 0; i < 101; i++){
            //100 mal mischen
            tisch.initNachziehstapel();
            tisch.mischenNachziehstapel();
            if(tisch.karteZiehen().getValue() != 1){
                //erste Stelle ist die Karte 1
                count += 1;
            }
        }
        assertTrue(count > 70);
    }


}
