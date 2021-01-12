import Main.Hand;
import Main.Main;
import Main.Spieler;
import Main.Tisch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpielerTest {
    Spieler spieler;
    Spieler spieler2;
    Spieler spieler3;
    Main main;
    Tisch tisch;


    /**
     * setup für die Tests
     */
    @BeforeEach
    void setUp() {
        this.main = new Main();
        spieler = new Spieler("testSpieler1", "0");
        spieler2 = new Spieler("testSpieler2", "1");
        spieler3 = new Spieler("testSpieler3", "2");
        Spieler[] spielerListe = new Spieler[3];
        spielerListe[0] = spieler;
        spielerListe[1] = spieler2;
        spielerListe[2] = spieler3;
        tisch = new Tisch(spielerListe);
        main.setTisch(tisch);

    }

    /**
     * Test für einsteigen und aussteigen
     */
    @Test
    void einsteigenUndAussteigen(){
        spieler.einsteigen();
        assertEquals(true,spieler.inGame());
        spieler.aussteigen();
        assertEquals(false,spieler.inGame());
    }

    /**
     * Test für toString-Methode
     */
    @Test
    void tostring(){
        assertEquals("(testSpieler1 0)",spieler.toString());
    }

    /**
     * Test für equal-Methode
     */
    @Test
    void equals(){
        assertEquals(false,spieler.equals(spieler2));
        spieler.setCardHand(new Hand());
        assertEquals(true,spieler.equals(spieler));
    }

}