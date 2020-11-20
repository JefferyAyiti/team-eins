import java.util.*;

public class Spiellogik {
    private Stack<Spieler> letzteSpieler = new Stack();
    private Tisch tisch;
    private Spieler[] spielerListe;

    public Spiellogik(Tisch tisch) {
        this.tisch = tisch;
        spielerListe = tisch.getSpielerList();
    }

    public void durchgang() {

    }

    /**
     * Ein Spieler legt eine Karte aus seiner Hand auf den Ablagestapel
     *
     * @param spieler
     * @param karte
     */
    public void karteLegen(Spieler spieler, Karte karte) {
        try {
           /* if (tisch.getObereKarteAblagestapel().value == karte.value //gleicher Wert
                    || tisch.getObereKarteAblagestapel().value == karte.value - 1   //Handkarte ist um eins größer als die oberste Ablagekarte
                    || (tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                    || (tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1)  //1 auf Lama
            ) {*/
            if(spieler.inGame()){ //Unerfolgereich wenn aussgestiegen
                spieler.getCardHand().removeKarte((HandKarte) karte);
                tisch.karteAblegen(karte);
            }

           /* } else {
                throw new Exception();
            }
*/
        } catch (Exception e) {
        }
    }


    /**
     * Ein Spieler zieht eine KArte vom Nachziehstapel,
     * diese wird seiner Hand hinzugefügt
     *
     * @param spieler
     */
    public void karteNachziehen(Spieler spieler) {
        try {
            //TODO
            // regelüberprüfung
            if(spieler.inGame()){ //Unerfolgereich wenn Spieler aussgestiegen ist
                spieler.getCardHand().addKarte(tisch.karteZiehen());
            }

        } catch (Exception e) {
        }
    }

    /**
     * Errechnet die Summe der verbliebenen Karten auf der Hand
     * und nimmt entsprechend Chips vom Tisch (möglichst schwarze zuerst)
     * @param spieler
     */
    public void chipsKassieren(Spieler spieler) {
        Set<Karte> handkarten = new LinkedHashSet<>();
        //Karten als Menge damit nur einmalig gezählt wird
        for (Karte c : spieler.getCardHand().getHandKarte()) {
            handkarten.add(c);
        }

        int summe = 0;
        //aufaddieren
        for (Karte c : handkarten) {
            summe += c.getValue();
        }


        int schwarzeChips = summe / 10;
        int weisseChips = summe % 10;

        //versuche zuerst schwarze chips zu nehmen

        while (schwarzeChips > 0 && tisch.getBlackChips() > 0) {
            tisch.takeChips(0, 1);
            spieler.setBlackChips(spieler.getBlackChips() + 1);
            schwarzeChips--;
        }

        weisseChips += schwarzeChips * 10;
        tisch.takeChips(weisseChips,0);
        spieler.setWhiteChips(spieler.getWhiteChips()+weisseChips);


    }

    /**
     * Tauscht 10 weiße Chips gegen 1 schwarzen Chip aus.
     * @param spieler
     * @return Wenn nicht genügend weiße Chips vorhanden sind und
     * Wenn nicht genügend schwarze Chips auf dem Tisch liegen: transaktion = false
     * sonst: transaktion = true
     */
    public boolean chipsTauschen(Spieler spieler) {

        boolean transaktion = false;

        if(spieler.getWhiteChips() >= 10 || tisch.getBlackChips() != 0){

            spieler.setBlackChips(spieler.getBlackChips() + 1);
            spieler.setWhiteChips(spieler.getWhiteChips() - 10);

            tisch.takeChips(-10, 1);

            transaktion = true;
        }
        return transaktion;
    }

    /**
     * gibt einen Chip an den Tisch zurück
     * @param spieler Spieler der die Aktiion ausführt
     * @param chip Chip den der Spieler Abgeben möchte
     * @return true wenn Chips zum zurückgeben vorhanden sind ansonsten false
     */
    public boolean chipAbgeben(Spieler spieler, Chip chip) {
        boolean aktion=false;
        if(spieler.getBlackChips() >0 || spieler.getWhiteChips() >0) {
            if (chip.getValue() == -1) {
                spieler.setWhiteChips(spieler.getWhiteChips() + 1);
                tisch.takeChips(-1, 0);
            } else {
                spieler.setBlackChips(spieler.getBlackChips() + 1);
                tisch.takeChips(0, -1);
            }
            aktion=true;
        }
        return aktion;
    }

    /**
     * @param spieler
     * Beim Aussteigen wird keine neue Zuege erkannt
     */
    public void aussteigen(Spieler spieler) {
        spieler.aussteigen();
        /*spieler.getCardHand().setValueSum();
        int punkte = spieler.getCardHand().getValueSum();
        spieler.setPoints(punkte);*/
    }

    Map<Spieler, Integer> ranglisteErstellen() {
        return new HashMap<>();
    }

    /**
     * Initiiert eine neue Runde, d.h. Stapel mischen und neue Karten verteilen
     *
     * @throws Exception
     */
    public void initNeueRunde() throws Exception {
        for (int i = 0; i < Main.spieler.length; i++) {
            Main.haende[i] = new Hand();
            Main.spieler[i].setCardHand(Main.haende[i]);

        }

        tisch.initNachziehstapel();
        tisch.mischenNachziehstapel();

        //gebe jeden Spieler (anzSpieler) 6 Karten in Reihenfolge
        for (int i = 0; i < 6; i++) {
            for (int s = 0; s < Main.spieler.length; s++) {
                Main.haende[s].addKarte(tisch.karteZiehen());
            }

        }

        tisch.karteAblegen(tisch.karteZiehen()); //Ablagestapel
        tisch.nextDurchgang();
        //TODO Alle ausgestiegene Spieler einsteigen lassen
    }

}

