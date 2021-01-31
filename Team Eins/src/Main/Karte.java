package Main;

import java.io.Serializable;

import static Main.Main.tisch;

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

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        Karte ob = (Karte)obj;
        return value == ob.getValue();
    }

    public boolean isPlayable() {
        return tisch.getObereKarteAblagestapel().value == this.value //gleicher Wert
                || tisch.getObereKarteAblagestapel().value == this.value - 1   //Handkarte ist um eins größer als die oberste Ablagekarte
                || (tisch.getObereKarteAblagestapel().value == 6 && this.value == 10) //Lama auf 6
                || (tisch.getObereKarteAblagestapel().value == 10 && this.value == 1);
    }
}

