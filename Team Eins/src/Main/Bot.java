package Main;

import java.io.Serializable;
import java.util.*;

import static Main.Main.anzSpieler;
import static Main.Main.spiellogik;

public class Bot extends Spieler  implements Serializable {

    private int schwierigkeit;
    private boolean zug = false;

    /**
     * Erstellt Spieler/Bot Object mit Spielername
     *
     * @param playerName Spieler Name
     */
    public Bot(String playerName, int entrySchwierigkeit) {
        super(playerName, UUID.randomUUID().toString());

        schwierigkeit = entrySchwierigkeit;
    }

    boolean play() {
        setZug(true);
        switch (schwierigkeit) {
            case 1:
                playSchwierigkeitLeicht();
                break;
            case 2:
                playSchwierigkeitMittel();
                break;
            case 3:
                playSchwierigkeitSchwer();
                break;
        }

        //Main.tisch.naechste();
        return true;
    }

    /**
     * Bot versucht immer Chips zu tauschen, danach spielt er in folgender Reihenfolge:
     * - Versuch in der Reihenfolge der Handkarte eine Karte abzulegen.
     * - Wenn ablegen nicht möglich ziehe eine Karte vom Stapel mit 60%iger Wsk.
     * - Wenn ziehen nicht möglich steige aus.
     */
    public void playSchwierigkeitLeicht() {
        Spieler playing = Main.tisch.getSpielerList()[Main.tisch.aktiv];
        Karte card;
        Main.spiellogik.chipsTauschen(Main.tisch.aktiv);
        int ablage = Main.tisch.getObereKarteAblagestapel().value;
        System.out.println("ablage: "+ ablage);

        //Main.spiellogik.aussteigen(this);
        if (inGame()) {
            if (zug) {
                for (int i = 0; i < playing.getCardCount(); i++) {
                    card = playing.getCardHand().getKarte(i);
                    if (Main.spiellogik.karteLegen(playing, card)) {
                        System.out.println("\tLege " + card.getValue());
                        return;
                    }
                }
                //60%ige wahrscheinlichkeit eine Karte zu ziehen sonst aussteigen
                if (Main.spiellogik.karteNachziehen(playing) && Math.random() >= 0.4) {
                    System.out.println("\tZiehe");
                    return;
                } else {
                    System.out.println("\tSteige aus");
                    Main.spiellogik.aussteigen(playing);
                }
            }
        }
    }

