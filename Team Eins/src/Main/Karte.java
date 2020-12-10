package Main;

import java.io.Serializable;

public class Karte implements Comparable, Serializable {
final int value;
boolean covered;

    /**
     * @param value Wert der Karte
     * @param covered verdeckt oder offen
     */
    public Karte(int value, boolean covered) {
        this.value = value;
        this.covered = covered;
    }


    /**
     * @return Gibt den Wert der Karte zurück
     */
    public int getValue() {
        return value;
    }


    @Override
    public int compareTo(Object o) {
        Karte comp = (Karte)o;
        if(this.getValue() == comp.getValue())
            return 0;
        else if(this.getValue() > comp.getValue())
            return 1;
        else return -1;
    }
}

