import java.util.ArrayList;
import java.util.List;

public class Hand {
    private int valueSum;
    private ArrayList<HandKarte> handKarten = new ArrayList<HandKarte>();
    private Spieler spieler;
    private Tisch tisch;

    public Hand(){
    }

    public Hand(ArrayList<HandKarte> karten){
        handKarten = karten;
    }
    public void setSum(int i){
        valueSum += i;
    }
    public void setTisch(Tisch entryTisch){
        tisch = entryTisch;
    }
    public void setSpieler(Spieler entrySpieler){
        spieler = entrySpieler;
    }

    public int getSum(){
        return valueSum;
    }
    public Tisch getTisch(){
        return tisch;
    }
    public Spieler getSpieler(){
        return spieler;
    }


    public List<HandKarte> getHandKarte() {
        return handKarten;
    }

    public HandKarte getKarte(int nr) {
        return handKarten.get(nr);
    }
    public void addKarte(Karte karte){
        HandKarte hk = new HandKarte(karte.getValue(), false, tisch, this);
        handKarten.add(hk);
    }
    public void removeKarte (HandKarte karte){
        handKarten.remove(karte);
    }
}
