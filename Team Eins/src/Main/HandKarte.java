package Main;

import java.io.Serializable;

public class HandKarte extends Karte  implements Serializable {
    private static final int LAMA = 10;
    private Tisch tisch;
    private Hand hand;

    /**
     * @param value
     * @param covered
     * @param entryTisch
     * @param entryHand
     */
    public HandKarte(int value, boolean covered, Tisch entryTisch, Hand entryHand) {
        super(value, covered);
        tisch = entryTisch;
        hand = entryHand;
    }

    /**
     * legt die Karte auf den Tisch und entfernt die Karte aus der Hand
     * @throws Exception
     */
    void ablegen() throws Exception {
        if(this.isPlayable()) {
            tisch.karteAblegen(this);
            hand.removeKarte(this);
        }
    }

    /**
     * @return Ob die aktuelle Karte ablegbar ist oder nicht
     * @throws Exception
     */
    boolean isPlayable() throws Exception {
        //3 Ablageregeln
        boolean ruleNormal = value != LAMA && (
                tisch.getObereKarteAblagestapel().getValue() == value
                || tisch.getObereKarteAblagestapel().getValue() == value+1);
        boolean ruleLama = value == LAMA &&
                (tisch.getObereKarteAblagestapel().getValue() ==  6 ||
                        tisch.getObereKarteAblagestapel().getValue() ==  LAMA);
        boolean ruleAufLama = tisch.getObereKarteAblagestapel().getValue() ==  LAMA &&
                (value == LAMA || value == 1);


        return ruleNormal || ruleLama || ruleAufLama;
    }
}
