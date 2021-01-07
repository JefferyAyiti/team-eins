import Main.BlackChip;
import Main.Chip;
import Main.WhiteChip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChipTest {
    Chip chip;
    WhiteChip whiteChip;
    BlackChip blackChip;



    @BeforeEach
    void setUp() {
        chip = new Chip(3);
        whiteChip = new WhiteChip();
        blackChip = new BlackChip();
    }

    /**
     * Test für getValue-Methode
     */
    @Test
    void getValue(){
        assertEquals(3,chip.getValue());
        assertEquals(-1,whiteChip.getValue());
        assertEquals(-10,blackChip.getValue());

    }

    @Test
    void getMaxChips(){
        assertEquals(20,blackChip.getMaxChips());
        assertEquals(50,whiteChip.getMaxChips());
    }


}
