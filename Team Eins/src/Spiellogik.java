import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Spiellogik {
    private Stack<Spieler> letzteSpieler = new Stack();
    private Tisch tisch;
    private Spieler[] spielerListe;

    public Spiellogik(Tisch tisch) {
        this.tisch = tisch;
        spielerListe = tisch.getSpieler();
    }

    public void durchgang(){

    }
    /** Ein Spieler legt eine Karte aus seiner Hand auf den Ablagestapel
     * @param spieler
     * @param karte
     */
    public void karteLegen(Spieler spieler, Karte karte) {
        try {
            if(tisch.getObereKarteAblagestapel().value == karte.value //gleicher Wert
                    ||tisch.getObereKarteAblagestapel().value == karte.value - 1   //Handkarte ist um eins größer als die oberste Ablagekarte
                    ||(tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                    ||(tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1)  //1 auf Lama
            ){
                spieler.getCardHand().removeKarte((HandKarte) karte);
                tisch.karteAblegen(karte);
            }

            else {
                throw new Exception();
            }

        }
        catch (Exception e){}
    }


    /** Ein Spieler zieht eine KArte vom Nachziehstapel
     * @param spieler
     */
    public void karteNachziehen(Spieler spieler)  {
        try {
            //TODO
            // regelüberprüfung
            spieler.getCardHand().addKarte(tisch.karteZiehen());
        } catch (Exception e) {}
    }

    public void chipsKassieren(Spieler spieler) {

    }

    public void chipsTauschen(Spieler spieler) {

    }

    public void chipAbgeben(Spieler spieler, Chip chip) {

    }

    public void aussteigen(Spieler spieler) {

    }

    Map<Spieler, Integer> ranglisteErstellen() {
        return new HashMap<>();
    }

}

