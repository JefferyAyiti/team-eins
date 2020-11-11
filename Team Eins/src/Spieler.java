public class Spieler {
    int points = 0;
    Hand cardHand;
    String playerName;
    int blackChips = 0, whiteChips = 0;
    boolean folded = false;

    public Spieler(Hand cardHand, String playerName) {
        this.cardHand = cardHand;
        this.playerName = playerName;
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
}
