import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class HandKarte extends Karte {

    private static final int LAMA = 10;

    public HandKarte(int value, boolean covered) {
        super(value, covered);
    }

    void ablegen() {
        if(this.isPlayable()) {
            ablagestapel.push(this);
            cardHand.removeKarte(this);
        }
    }

    boolean isPlayable() {
        //3 Ablageregeln
        boolean ruleNormal = value != LAMA && (
                ablagestapel.peek().getValue() == value
                || ablagestapel.peek().getValue() == value+1);
        boolean ruleLama = value == LAMA &&
                (ablagestapel.peek().getValue() == 6 ||
                        ablagestapel.peek().getValue() == LAMA);
        boolean ruleAufLama = ablagestapel.peek().getValue() == LAMA &&
                (value == LAMA || value == 1);


        return ruleNormal || ruleLama || ruleAufLama;
    }
}
