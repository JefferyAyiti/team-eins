public class WhiteChip extends Chip {

    private final int MAX_CHIPS = 50;

    /**
     *Ein weißer Chip wird mit eine Wert von -1 initialisiert.
     */
    public WhiteChip(){

        super(-1);
    }

    /**
     * Gibt max. Anzahl an weißen Chips zurück.
     * @return Max. Anzahl an Chips
     */
    public int getMaxChips(){
        return MAX_CHIPS;
    }
}