    /**
     * Mittelschwere Bot Methode:
     * Bot führt Aktionen in folgender Reihenfolge aus:
     * - Chips umtauschen
     * - Karten legen wenn möglich (gleiche zuerst)
     * - Wenn keine Karten mehr auf der Hand wird größtmöglicher Chip abgeben
     * - Karte ziehen wenn möglich wenn mehr als 4 Karten auf der Hand oder das Spiel durch die Handsumme beendet werden würde
     * - sonst Zug beendet
     * <p>
     * Um Methode zu verwenden muss setZug(true) aufgerufen werden.
     * Methode setzt setZug(false) automatisch
     */
    public void playSchwierigkeitMittel(){
        Spieler playing = Main.tisch.getSpielerList()[Main.tisch.aktiv];
        Tisch tisch = Main.tisch;
        Spiellogik logik = Main.spiellogik;
        Main.spiellogik.chipsTauschen(Main.tisch.aktiv);
        int ablage = Main.tisch.getObereKarteAblagestapel().value;
        System.out.println("ablage: "+ ablage);

        boolean gleicheKarte = true;
        boolean abgelegt = false;
        boolean noChange = true;
        HandKarte legen = null;

        if (inGame()) {
            if (zug) {

                //for Schleife für das Karten legen
                for (int i = 0; i < playing.getCardCount(); i++) {
                    HandKarte karte = playing.cardHand.getKarte(i);

                    if (gleicheKarte && ablage == karte.value) {//gleicher Wert
                        System.out.println("\t lege gleich " + karte.getValue());
                        logik.karteLegen(playing, karte);
                        abgelegt = true;
                        noChange = false;
                        break;
                    }
                }

                for (int i = 0; i < playing.getCardCount() && noChange; i++) {
                    HandKarte karte = playing.cardHand.getKarte(i);

                    if ((tisch.getObereKarteAblagestapel().value == karte.value - 1
                            || (tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                            || (tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1))) {// Handkarte um eins größer
                        System.out.println("\t lege höher " + karte.getValue());
                        logik.karteLegen(playing, karte);
                        abgelegt = true;
                        break;
                    }
                }
                //Kartenlegen Ende//

                //Karten ziehen wenn noch keine Karte abgelegt wurde, wenn man am Zug ist, Wenn mehr als 4 Karten hat oder die Handsumme das Spiel beenden würde
                if (!abgelegt && (playing.getCardCount() > 4  || (Main.spielArt==0 && handSumme()+playing.getPoints()>=40) && handSumme() <= 5 )){
                    if (Main.spiellogik.karteNachziehen(playing)) {
                        System.out.println("\t Karte nachziehen");
                        return;
                    }else {
                        System.out.println("\tziehen nicht möglich");
                        Main.spiellogik.aussteigen(playing);
                        return;
                    }

                }else if(!abgelegt){
                    Main.spiellogik.aussteigen(playing);
                    System.out.println("\t steige aus");

                }
            }
        }
    }


    /**
     * Schwerer Bot
     * Versucht zuerst die nicht doppelte Karte abzulegen
     * Außnahme : Lama
     * Ansonsten:
     *  - schaut die Summe der Handkarten und wieviele Karten die Gegner noch haben an und zieht oder Steigt demendsprechend aus.
     *  - zusätzlich Chance auf Risikozug.
     *  - versucht immer zu ziehen falls Handsumme das Spiel beenden würde
     *  Wenn Nachziehstapel leer steige ich ebenfalls aus
     */
    public void playSchwierigkeitSchwer() {
        Spieler playing = Main.tisch.getSpielerList()[Main.tisch.aktiv];
        Main.spiellogik.chipsTauschen(Main.tisch.aktiv);
        int ablage = Main.tisch.getObereKarteAblagestapel().value;
        System.out.println("ablage: "+ ablage);

        boolean noChange=true;

        Karte legen = new Karte (0,true);
        Karte merken = new Karte (0,true);
        int risiko=0;
        int anzleg = 0;
        int anzmerk = 0;
        int gezogen=0;

        while(inGame()) {
            if(zug) {
                Map<HandKarte, Integer> karteAnz = doppelteKarten();

                if (!letzterSpieler()) {
                    for (Map.Entry<HandKarte, Integer> entry : karteAnz.entrySet()) {
                        if (entry.getKey().value == ablage && entry.getValue() < 2) {
                            legen = entry.getKey();
                            anzleg = entry.getValue();
                        } else if (entry.getKey().value == ablage) {
                            merken = entry.getKey();
                            anzmerk = entry.getValue();
                        }
                    }

                    //überschreibe falls größere möglich und nicht doppelt
                    for (Map.Entry<HandKarte, Integer> entry : karteAnz.entrySet()) {
                        if (ablage == 10) {
                            if (entry.getKey().value == 10 && entry.getValue() < 2) {
                                legen = entry.getKey();
                                anzleg = entry.getValue();
                            } else if (entry.getKey().value == 10) {
                                legen = entry.getKey();
                                anzleg = entry.getValue();
                            } else if (entry.getKey().value == 1) {
                                legen = entry.getKey();
                                anzleg = entry.getValue();
                            }
                        } else if (ablage == 6) {
                            if ((entry.getKey().value == 10) && entry.getValue() < 2) {
                                legen = entry.getKey();
                                anzleg = 0;
                            } else if (entry.getKey().value == 10 && entry.getValue() > 1) {
                                merken = entry.getKey();
                                anzmerk = entry.getValue();
                            } else if (entry.getKey().value == 6 && entry.getValue() < 2) {
                                legen = entry.getKey();
                                anzleg = entry.getValue();
                            }
                        } else if (entry.getKey().value == ablage + 1 && entry.getValue() < 2) {
                            legen = entry.getKey();
                            anzleg = entry.getValue();
                        } else if (entry.getKey().value == ablage + 1) {
                            merken = entry.getKey();
                            anzmerk = entry.getValue();
                        }
                    }
                    //Kartelegen
                    if (legen.value != 0 || merken.value != 0) {
                        if ((merken.value != 0) && (legen.value != 0)) {
                            if (legen.value == 10) {
                                System.out.println("\tlege " + legen.value);
                                Main.spiellogik.karteLegen(playing, legen);
                                noChange = false;
                                return;
                            } else if (merken.value == 10) {
                                System.out.println("\tlege " + merken.value);
                                Main.spiellogik.karteLegen(playing, merken);
                                noChange = false;
                                return;
                            } else if (anzleg < anzmerk) {
                                Main.spiellogik.karteLegen(playing, legen);
                                System.out.println("\tlege " + legen.value);
                                noChange = false;
                                return;
                            } else if (anzmerk < anzleg) {
                                Main.spiellogik.karteLegen(playing, merken);
                                System.out.println("\tlege " + merken.value);
                                noChange = false;
                                return;
                            }
                        } else if (legen.value != 0) {
                            System.out.println("\tlege " + legen.value);
                            Main.spiellogik.karteLegen(playing, legen);
                            noChange = false;
                            gezogen = 0;
                            return;
                        } else if (merken.value != 0) {
                            System.out.println("\tlege " + merken.value);
                            Main.spiellogik.karteLegen(playing, merken);
                            noChange = false;
                            return;
                        }
                        gezogen = 0;

                    }
                }else{
                    for (int i = 0; i < playing.getCardCount() && noChange; i++) {
                        HandKarte karte = playing.cardHand.getKarte(i);

                        if ((ablage == karte.value - 1
                                || (ablage== 6 && karte.value == 10) //Lama auf 6
                                || (ablage== 10 && karte.value == 1))) {// Handkarte um eins größer
                            System.out.println("\t lege nach Reihenfolge " + karte.getValue());
                            Main.spiellogik.karteLegen(playing, karte);
                            noChange=false;
                            break;
                        }
                    }
                }

                //wenn legen nicht möglich, ziehen oder aussteigen?

                // Immer nach ziehen fals Handsumme das Spielbeenden würde
                if (noChange && (Main.spielArt==0 && handSumme()+playing.getPoints()>=40)){

                    if (Main.spiellogik.karteNachziehen(playing)) {
                        System.out.println("ziehen");
                    }else {
                            System.out.println("\tziehen nicht möglich");
                            Main.spiellogik.aussteigen(playing);
                    }return;
                // wenn ein Spieler hat nur noch eine Karte oder Handsumme ist kleiner als 3 -> aussteigen
                } else if (noChange && playerCardCount()  == 1|| handSumme()<3) {

                    System.out.println("\tSteige sicherheitshalber aus");
                    Main.spiellogik.aussteigen(playing);
                    return;
                // Handsumme ist größer als 8 und kein Spieler hat weniger als 2 Karten, dann 60%ige Wsk. auf Risiko zug. (nur 2 mal in einer Runde möglich)
                } else if (noChange && (handSumme() > 8) && playerCardCount() <= 2 && (risiko == 0 || risiko == 1 && Math.random() <= 0.6) ) {

                    if (Main.spiellogik.karteNachziehen(playing)) {
                        risiko++;
                        System.out.println("Risiko ziehen");
                    }else {
                        System.out.println("\tSteige gezwungenermaßen aus");
                        Main.spiellogik.aussteigen(playing);
                    }
                    return;
                //wenn handsumme kleiner als 5 ist und min. ein spieler weniger als 2 Karten hat -> aussteigen
                } else if (noChange && (handSumme() < 5) && playerCardCount() < 2) {

                    System.out.println("\tSteige sicherheitshalber aus");
                    Main.spiellogik.aussteigen(playing);
                    return;
                //falls Handwert zu hoch und kein Mitspieler wenig Karten hat
                } else if (noChange) {
                    // falls nicht schon 3 mal hintereinander gezogen -> karte Nachziehen
                    if ((gezogen < 3) && Main.spiellogik.karteNachziehen(playing)) {
                        System.out.println("\tZiehe");
                        gezogen++;
                        return;
                    // bereits dreimal Hintereinander gezogen aber die Handsumme größer als 12 ist und kein spieler weniger als 3 Karten hat -> trotzdem ziehen
                    }else if (handSumme()>12 && playerCardCount()>2 && Main.spiellogik.karteNachziehen(playing)){
                        System.out.println("\tZiehe");
                        gezogen++;
                        return;

                    } else {//ziehen nicht möglich -> aussteigen
                        System.out.println("\tSteige gezwungenermaßen aus");
                        Main.spiellogik.aussteigen(playing);
                        return;
                    }
                }

            }
        }
    }
    /**
     * Lässt den Bot Chips ablegen, wenn er alle Karten abgelegt hat.
     * <p>
     * Die Methode ist eine Helper Methode für die playSchwierigkeit() Methoden.
     * Ähnelt der Methode aus der Spiellogik.
     */
    public void chipAbgeben() {

        Tisch tisch = Main.tisch;
        if (this.getBlackChips() > 0) {
            Main.spiellogik.chipAbgeben(this, new BlackChip());
            System.out.println(this.getName()+" gibt schwarzen Chip ab");
        } else if (this.getWhiteChips() > 0) {
            Main.spiellogik.chipAbgeben(this, new WhiteChip());
            System.out.println(this.getName()+" gibt weißen Chip ab");
        }

    }

    /**
     * Gibt dem Bot bescheid, dass sein Zug beginnen kann.
     * Eine playSchwierigkeit() Methode muss allerdings aufgerufen werden, damit der Bot beginnen kann.
     * <p>
     * Setzt den Parameter selbständig auf false, wenn er fertig ist.
     *
     * @param entryZug
     */
    public void setZug(boolean entryZug) {
        zug = entryZug;
    }

    public int playerCardCount(){
        int minHandKartCoundGegner = Integer.MAX_VALUE;
        for(Spieler sp: Main.tisch.getSpielerList()) {
            if(sp.inGame() && sp.getCardCount() < minHandKartCoundGegner) {
                minHandKartCoundGegner = sp.getCardCount();
            }
        }
        return minHandKartCoundGegner;
    }

    /**
     * @return Kartenwert den Bot auf der Hand hat
     */
    private int handSumme(){
        List<Karte> cards = new ArrayList();
        cards.addAll(this.getCardHand().getHandKarte());
        Collections.sort(cards);

        Set<Integer> cardvals = new HashSet<>();
        for(Karte c :cards) {
            cardvals.add(c.getValue());
        }
        int handsumme = 0;
        for(int val:cardvals) {
            handsumme += val;
        }

        return handsumme;
    }


    /**
     * @return Map mit gleichen Karten und deren Häufigkeit
     */
    private Map<HandKarte,Integer> doppelteKarten(){
        List<HandKarte> cards = new ArrayList();
        cards.addAll(this.getCardHand().getHandKarte());
        Collections.sort(cards);

        List<Integer> value = new ArrayList<>();

        for (HandKarte e : cards){
            value.add(e.getValue());
        }
        Collections.sort(value);

        Map<HandKarte, Integer> dublikate = new TreeMap<>();
        for(HandKarte e: cards){
            int count = 0;

            for (int i =0; i<value.size() ; i++) {
                if (e.getValue()== value.get(i)) {
                    count++;

                }

            }

            dublikate.put(e, count);

        }


        for(Map.Entry<HandKarte,Integer> e : dublikate.entrySet()){
            HandKarte k = e.getKey();

            //System.out.println("Karte:"+e.getKey().getValue()+" x"+e.getValue());
        }
        return dublikate;
    }

    /**
     * @return true wenn alle anderen Spieler aussgestiegen sind, false wenn noch spieler im Spiel sind
     *
     */
    boolean letzterSpieler(){
        int anzahlSpielerNichtFertig = 0;

        for (int i = 0; i < anzSpieler; i++) { //spielerListe durchgehen und gucken wie viele Spieler noch spielen
            if (Main.tisch.getSpielerList()[i].inGame()||!(Main.tisch.getSpielerList()[i].equals(Main.tisch.getSpielerList()[Main.tisch.aktiv]))) {  //spieler ist nicht ausgestiegen
                anzahlSpielerNichtFertig += 1;;
            }
        }
        if (anzahlSpielerNichtFertig==0){
            return true;
        }else{
            return false;
        }

    }

    /**
     * @return Integer der dem Bot level entspricht; 1: Einfach , 2: Mittel, 3: Schwer
     */
    public int getSchwierigkeit() {
        return schwierigkeit;
    }
}



