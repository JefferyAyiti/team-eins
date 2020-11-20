public class Bot extends Spieler{

    private int schwierigkeit;
    private boolean zug = false;
    private Spiellogik logik;

    /**
     * Erstellt Spieler/Bot Object mit Spielername
     *
     * @param playerName Spieler Name
     */
    public Bot(String playerName, int entrySchwierigkeit, Spiellogik entryLogik) {
        super(playerName);
        logik = entryLogik;
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
     * Bot ist nur aktiv wenn this(Spieler).aussteigen == true
     */
    public void playSchwierigkeitMittel(){

        boolean abgelegt = false;

        if(inGame()){
            //Chips umtauschen
            while(this.whiteChips >= 10){
                logik.chipsTauschen(this);
            }

            //for Schleife für das Karten legen
            for(int i = 0;i < this.getCardCount() ; i++){
                HandKarte karte = this.cardHand.getKarte(i);

                try {
                    if(logik.tisch.getObereKarteAblagestapel().value == karte.value ){//gleicher Wert
                        logik.karteLegen(this, karte);
                        abgelegt = true;
                        break;

                    }else if(logik.tisch.getObereKarteAblagestapel().value == karte.value - 1  || (logik.tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                            || (logik.tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1)   ){// Handkarte um eins größer
                        logik.karteLegen(this, karte);
                        abgelegt = true;
                        break;
                    }
                } catch (Exception e) {
                }
            }


            //Kartenlegen Ende//

            //Prüfen ob noch Karten vorhanden
            if(this.getCardCount() == 0){
                this.chipAbgeben();
            }

            //Karten ziehen wenn noch keine Karte abgelegt wurde, wenn man am Zug ist, Wenn man mehr als 3 Karten hat,
            //und 50% wskeit kommt dazu. Sonst wird ausgestiegen
            if(this.zug && !abgelegt && this.getCardCount() >= 4 && Math.random() <= 0.5){
                logik.karteNachziehen(this);
            }else{
                this.aussteigen();
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
     * Methode ruft Spiellogik.chipAbgeben(Spieler, Chip) auf.
     */
    private void chipAbgeben(){
        if(this.getBlackChips() > 0){
            logik.chipAbgeben(this,new BlackChip());

        }else if(this.getWhiteChips() > 0){
            logik.chipAbgeben(this, new WhiteChip());
        }
    }
}
