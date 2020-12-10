package Main;

import java.io.Serializable;
import java.util.*;


public class Hand  implements Serializable {
    private ArrayList<HandKarte> handKarten = new ArrayList<HandKarte>();


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
            HandKarte hk = new HandKarte(karte.getValue(), false);
            handKarten.add(hk);
    }

    /**
     * Entfernt Karte "karte" von der Hand
     * @param karte
     */
    public void removeKarte (HandKarte karte){
        System.out.println(handKarten);
        System.out.println("Karte "+karte.getValue()+" entfernt");
        handKarten.remove(karte);
        System.out.println(handKarten);
    }
}
