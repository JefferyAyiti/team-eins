public class Bot extends Spieler {

    private int schwierigkeit;
   // private Tisch tisch = Main.tisch;
    private boolean zug = false;

    /**
     * Erstellt Spieler/Bot Object mit Spielername
     *
     * @param playerName Spieler Name
     */
    public Bot(String playerName, int entrySchwierigkeit) {
        super(playerName);

        schwierigkeit = entrySchwierigkeit;
    }

    boolean play() {
        setZug(true);
        switch (schwierigkeit) {
            case 1:
                playSchwierigkeitLeicht();
                break;
            case 2:
                playSchwierigkeitMittel();
                break;
            case 3:
                playSchwierigkeitSchwer();
                break;
        }

        //Main.tisch.naechste();
        return true;
    }

    /**
     * @return Gibt den schwierigkeitsgrad des Bots zurück. (1 := Leicht, 2:= Mittel, 3 := Schwer)
     */
    public int getSchwierigkeit() {
        return schwierigkeit;
    }

    /**
     * Bot versucht immer Chips zu tauschen, danach spielt er in folgender Reihenfolge:
     * - Versuch in der Reihenfolge der Handkarte eine Karte abzulegen.
     * - Wenn ablegen nicht möglich ziehe eine Karte vom Stapel
     * - Wenn ziehen nicht möglich steige aus.
     */
    public void playSchwierigkeitLeicht() {
        Karte card;
        Main.spiellogik.chipsTauschen(this);

        //Main.spiellogik.aussteigen(this);

        for (int i = 0; i < this.getCardCount(); i++) {
            card = this.getCardHand().getKarte(i);
            if (Main.spiellogik.karteLegen(this, card)) {
                System.out.println("\tLege " + card.getValue());
                return;
            }
        }
        if (Main.spiellogik.karteNachziehen(this)) {
            System.out.println("\tZiehe");
            return;
        } else {
            System.out.println("\tSteige aus");
            Main.spiellogik.aussteigen(this);
        }

    }

    /**
     * Mittelschwere Bot Methode:
     * Bot führt Aktionen in folgender Reihenfolge aus:
     * - Chips umtauschen
     * - Karten legen wenn möglich
     * - Wenn keine Karten vorhanden größtmöglichen Chip abgeben
     * - Karte ziehen wenn möglich (mit einer wskeit)
     * - sonst Zug beendet
     * <p>
     * Um Methode zu verwenden muss setZug(true) aufgerufen werden.
     * Methode setzt setZug(false) automatisch
     */
    public void playSchwierigkeitMittel() {
        Tisch tisch = Main.tisch;
        Spiellogik logik = Main.spiellogik;

        boolean gleicheKarte = true;
        boolean abgelegt = false;
        boolean noChange = true;

        if (inGame()) {
            if (zug) {
                //Chips umtauschen
                while (this.whiteChips >= 10) {
                    logik.chipsTauschen(this);
                    System.out.println("\t tausche");
                }

                //for Schleife für das Karten legen
                for (int i = 0; i < this.getCardCount(); i++) {
                    HandKarte karte = this.cardHand.getKarte(i);

                    if (gleicheKarte && Main.tisch.getObereKarteAblagestapel().value == karte.value) {//gleicher Wert
                        System.out.println("\t lege gleich " + karte.getValue());
                        logik.karteLegen(this, karte);
                        abgelegt = true;
                        noChange = false;
                        break;
                    }
                }

                for (int i = 0; i < this.getCardCount() && noChange; i++) {
                    HandKarte karte = this.cardHand.getKarte(i);

                    if ((tisch.getObereKarteAblagestapel().value == karte.value - 1
                            || (tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                            || (tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1))) {// Handkarte um eins größer
                        System.out.println("\t lege höher " + karte.getValue());
                        logik.karteLegen(this, karte);
                        abgelegt = true;
                        break;
                    }
                }
                //Kartenlegen Ende//

                //Prüfen ob noch Karten vorhanden
                if (this.getCardCount() == 0) {
                    Main.spiellogik.aussteigen(this);
                    System.out.println("\t steige aus keine Karten");
                }

                //Karten ziehen wenn noch keine Karte abgelegt wurde, wenn man am Zug ist, Wenn man mehr als 3 Karten hat,
                //und 20% wskeit kommt dazu. Sonst wird ausgestiegen
                if (!abgelegt && (this.getCardCount() >= 4 && Math.random() <= 0.2)  ){
                    Main.spiellogik.karteNachziehen(this);
                    System.out.println("\t Karte nachziehen");
                }else if(!abgelegt){
                    Main.spiellogik.aussteigen(this);
                    System.out.println("\t steige aus");
                }
            }
        }
    }


    public void playSchwierigkeitSchwer() {
        //TODO
    }

    /**
     * Lässt den Bot Chips ablegen, wenn er alle Karten abgelegt hat.
     * <p>
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    private void chipAbgeben() {
        Tisch tisch = Main.tisch;
        if (this.getBlackChips() > 0) {
            this.setBlackChips(this.getBlackChips() - 1);

            tisch.takeChips(0, -1);

        } else if (this.getWhiteChips() > 0) {
            this.setWhiteChips(this.getWhiteChips() - 1);

            tisch.takeChips(-1, 0);
        }
    }

    /**
     * Tauscht 10 weiße Chips gegen 1 schwarzen Chip aus.
     * <p>
     * Die Methode ist Public, da falls es zu wenig weiße Chips auf dem Feld gibt, man den Bot forcieren kann, seine Chips
     * zu tauschen.
     * <p>
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    public void chipsTauschen() {
        Tisch tisch = Main.tisch;

        if (this.getWhiteChips() >= 10 || tisch.getBlackChips() > 0) {

            this.setBlackChips(this.getBlackChips() + 1);
            this.setWhiteChips(this.getWhiteChips() - 10);

            tisch.takeChips(-10, 1);

        }
    }

    /**
     * Gibt dem Bot bescheid, dass sein Zug beginnen kann.
     * Eine playSchwierigkeit() Methode muss allerdings aufgerufen werden, damit der Bot beginnen kann.
     * <p>
     * Setzt den Parameter selbständig auf false, wenn er fertig ist.
     *
     * @param entryZug
     */
    public void setZug(boolean entryZug) {
        zug = entryZug;
    }


}
