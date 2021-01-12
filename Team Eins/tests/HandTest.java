import Main.Hand;
import Main.HandKarte;
import Main.Karte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandTest {
    Hand hand;


    /**
     * setup für die Tests
     */
    @BeforeEach
    void setUp() {
        hand = new Hand();

    }

    /**
     * Test für addKarte-Methode
     */
    @Test
    public void addKarte(){
        hand.addKarte(new Karte(1,true));
        hand.addKarte(new Karte(1,true));
        hand.addKarte(new Karte(2,true));
        hand.addKarte(new Karte(3,true));
        assertEquals(1,hand.getKarte(0).getValue());
        assertEquals(1,hand.getKarte(1).getValue());
        assertEquals(2,hand.getKarte(2).getValue());
        assertEquals(3,hand.getKarte(3).getValue());

    }

    /**
     * Test für remove-Methode
     */
    @Test
    public void removeKarte(){
        hand.addKarte(new Karte(1,true));
        hand.addKarte(new Karte(1,true));
        hand.addKarte(new Karte(2,true));
        hand.addKarte(new Karte(3,true));
        hand.removeKarte(new HandKarte(2,true));
        assertEquals(3,hand.getHandKarte().size());
        assertEquals(3,hand.getKarte(2).getValue());

    }



}
