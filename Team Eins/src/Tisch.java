public class Tisch {

    private int spieler;
    private BlackChip chipsblack = new BlackChip();
    private WhiteChip whiteChip = new WhiteChip();
    private int blackCh;
    private int whiteCh;
    private int durchgangNr;
    private Stapel nachziehStapel = new Stapel(56);
    private Stapel ablageStapel = new Stapel(0);

    public Tisch(int anzSpieler) throws Exception {
        blackCh = chipsblack.getMaxChips();
        whiteCh = whiteChip.getMaxChips();
        spieler = anzSpieler;
    }


    public void neuerDurchgang(){
        durchgangNr = 0;
    }
    public void takeChips(int whiteChips, int blackChips){
        blackCh = blackCh - blackChips;
        whiteCh = whiteCh - whiteChips;
    }
    public Karte karteZiehen() throws Exception {
        return nachziehStapel.nehmen();
    }
    public void karteAblegen(Karte karte){
        ablageStapel.ablegen(karte);
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
    public Karte getObereKarteAblagestapel(){
        //TODO
        return null;
    }
}
