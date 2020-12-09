package Main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Stapeldatenstruktur für den Zieh und -Ablagestapel.
 */
public class Stapel implements Serializable {

    /**
     * cardCount steht für die Anzahl der Karten im Stapel
     */
    private int cardCount = 0;
    /**
     * coverCard steht für den Typ des Stapels.
     * True für Ziehstapel. False für Ablagestapel
     */
    private boolean coverCard;
    /**
     * stapel steht für den Stapel dargestellt als stack
     */
    public Stack<Karte> stapel = new Stack<>();
    /**
     * topCard ist die oberste Karte des Stapels
     */
    private Karte topCard;

    /** Der Stapel wird mit dem Typ des Stapels initialisiert
     * @param coverCard True für Ziehstapel. False für Ablagestapel
     */
    public Stapel(boolean coverCard) {
        this.coverCard = coverCard;
    }

    /**Ziehe die oberste Karte aus dem Ziehstapel.
     *  Vorher muss geguckt werden, ob es sich um den Ziehstapel handelt.
     * @return Karte die gezogen wurde
     * @throws Exception
     */
    public Karte ziehen()  {
        if(stapel.isEmpty()){
           return null;
        }

        if(coverCard){    //Ziehstapel
            Karte karte = stapel.pop();
            cardCount = cardCount - 1;
            return karte;
        }

        return null;
    }


    /**Falls es sich um den Ablagestapel handelt, lege die Karte auf dem Ablagestapel.
     * @param karte Diese Karte wird auf dem Ablagestapel gelegt
     * @throws Exception
     */
    public boolean ablegen(Karte karte) {
        if (!coverCard){    //Ablagestapel
            stapel.add(karte);
            cardCount = cardCount + 1;
            topCard = stapel.peek();
            return true;
        } else
        return false;
    }

    /**Mische den Kartenstapel
     *
     */
    public void mischen(){
        ArrayList<Karte> kartenStapel = new ArrayList<>();

        while(!stapel.isEmpty()){           //stapel leeren und in liste packen
            Karte karte = stapel.pop();
            kartenStapel.add(karte);
        }
        Collections.shuffle(kartenStapel);    //shuffle liste

        int length = kartenStapel.size();
        for(int i = 0; i <= length-1; i++){
            stapel.push(kartenStapel.get(i));       //wieder auf stack packen
        }
    }

    /**
     * Kartenstapel leeren
     */
    public void Reset(){
         stapel = new Stack<>();
    }

    /** getter-Methode für cardCount
     * @return cardCount
     */
    public int getCardCount() {
        return cardCount;
    }

    /** getter-Methode für coverCard
     * @return coverCard
     * @throws Exception
     */
    public Karte getTopCard(){
            return topCard;
    }

    /**Füge zu dem Ziehstapel die Karte hinzu.
     * @param karte Diese Karte wird zum Ziehstapel hinzugefügt
     */
    public void addCard(Karte karte) {
        if(coverCard){
            stapel.push(karte);
            cardCount = cardCount + 1;
        }
    }
}
