import java.util.List;

public class Hand {
    private int valueSum;
    private List<Karte> handKarte;
    //private Spieler spieler;

    public Hand(/*Spieler entrySpieler*/){
        //spieler = entrySpieler;
        //TODO add Spieler
    }
    public void aussteigen(){
        //TODO spieler.aussteigen();
    }
    public void setSum(int i){
        valueSum += i;
    }
    public int getSum(){
        return valueSum;
    }

    public List<Karte> getHandKarte() {
        return handKarte;
    }
    public void addKarte(Karte karte){
        handKarte.add(karte);
    }
    public void removeKarte (Karte karte){
        handKarte.remove(karte);
    }
}
