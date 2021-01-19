package Main;

import java.io.Serializable;
import java.util.*;


public class Hand  implements Serializable {
    private ArrayList<HandKarte> handKarten = new ArrayList<HandKarte>();

    /**
     * Seter für das Umsortieren der Karten per Drag&Drop
     * @param handKarten
     */
    public void setHandKarten(ArrayList<HandKarte> handKarten) {
        this.handKarten = handKarten;
    }

    /**
     * @return Gibt alle aktuellen Karten der hand aus
     */
    public ArrayList<HandKarte> getHandKarte() {
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
        for(HandKarte c:handKarten) {
            if(karte.getValue() == c.getValue()) {
                handKarten.remove(c);
                break;
            }
        }
    }
}
