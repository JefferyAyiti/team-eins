import Main.Karte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KarteTest {
    Karte karte1;
    Karte karte2;


    /**
     * setup für die Tests
     */
    @BeforeEach
    void setUp() {
        karte1 = new Karte(2,true);
        karte2 = new Karte(3,true);

    }

    /**
     * Test für getValue-Methode
     */
    @Test
    public void getValue(){
        assertEquals(2,karte1.getValue());
        assertEquals(3,karte2.getValue());
    }

    /**
     * Test für compareTo-Methode
     */
    @Test
    public void compareTo(){
        Karte karte3 = new Karte(2,true);
        assertEquals(-1,karte1.compareTo(karte2));
        assertEquals(0,karte1.compareTo(karte3));
    }

}
