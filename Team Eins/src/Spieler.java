public class Spieler {
    int points = 0;
    Hand cardHand;
    String playerName;
    int blackChips = 0, whiteChips = 0;
    boolean folded = false;

    /**
     * Erstellt Spieler Object mit Spielername
     * @param playerName Spieler Name
     */
    public Spieler(String playerName) {
        this.playerName = playerName;
    }


    /**
     * Weißt dem Spieler eine Kartenhand zu
     * @param hand
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

    public boolean inGame() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
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
    int getCardCount() {
        return cardHand.getHandKarte().size();
    }

    /**
     * tauscht 10 weiße Chips gegen einen schwarzen Chip
     */
    void chipTausch() {

    }
}
