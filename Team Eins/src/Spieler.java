public class Spieler {
    int points = 0;
    Tisch tisch;
    Hand cardHand;
    String playerName;
    int blackChips = 0, whiteChips = 0;
    boolean folded = false;
    boolean isPlaying = false;

    public Spieler(Hand cardHand, String playerName, Tisch tisch) {
        this.cardHand = cardHand;
        this.playerName = playerName;
        this.tisch = tisch;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean inGame() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    public boolean getIsPlaying(){
        return isPlaying;
    }

    public void setisPlaying(boolean entryIsplaying){
        isPlaying = entryIsplaying;
    }

    public int getBlackChips() {
        return blackChips;
    }

    public void setBlackChips(int blackChips) {
        this.blackChips = blackChips;
    }

    public int getWhiteChips() {
        return whiteChips;
    }

    public void setWhiteChips(int whiteChips) {
        this.whiteChips = whiteChips;
    }

    int getCardCount() {
        return cardHand.getHandKarte().size();
    }

    void chipTausch() {
        if(tisch.getBlackChips() >= 1 && whiteChips >= 10) {
            tisch.takeChips(-10, 1);
            blackChips++;
            whiteChips -= 10;
        }
    }
}
