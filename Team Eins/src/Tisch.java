public class Tisch {

    private int spieler;
    private BlackChip chipsblack = new BlackChip();
    private WhiteChip whiteChip = new WhiteChip();
    private int blackCh;
    private int whiteCh;
    private int durchgangNr;
    private Stapel nachziehStapel = new Stapel(true);
    private Stapel ablageStapel = new Stapel(false);

    public Tisch(int anzSpieler) throws Exception {
        blackCh = chipsblack.getMaxChips();
        whiteCh = whiteChip.getMaxChips();
        spieler = anzSpieler;
    }


    public void neuerDurchgang(){
        durchgangNr = 0;
    }
    public void nextDurchgang(){
        durchgangNr += 1;
    }
    public void takeChips(int whiteChips, int blackChips){
        blackCh = blackCh - blackChips;
        whiteCh = whiteCh - whiteChips;
    }
    public Karte karteZiehen() throws Exception {
        return nachziehStapel.ziehen();
    }
    public void karteAblegen(Karte karte){
        ablageStapel.ablegen(karte);
    }
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
    public void mischenNachziehstapel(){
        nachziehStapel.mischen();
    }

    //*****************Getter*******************

    public int getAnzSpieler(){
        return spieler;
    }
    public int getWhiteChips() {
        return whiteCh;
    }
    public int getBlackChips(){
        return blackCh;
    }
    public Karte getObereKarteAblagestapel() throws Exception {
        return ablageStapel.getTopCard();
    }
    public int getDurchgangNr(){
        return durchgangNr;
    }
    public int getNachziehStapelSize(){
        return nachziehStapel.getCardCount();
    }
    public int getAblageStapelSize(){
        return ablageStapel.getCardCount();
    }
}
