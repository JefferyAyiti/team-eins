import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Stapel {
    private int cardCount;
    private boolean coverCard;
    private Stack<Karte> stapel;
    private Karte topCard;

    public Stapel(boolean coverCard,int cardCount) {
        this.cardCount = cardCount;
        this.coverCard = coverCard;
    }

    public Karte ziehen() throws Exception {
        if(stapel.isEmpty()){
            throw new Exception("Stapel leer");
        }

        if(coverCard){    //Ziehstapel
            Karte karte = stapel.pop();
            cardCount = cardCount - 1;
            return karte;
        }

        throw new Exception("Stapel leer");
    }

    public void ablegen(Karte karte){
        if (!coverCard){    //Ablagestapel
            stapel.add(karte);
            cardCount = cardCount + 1;
            topCard = stapel.peek();
        }
    }

    public void mischen(){
        ArrayList<Karte> kartenStapel = new ArrayList<Karte>();

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
    public void Reset(){
         stapel = new Stack();
    }

    public int getCardCount() {
        return cardCount;
    }
    public Karte getTopCard() throws Exception {
        if(!coverCard){
            return topCard;
        }
        throw new Exception();
    }
}
