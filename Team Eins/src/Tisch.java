public class Tisch {

    private BlackChip chipsblack = new BlackChip();
    private WhiteChip whiteChip = new WhiteChip();
    private int blackCh;
    private int whiteCh;
    private int durchgangNr = 0;
    private Stapel nachziehStapel = new Stapel(true);
    private Stapel ablageStapel = new Stapel(false);
    private Spieler[] spieler;



    public Tisch(Spieler[] spieler) {
        blackCh = chipsblack.getMaxChips();
        whiteCh = whiteChip.getMaxChips();
        this.spieler = spieler;
    }

    /**
     * @return Gibt die Liste der Spieler zurück
     */
    Spieler[] getSpieler() {
        return spieler;
    }


    /**
     *Erhöt den Durchgang um 1
     */
    public void nextDurchgang(){
        durchgangNr += 1;
    }

    /**
     * Bestimmte Chips werden von dem Chip Stapel entfernt
     * @param whiteChips
     * @param blackChips
     */
    public void takeChips(int whiteChips, int blackChips){
        blackCh = blackCh - blackChips;
        whiteCh = whiteCh - whiteChips;
    }

    /**
     * @return Oberste Karte des Nachziehstapels und entfernt diese vom Stapel
     * @throws Exception
     */
    public Karte karteZiehen() throws Exception {
        return nachziehStapel.ziehen();
    }

    /**
     * Legt gewählte Karte auf den AblageStapel
     * @param karte
     */
    public void karteAblegen(Karte karte) throws Exception {
        ablageStapel.ablegen(karte);
    }

    /**
     * Initiert den Nachziehstapel. 56 Karten werden hinzugefügt.
     * @throws Exception
     */
    public void initNachziehstapel() throws Exception {
        for (int i = 0; i<7; i++){
            for(int j = 0; j<8; j++){
                switch (i){
                    case 0: nachziehStapel.addCard(new Karte(1, true));break;
                    case 1: nachziehStapel.addCard(new Karte(2, true));break;
                    case 2: nachziehStapel.addCard(new Karte(3, true));break;
                    case 3: nachziehStapel.addCard(new Karte(4, true));break;
                    case 4: nachziehStapel.addCard(new Karte(5, true));break;
                    case 5: nachziehStapel.addCard(new Karte(6, true));break;
                    case 6: nachziehStapel.addCard(new Karte(10, true));break;
                }
            }
        }
    }

    /**
     *Nachziehstapel wird gemischt
     */
    public void mischenNachziehstapel(){
        nachziehStapel.mischen();
    }

    //*****************Getter*******************

    /**
     * @return Anzahl Spieler
     */
    public int getAnzSpieler(){
        return spieler.length;
    }

    /**
     * @return Anzahl weiße Chips
     */
    public int getWhiteChips() {
        return whiteCh;
    }

    /**
     * @return Anzahl schwaze Chips
     */
    public int getBlackChips(){
        return blackCh;
    }

    /**
     * @return Oberste Karte des Ablagestapel. Diese wird nicht entfernt.
     * @throws Exception
     */
    public Karte getObereKarteAblagestapel() throws Exception {
        return ablageStapel.getTopCard();
    }

    /**
     * @return Aktuelle durchgangsNummer
     */
    public int getDurchgangNr(){
        return durchgangNr;
    }

    /**
     * @return Größe des Nachziehstapels
     */
    public int getNachziehStapelSize(){
        return nachziehStapel.getCardCount();
    }

    /**
     * @return Größe des Ablagestapels
     */
    public int getAblageStapelSize(){
        return ablageStapel.getCardCount();
    }
}
