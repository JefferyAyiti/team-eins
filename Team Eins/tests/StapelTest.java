import Main.Karte;
import Main.Stapel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StapelTest {
    Stapel ablagestapel;
    Stapel ziehstapel;


    @BeforeEach
    void setUp() {
        this.ablagestapel = new Stapel(false);
        this.ziehstapel = new Stapel(true);
    }

    /**
     * Test für addCard-Methode und ziehen-Methode
     */
    @Test
    public void addCardUndZiehen(){
        //Ablagestapel mit der Karte 3
        ziehstapel.addCard(new Karte(3,true));
        //Karte wird gezogen
        assertEquals(3,ziehstapel.ziehen().getValue());
        //Stapel nun leer
        assertEquals(0,ziehstapel.getCardCount());

    }

    /**
     * Test für getTopCard-Methode
     */
    @Test
    public void getTopCard(){
        //Ablagestapel mit Karte 3
        ziehstapel.addCard(new Karte(3,true));
        //Gucken welche Karten oben liegt
        assertEquals(3,ziehstapel.getTopCard().getValue());
        //Kartenanzahl bleibt erhalten
        assertEquals(1,ziehstapel.getCardCount());

    }

    /**
     * Test für ablegen-Methode
     */
    @Test
    public void ablegen(){
        ablagestapel.ablegen(new Karte(4,false));
        assertEquals(4,ablagestapel.getTopCard().getValue());

    }

    /**
     * Test für mischen-Methode
     */
    @Test
    public void mischen(){
        //Zähler wie oft die 1 oben ist
        int count = 0;
        for(int i = 0; i < 100; i++){
            //neuer Stapel
            ziehstapel = new Stapel(true);
            //Karten hinzufügen
            ziehstapel.addCard(new Karte(2,true));
            ziehstapel.addCard(new Karte(1,true));
            //mischen
            ziehstapel.mischen();
            //gucken welche oben ist
            if(ziehstapel.getTopCard().getValue() == 1){
                count++;
            }

        }
        assertTrue(count > 20);
    }
    }
