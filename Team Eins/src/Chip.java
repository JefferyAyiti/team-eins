/**
 * Bildet die Oberklasse aller Chips
 */
public class Chip {

    public int value;


    /**
     * Chip wird mit einem Wert initialisiert
     * @param value Wert des Chips
     */
    public Chip(int value) {

        this.value = value;
    }

    /**
     * Gibt Wert des Chips zurück
     * @return Chip wert
     */
    public int getValue() {

        return value;
    }



}
