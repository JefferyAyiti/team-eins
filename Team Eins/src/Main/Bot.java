package Main;

import java.io.Serializable;
import java.util.*;

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
     * - Wenn ablegen nicht möglich ziehe eine Karte vom Stapel
     * - Wenn ziehen nicht möglich steige aus.
     */
    public void playSchwierigkeitLeicht() {
        Spieler playing = Main.tisch.getSpielerList()[Main.tisch.aktiv];
        Karte card;
        Main.spiellogik.chipsTauschen(Main.tisch.aktiv);

        //Main.spiellogik.aussteigen(this);

        for (int i = 0; i < playing.getCardCount(); i++) {
            card = playing.getCardHand().getKarte(i);
            if (Main.spiellogik.karteLegen(playing, card)) {
                System.out.println("\tLege " + card.getValue());
                return;
            }
        }
        if (Main.spiellogik.karteNachziehen(playing)) {
            System.out.println("\tZiehe");
            return;
        } else {
            System.out.println("\tSteige aus");
            Main.spiellogik.aussteigen(playing);
        }

    }

    /**
     * Mittelschwere Bot Methode:
     * Bot führt Aktionen in folgender Reihenfolge aus:
     * - Chips umtauschen
     * - Karten legen wenn möglich
     * - Wenn keine Karten vorhanden größtmöglichen Chip abgeben
     * - Karte ziehen wenn möglich (mit einer wskeit)
     * - sonst Zug beendet
     * <p>
     * Um Methode zu verwenden muss setZug(true) aufgerufen werden.
     * Methode setzt setZug(false) automatisch
     */
    public void playSchwierigkeitMittel(){
        Spieler playing = Main.tisch.getSpielerList()[Main.tisch.aktiv];
        Tisch tisch = Main.tisch;
        Spiellogik logik = Main.spiellogik;
        int ablage = Main.tisch.getObereKarteAblagestapel().value;

        boolean gleicheKarte = true;
        boolean abgelegt = false;
        boolean noChange = true;
        HandKarte legen = null;

        if (inGame()) {
            if (zug) {
                //Chips umtauschen
                while (playing.whiteChips >= 10) {
                    logik.chipsTauschen(Main.tisch.aktiv);
                    System.out.println("\t tausche");
                }

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

                //Karten ziehen wenn noch keine Karte abgelegt wurde, wenn man am Zug ist, Wenn man mehr als 3 Karten hat,
                //und 20% wskeit kommt dazu. Sonst wird ausgestiegen
                if (!abgelegt && (playing.getCardCount() >= 4 && Math.random() <= 0.2)  ){
                    Main.spiellogik.karteNachziehen(playing);
                    System.out.println("\t Karte nachziehen");
                }else if(!abgelegt){
                    Main.spiellogik.aussteigen(playing);
                    System.out.println("\t steige aus");
                }
            }
        }
    }


    /**
     * Schwerer Bot
     * Versucht zuerst die höchsten Werte abzulegen
     * ANsonsten schaue die Summe meiner Handkarten und wieviele Karten
     *      die Gegner noch haben
     *      - Wenn ich höchsten 8 Strafpunkte bekommen würde und min einer meiner Gegner
     *          höchstens noch 2 Karten hat steige ich aus
     *  Ansonsten ziehe ich
     *  Wenn Nachziehstapel leer steige ich ebenfalls aus
     */
    public void playSchwierigkeitSchwer() {
        Spieler playing = Main.tisch.getSpielerList()[Main.tisch.aktiv];
        Karte card;
        Main.spiellogik.chipsTauschen(Main.tisch.aktiv);
        int ablage = Main.tisch.getObereKarteAblagestapel().value;
        System.out.println("ablage: "+ ablage);

        boolean noChange=true;
        boolean ablegen = false;
        boolean gleich= false;
        Karte legen = new Karte (0,true);
        Karte merken = new Karte (0,true);
        int risiko=0;
        int anzleg = 0;
        int anzmerk = 0;
        int gezogen=0;

        while(inGame()) {
            if(zug) {
                Map<HandKarte, Integer> karteAnz = doppelteKarten();


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
                        if (entry.getKey().value==10 && entry.getValue()<2) {
                            legen = entry.getKey();
                            anzleg = entry.getValue();
                        }else if( entry.getKey().value == 10){
                            legen = entry.getKey();
                            anzleg = entry.getValue();
                        } else if (entry.getKey().value == 1) {
                            legen = entry.getKey();
                            anzleg = entry.getValue();
                        }
                    } else if (ablage == 6) {
                        if ((entry.getKey().value == 10) && entry.getValue() < 2) {
                            legen = entry.getKey();
                            anzleg=0;
                        } else if (entry.getKey().value == 10 && entry.getValue()>1) {
                            merken = entry.getKey();
                            anzmerk = entry.getValue();
                        }else if ( entry.getKey().value==6 && entry.getValue()<2){
                            legen = entry.getKey();
                            anzleg=entry.getValue();
                        }
                    } else if (entry.getKey().value == ablage + 1 && entry.getValue() < 2) {
                        legen = entry.getKey();
                        anzleg = entry.getValue();
                    } else if (entry.getKey().value == ablage + 1) {
                        merken = entry.getKey();
                        anzmerk = entry.getValue();
                    }
                }

                if (legen.value != 0 || merken.value != 0) {
                    if ((merken.value != 0) && (legen.value != 0)) {
                        if (legen.value==10){
                            System.out.println("\tlege " + legen.value);
                            Main.spiellogik.karteLegen(playing, legen);
                            noChange = false;
                            return;
                        }else if ( merken.value == 10){
                            System.out.println("\tlege " + merken.value);
                            Main.spiellogik.karteLegen(playing, merken);
                            noChange = false;
                            return;
                        }else if (anzleg < anzmerk) {
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
                        gezogen=0;
                        return;
                    } else if (merken.value != 0) {
                        System.out.println("\tlege " + merken.value);
                        Main.spiellogik.karteLegen(playing, merken);
                        noChange = false;
                        return;
                    }
                    gezogen=0;

                }

                //wenn legen nicht möglich, ziehen oder aussteigen?

                if (noChange && playerCardCount()  == 1|| handSumme()<3) {
                    System.out.println("\tSteige sicherheitshalber aus");
                    Main.spiellogik.aussteigen(playing);
                    return;
                } else if (noChange && (handSumme() > 8) && playerCardCount() <= 2 && (risiko == 0 || risiko == 1 && Math.random() <= 0.6) ) {
                    if (Main.spiellogik.karteNachziehen(playing)) {
                        risiko++;
                        System.out.println("Risiko ziehen");
                    }else {
                        System.out.println("\tSteige gezwungenermaßen aus");
                        Main.spiellogik.aussteigen(playing);
                    }
                    return;
                } else if (noChange && (handSumme() < 5) && playerCardCount() < 2) {
                    System.out.println("\tSteige sicherheitshalber aus");
                    Main.spiellogik.aussteigen(playing);
                    return;
                } else if (noChange) {
                    if ((gezogen < 3) && Main.spiellogik.karteNachziehen(playing)) {
                        System.out.println("\tZiehe");
                        gezogen++;
                        return;
                    }else if (handSumme()>12 && playerCardCount()>2 && Main.spiellogik.karteNachziehen(playing)){
                        System.out.println("\tZiehe");
                        gezogen++;
                        return;
                    } else {//ziehen nicht möglich -> aussteigen
                        System.out.println("\tSteige gezwungenermaßen aus");
                        Main.spiellogik.aussteigen(playing);
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

            System.out.println("Karte:"+e.getKey().getValue()+" x"+e.getValue());
        }
        return dublikate;
    }
}



