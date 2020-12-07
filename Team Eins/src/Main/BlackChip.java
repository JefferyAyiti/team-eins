package Main;

public class BlackChip extends Chip {

    private final int MAX_CHIPS = 20;

    /**
     * Schwarzer Chip wird mit eine Wert von -10 initialisiert
     */
    public BlackChip(){

        super(-10);
    }

    /**
     * gibt max. Anzahl an schwarzen Chips zurück.
     * @return Max. Anzahl Chips
     */
    public int getMaxChips(){
        return this.MAX_CHIPS;
    }
}