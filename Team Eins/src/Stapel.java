import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Stapel {
    private int cardCount;
    private boolean isAblage;
    private Stack<Karte> stapel;

    public Stapel(int cardCount) {
        this.cardCount = cardCount;
    }

    public Karte nehmen() throws Exception {
        if(stapel.isEmpty()){
            throw new Exception("Stapel leer");
        }
        if(!isAblage){
            Karte karte = stapel.pop();
            return karte;
        }

        throw new Exception("Stapel leer");
    }

    public void ablegen(Karte karte){
        if (isAblage){
            stapel.add(karte);
        }
    }

    public void mischen(){
        ArrayList<Karte> kartenStapel = new ArrayList<Karte>();

        while(!stapel.isEmpty()){
            Karte karte = stapel.pop();
            kartenStapel.add(karte);
        }
        Collections.shuffle(kartenStapel);

        int length = kartenStapel.size();
        for(int i = 0; i <= length-1; i++){
            stapel.push(kartenStapel.get(i));
        }
    }
}
