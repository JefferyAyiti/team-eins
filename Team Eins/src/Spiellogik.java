import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Spiellogik {
    Stack<Spieler> letzteSpieler = new Stack();
    Tisch tisch;
    Spieler[] spielerliste;

    public Spiellogik(Tisch tisch) {
        this.tisch = tisch;
        spielerliste = tisch.getSpieler();
    }

    /** Ein Spieler legt eine Karte aus seiner Hand auf den Ablagestapel
     * @param spieler
     * @param karte
     */
    void karteLegen(Spieler spieler, Karte karte) {
        try {
            //TODO
            // regelüberprüfung
            spieler.getCardHand().removeKarte((HandKarte) karte);
            tisch.karteAblegen(karte);
        }
        catch (Exception e){}
    }


    /** Ein Spieler zieht eine KArte vom Nachziehstapel
     * @param spieler
     */
    void karteNachziehen(Spieler spieler)  {
        try {
            //TODO
            // regelüberprüfung
            spieler.getCardHand().addKarte(tisch.karteZiehen());
        } catch (Exception e) {}
    }

    void chipsKassieren(Spieler spieler) {

    }

    void chipsTauschen(Spieler spieler) {

    }

    void chipAbgeben(Spieler spieler, Chip chip) {

    }

    void aussteigen(Spieler spieler) {

    }

    Map<Spieler, Integer> ranglisteErstellen() {
        return new HashMap<>();
    }

}

