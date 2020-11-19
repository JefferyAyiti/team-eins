public class Bot extends Spieler{

    private int schwierigkeit;
    private Tisch tisch;
    private boolean zug = false;

    /**
     * Erstellt Spieler/Bot Object mit Spielername
     *
     * @param playerName Spieler Name
     */
    public Bot(String playerName, int entrySchwierigkeit, Tisch entryTisch) {
        super(playerName);

        tisch = entryTisch;
        schwierigkeit = entrySchwierigkeit;
    }

    /**
     * @return Gibt den schwierigkeitsgrad des Bots zurück. (1 := Leicht, 2:= Mittel, 3 := Schwer)
     */
    public int getSchwierigkeit(){
        return schwierigkeit;
    }

    /**
     * TODO
     */
    public void playSchwierigkeitLeicht(){

    }

    /**
     * Mittelschwere Bot Methode:
     * Bot führt Aktionen in folgender Reihenfolge aus:
     * - Chips umtauschen
     * - Karten legen wenn möglich
     * - Wenn keine Karten vorhanden größtmöglichen Chip abgeben
     * - Karte ziehen wenn möglich (mit einer wskeit)
     * - sonst Zug beendet
     *
     * Um Methode zu verwenden muss setZug(true) aufgerufen werden.
     * Methode setzt setZug(false) automatisch
     */
    public void playSchwierigkeitMittel(){

        boolean abgelegt = false;

        if(inGame()){
            if(zug){
                //Chips umtauschen
                while(this.whiteChips >= 10){
                    this.chipsTauschen();
                }

                //for Schleifen für das Karten legen
                for(int i = 0;i < this.getCardCount() ; i++){
                    HandKarte karte = this.cardHand.getKarte(i);

                    try {
                        if(tisch.getObereKarteAblagestapel().value == karte.value ){//gleicher Wert
                            this.karteLegen(karte);
                            abgelegt = true;
                        }
                    } catch (Exception e) {
                    }
                }

                for(int i = 0;i < this.getCardCount() ; i++){
                    HandKarte karte = this.cardHand.getKarte(i);

                    try {
                        if(tisch.getObereKarteAblagestapel().value == karte.value - 1  || (tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                                || (tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1)   ){// Handkarte um eins größer
                            this.karteLegen(karte);
                            abgelegt = true;
                        }
                    } catch (Exception e) {
                    }
                }
                //Kartenlegen Ende//

                //Prüfen ob noch Karten vorhanden
                if(this.getCardCount() == 0){
                    this.chipAbgeben();
                    this.aussteigen();
                    this.setZug(false);
                }

                //Karten ziehen wenn noch keine Karte abgelegt wurde, wenn man am Zug ist, Wenn man mehr als 3 Karten hat,
                //und 50% wskeit kommt dazu. Sonst wird ausgestiegen
                if(this.zug && !abgelegt && this.getCardCount() >= 4 && Math.random() <= 0.5){
                    this.karteNachziehen();
                    this.setZug(false);
                }else{
                    this.aussteigen();
                    this.setZug(false);
                }
            }
        }
    }

    /**
     * TODO
     */
    public void playSchwierigkeitSchwer(){

    }

    /**
     * Lässt den Bot Chips ablegen, wenn er alle Karten abgelegt hat.
     *
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    private void chipAbgeben(){
        if(this.getBlackChips() > 0){
            this.setBlackChips(this.getBlackChips() -1);

            tisch.takeChips(0,-1);

        }else if(this.getWhiteChips() > 0){
            this.setWhiteChips(this.getWhiteChips() - 1);

            tisch.takeChips(-1,0);
        }
    }

    /**
     * Setzt folded = true.
     * Somit steigt der Bot aus.
     *
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */

    /**
     * Tauscht 10 weiße Chips gegen 1 schwarzen Chip aus.
     *
     * Die Methode ist Public, da falls es zu wenig weiße Chips auf dem Feld gibt, man den Bot forcieren kann, seine Chips
     * zu tauschen.
     *
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    public void chipsTauschen(){

        if(this.getWhiteChips() >= 10 || tisch.getBlackChips() > 0){

            this.setBlackChips(this.getBlackChips() + 1);
            this.setWhiteChips(this.getWhiteChips() - 10);

            tisch.takeChips(-10, 1);

        }
    }

    /**
     * Lässt den Bot eine Karte nachziehen.
     *
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    private void karteNachziehen(){
       try {
           //TODO
           // regelüberprüfung
            this.getCardHand().addKarte(tisch.karteZiehen());
        } catch (Exception e) {
        }
    }

    /**
     * Lässt den Bot eine Karte legen, wenn die Regeln es zulassen.
     *
     * @param karte
     *
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    private void karteLegen(HandKarte karte){
        try {
            if (karte.isPlayable()) {
                this.getCardHand().removeKarte(karte);
                tisch.karteAblegen(karte);
            }


        } catch (Exception e) {
        }
    }

    /**
     * Gibt dem Bot bescheid, dass sein Zug beginnen kann.
     * Eine playSchwierigkeit() Methode muss allerdings aufgerufen werden, damit der Bot beginnen kann.
     *
     * Setzt den Parameter selbständig auf false, wenn er fertig ist.
     *
     * @param entryZug
     */
    public void setZug(boolean entryZug){
        zug = entryZug;
    }


}
