package Main;

import java.io.Serializable;

/**
 * Die Tisch Klasse dient zur verwaltung der Spieler und als Schnittstelle der GUI. Informationen werden von hier an andere Klassen weitergegeben.
 */
public class Tisch implements Serializable {

    private BlackChip chipsblack = new BlackChip();
    private WhiteChip whiteChip = new WhiteChip();
    private int blackCh;
    private int whiteCh;
    private int durchgangNr = 0;
    private Stapel nachziehStapel = new Stapel(true);
    public Stapel ablageStapel = new Stapel(false);
    public Spieler[] spielerList;
    public volatile int aktiv;




    public Tisch(Spieler[] spieler) {
        blackCh = chipsblack.getMaxChips();
        whiteCh = whiteChip.getMaxChips();
        this.spielerList = spieler;
        aktiv = (int) (Math.random()*getAnzSpieler());
    }

    /**
     * @return Gibt die Liste der aktuellen Spieler zurück, die "an dem Tisch sitzen".
     */
    public Spieler[] getSpielerList() {
        return spielerList;
    }


    /**
     *Erhöt die durchgangNr (Durchgangsnummer) um 1.
     * Methode ist ein Counter um die Durchgänge zu zählen.
     */
    public void nextDurchgang(){
        durchgangNr += 1;
        ablageStapel = new Stapel(false);

    }

    /**
     * weiße/schwarze Chips werden von dem Chip Stapel entfernt/hinzugefügt, der als Integer verwaltet wird.
     * Um den Tisch Chips hinzuzufügen, muss der Parameter nur negativ angegeben werden.
     * @param whiteChips
     * @param blackChips
     */
    public void takeChips(int whiteChips, int blackChips){
        blackCh = blackCh - blackChips;
        whiteCh = whiteCh - whiteChips;
    }

    /**
     * @return Oberste Karte des Nachziehstapels und entfernt diese vom Stapel.
     * @throws Exception
     */
    public Karte karteZiehen() {
        return nachziehStapel.ziehen();
    }

    /**
     * Legt gewählte Karte auf den AblageStapel
     * @param karte
     */
    public void karteAblegen(Karte karte) {
        ablageStapel.ablegen(karte);
    }

    /**
     * Initiert den Nachziehstapel. 56 Karten werden hinzugefügt.
     * 8*(card1, card2, card3, card4, card5, card6, lama)
     * @throws Exception von Methode Stapel.addCard().
     */
    public void initNachziehstapel() {
        nachziehStapel = new Stapel(true);
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
     *Methode um den Nachziehstapel zu mischen.
     */
    public void mischenNachziehstapel(){
        nachziehStapel.mischen();
    }

    //*****************Getter*******************

    /**
     * @return gibt die Anzahl der aktuellen Spieler zurück.
     */
    public int getAnzSpieler(){
        return spielerList.length;
    }

    /**
     * @return Gibt die Anzahl der aktuellen weißen Chips zurück, die auf dem Tisch liegen/vorhanden sind.
     */
    public int getWhiteChips() {
        return whiteCh;
    }

    /**
     * @return Gibt die Anzahl der aktuellen schwarzen Chips zurück, die auf dem Tisch liegen/vorhanden sind.
     */
    public int getBlackChips(){
        return blackCh;
    }

    /**
     * @return Gibt Oberste Karte des Ablagestapels zurück. Diese wird nicht entfernt.
     * @throws Exception von Methode Stapel.getTopCard().
     */
    public Karte getObereKarteAblagestapel(){
        return ablageStapel.getTopCard();
    }

    /**
     * @return Gibt aktuelle durchgangNr (Durchgangsnummer) in Integer zurück
     */
    public int getDurchgangNr(){
        return durchgangNr;
    }

    /**
     * @return Gibt größe des Nachziehstapels in Integer zurück.
     */
    public int getNachziehStapelSize(){
        return nachziehStapel.getCardCount();
    }

    /**
     * @return Gibt größe des Ablagestapels in Integer zurück.
     */
    public int getAblageStapelSize(){
        return ablageStapel.getCardCount();
    }


    /**
     * Kommt auf naechste Spieler
     */
    public Spieler naechste(){
        do {
            aktiv = (aktiv+1)%getAnzSpieler();}
        while(!spielerList[aktiv].inGame());

        if(!(spielerList[aktiv] instanceof Bot))
            System.out.println(spielerList[aktiv].getName()+" ist dran:");
        System.out.println("Tischaktiv:"+aktiv);
        return spielerList[aktiv];
    }

    /**
     * @return Spieler
     * gibt der Spieler der gerade spielt zurück
     */
    public Spieler getAktivSpieler(){
        return spielerList[aktiv];
    }


    /** setter-Methode für den Ablagestapel
     * @param stapel Der neue Ablagestapel
     */
    public void setAblageStapel(Stapel stapel){
        this.ablageStapel = stapel;
    }

    /** setter-Methode für den Nachziehstapel
     * @param stapel Der neue Nachziehstapel
     */
    public void setNachziehstapel(Stapel stapel){
        this.nachziehStapel = stapel;
    }

    /** setter-Methode für aktiv
     * @param aktiverSpieler neuer aktiver Spieler
     */
    public void setAktiv(int aktiverSpieler){
        this.aktiv = aktiverSpieler;

    }


}
