package Main;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.UUID;

import static Main.Main.ich;
import static Main.Main.tisch;

public class Spieler  implements Serializable {
    int points = 0;
    int oldScore=0;
    Hand cardHand;
    String playerName;
    String uid;
    int blackChips = 0, whiteChips = 0;
    private boolean letzerSpielerDurchgang = false;
    private boolean aussteigen = false;
    private boolean leftServer = false;
    protected int schwierigkeit = 0;


    /**
     * Erstellt Spieler Object mit Spielername
     * @param playerName Spieler Name
     */
    public Spieler(String playerName, String id) {
        this.uid = id;
        this.playerName = playerName;
    }

    public String getUid() {
        return uid;
    }

    /**
     * Weist dem Spieler eine Kartenhand zu
     * @param hand aktuelle Karten auf der Hand
     */
    public void setCardHand(Hand hand) {
        cardHand = hand;
}

    /**
     *Gibt den Namen des Spielers zurück.
     * @return Spielername
     */
    public String getName() {
        return playerName;
 }

    /**
     * Gibt aktuellen Punktestand des Spieler zurück.
     * @return Punktestand
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setzt neuen Punktestand.
     * @param points Punktestand des Spielers
     */
    public void setPoints(int points) {
        this.points = points;
    }

    public void einsteigen(){
        aussteigen = false;
    }
    public void aussteigen(){
        System.out.println("Aussteigen: " + aussteigen);
        aussteigen = true;
        System.out.println("in Game: " + tisch.getSpielerList()[ich].inGame());
        //Main.tisch.naechste();
    }
    public boolean inGame() {
        return !aussteigen;
    }


    /**
     * Gibt zurück wie viele schwarze Chips der Spieler hat.
     * @return Anzahl der schwarzen Chips
     */
    public int getBlackChips() {
        return blackChips;
    }

    /**
     * Gibt Handkarten zurück
     * @return Karten auf der Hand
     */
    public Hand getCardHand() {
        return cardHand;
    }

    public void setBlackChips(int blackChips) {
        this.blackChips = blackChips;
    }

    /**
     * Gibt zurück wie viele weiße Chips der Spieler hat.
     * @return Anzhal der weißen Chips im Besitz
     */
    public int getWhiteChips() {
        return whiteChips;
    }

    public void setWhiteChips(int whiteChips) {
        this.whiteChips = whiteChips;
    }

    /**
     * Gibt zurück wie viele Karten der Spieler auf der Hand hat
     * @return Anzahl der Karten auf der Hand
     */
    public int getCardCount() {
        return cardHand.getHandKarte().size();
    }

    /**
     * tauscht 10 weiße Chips gegen einen schwarzen Chip
     */
    void chipTausch() {

    }


    /**
     * @return letzerSpielerDurchgang.Gibt an ab der Spieler noch am Ende ablegen darf(ohne nachziehen).
     */
    public boolean isLetzerSpielerDurchgang(){
        return letzerSpielerDurchgang;
    }

    /**
     * setter-Methode für letzerSpielerDurchgang
     */
    public void setLetzerSpielerDurchgang(boolean ende) {
        this.letzerSpielerDurchgang = ende;
    }

    /**
     * @return gibt alten Punktestand zurück
     */
    public int getOldScore() {
        return oldScore;
    }

    /**
     * legt alten Punktestand fest
     * @param alt alter Punktestand
     */
    public void setOldScore(int alt) {
        oldScore = alt;
    }

    public int getSchwierigkeit() {
        return schwierigkeit;
    }

    /**Checked ob der Spieler das Spiel verlassen hat.
     * @return true wenn der Spieler das Spiel verlassen hat,
     * false wenn der Spieler noch im Spiel ist.
     */
    public boolean getLeftServer(){
        return leftServer;
    }

    public void setLeftServer(boolean bool){
        leftServer = bool;
    }

    @Override
    public boolean equals(Object object) {
        return playerName == ((Spieler)object).getName() &&
                ((Spieler)object).getCardHand().equals(cardHand);
    }

    @Override
    public String toString() {
        return "("+getName()+" "+getUid()+")";
    }
}