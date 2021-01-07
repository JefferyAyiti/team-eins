import Main.HandKarte;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HandKarteTest {
    HandKarte handKarte;

    /**
     * Test für toString-Methode
     */
    @Test
    public void tostring(){
        handKarte = new HandKarte(3,true);
        assertEquals("3",handKarte.toString());

    }




}
