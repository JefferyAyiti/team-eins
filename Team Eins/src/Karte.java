public class Karte {
int value;
boolean covered;

    public Karte(int value, boolean covered) {
        this.value = value;
        this.covered = covered;
    }

    public int getValue() {
        return value;
    }

    public boolean isCovered() {
        return covered;
    }

    public void cover() {
        covered = true;
    }

    public void unCover() {
        covered = false;
    }
}

