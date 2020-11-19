import java.util.*;

public class Hand {
    private int valueSum;
    private ArrayList<HandKarte> handKarten = new ArrayList<HandKarte>();
    private Spieler spieler;
    private Tisch tisch;

    public Hand(){
    }

    /**
     * Setzt den Tisch auf dem die Hand kommt
     * @param entryTisch
     */
    public void setTisch(Tisch entryTisch){
        tisch = entryTisch;
    }

    /**
     * Setzt den Spieler dem die Hand gehören soll
     * @param entrySpieler
     */
    public void setSpieler(Spieler entrySpieler){
        spieler = entrySpieler;
    }

    /**
     * Alle Kartewerte in eins Set bringen und danach summieren
     */
    public void setValueSum() {
        int sum = 0;
        Set<Integer> ziffern = new HashSet<>();
        for (HandKarte karte:handKarten) {
            ziffern.add(karte.getValue());
        }
        for (Integer i:ziffern) {sum += i; }
        valueSum = sum;
    }

    /**
     *
     * @return Ziffern einmalig summieren
     */
    public int getValueSum(){
        return valueSum;
    }

    /**
     * @return Gibt den Spieler aus dem die Hand gehört
     */
    public Spieler getSpieler(){
        return spieler;
    }


    /**
     * @return Gibt alle aktuellen Karten der hand aus
     */
    public List<HandKarte> getHandKarte() {
        return handKarten;
    }

    /**
     * @param nr
     * @return Gibt die handKarte an der Stelle nr zurück
     */
    public HandKarte getKarte(int nr) {
        return handKarten.get(nr);
    }

    /**
     * Fügt die angegeben Karte der hand hinzu
     * @param karte
     */
    public void addKarte(Karte karte){
            HandKarte hk = new HandKarte(karte.getValue(), false, tisch, this);
            handKarten.add(hk);
    }

    /**
     * Entfernt Karte "karte" von der Hand
     * @param karte
     */
    public void removeKarte (HandKarte karte){
        handKarten.remove(karte);
    }
}
