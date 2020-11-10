public class HandKarte extends Karte {
    private static final int LAMA = 10;
    private Tisch tisch;
    private Hand hand;

    public HandKarte(int value, boolean covered, Tisch entryTisch, Hand entryHand) {
        super(value, covered);
        tisch = entryTisch;
        hand = entryHand;
    }

    void ablegen() {
        if(this.isPlayable()) {
            tisch.karteAblegen(this);
            hand.removeKarte(this);
        }
    }

    boolean isPlayable() {
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
