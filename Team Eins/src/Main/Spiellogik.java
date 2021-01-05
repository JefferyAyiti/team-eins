package Main;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

import static Main.Main.*;

/**
 * Spiellogik regelt die Runden des Spiels
 */
public class Spiellogik implements Serializable {

    public final Tisch tisch;
    public boolean rundeBeendet = false;
    public boolean spielBeendet = false;
    int win1 =0;
    int win2 =0;
    int win3 =0;
    Map<Integer,Integer> average = new LinkedHashMap<>();

    /**
     * Initialisiere Spiellogik mit dem Tisch
     *
     * @param tisch Das ist der Tisch, auf dem gespielt wird
     */
    public Spiellogik(Tisch tisch) {
        this.tisch = tisch;
    }


    /**
     * einSpielerUebrig überprüft die Regel,ob es einen einzigen Spieler am Ende gibt
     * der nochmal seine Karten ablegen darf.
     */
    private void einSpielerUebrig() {
        Spieler letzterSpieler = null;
        //uebruefen ob es einen einzigen Spieler gibt der noch Handkarten hat
        // -> dieser Spieler darf noch Karten ablegen
        int anzahlSpielerNichtFertig = 0;

        for (int i = 0; i < anzSpieler; i++) { //spielerListe durchgehen und gucken wie viele Spieler noch spielen
            if (Main.tisch.getSpielerList()[i].inGame()) {  //spieler ist nicht ausgestiegen
                anzahlSpielerNichtFertig += 1;
                letzterSpieler = Main.tisch.getSpielerList()[i];
            }

        }
        if (anzahlSpielerNichtFertig == 0) {
            rundeBeendet = true;
            rundeBeenden();
        } else if (anzahlSpielerNichtFertig == 1) {
            tisch.naechste();
            letzterSpieler.setLetzerSpielerDurchgang(true);//nur noch ein Spieler hat Handkarten
        } else { //gibt noch mehr als 1 Spieler die Handkarten haben
            if (tisch.getDurchgangNr() >= 1 && rundeBeendet == false) {

                tisch.naechste();
            }
            rundeBeendet = false;

        }


    }

    /**
     * Alle Spieler steigen aus dem Spiel aus
     */
    public void alleAussteigen() {
        for (int i = 0; i < anzSpieler; i++) { //jeder Spieler kassiert Chips
            Main.tisch.getSpielerList()[i].aussteigen();  //Spieler können wieder Züge machen

        }
    }

    /**
     * Ein Spieler legt eine Karte aus seiner Hand auf den Ablagestapel
     *
     * @param spieler Spieler der dran ist
     * @param karte   Karte die gelegt werden soll
     * @return boolean der anzeigt, ob der Zug erfolgreich war
     */
    public boolean karteLegen(Spieler spieler, Karte karte) {

        if (spieler.getName().equals(tisch.getAktivSpieler().getName())        //        && spieler.inGame()
        ) {
            try {
                Boolean karteAbgelegt = false;
                if (tisch.getObereKarteAblagestapel().value == karte.value //gleicher Wert
                        || tisch.getObereKarteAblagestapel().value == karte.value - 1   //Handkarte ist um eins größer als die oberste Ablagekarte
                        || (tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                        || (tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1)) {

                    //spieler.getCardHand().removeKarte((HandKarte) karte);
                    tisch.getAktivSpieler().getCardHand().removeKarte((HandKarte) karte);
                    tisch.karteAblegen(karte);

                    if (tisch.getAktivSpieler().cardHand.getHandKarte().size() == 0) {   //hat der Spieler noch Handkarten?
                        if (spieler instanceof Bot) {
                            ((Bot) spieler).chipAbgeben();
                        }
                        tisch.getAktivSpieler().setLetzerSpielerDurchgang(false);
                        karteAbgelegt = true;
                        tisch.getAktivSpieler().aussteigen();    // Spieler kann keinen Zug mehr machen
                        rundeBeendet = true;
                        rundeBeenden(); //ein Spieler hat keine Karten mehr oder der letzte Spieler ist fertig mit seinem Zug

                    }
                    if (!tisch.getAktivSpieler().isLetzerSpielerDurchgang() && !karteAbgelegt) {//Spieler darf noch seine Karten ablegen
                        tisch.naechste();
                    }

                    if (Main.server != null) {
                        try {
                            server.incAenderung();
                        } catch (RemoteException e) {
                        }
                    }
                    return true;


                } else {
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;

            }
        }
        return false;

    }


    /**
     * Ein Spieler zieht eine Karte vom Nachziehstapel,diese wird seiner Hand hinzugefügt
     *
     * @param spieler Spieler der dran ist
     * @return boolean der anzeigt, ob der Zug erfolgreich war
     */
    public boolean karteNachziehen(Spieler spieler) {
        if (!tisch.getAktivSpieler().isLetzerSpielerDurchgang() &&
                tisch.getAktivSpieler().getName().equals(spieler.getName())
                && tisch.getAktivSpieler().inGame()) {
            try {

                tisch.getAktivSpieler().getCardHand().addKarte(tisch.karteZiehen());
                tisch.naechste();
                if (Main.server != null) {
                    try {
                        server.incAenderung();
                    } catch (RemoteException e) {
                    }
                }
                System.out.println(spieler.getName() + " zieht nach");
                return true;

            } catch (Exception e) {
                return false;

            }
        } else {
            System.out.println("nachziehen falsch");
            return false;
        }
    }

    /**
     * Errechnet die Summe der verbliebenen Karten auf der Hand
     * und nimmt entsprechend Chips vom Tisch (möglichst schwarze zuerst)
     *
     * @param spieler
     */
    public void chipsKassieren(Spieler spieler) {
        int alt = spieler.getOldScore();
        Set<Integer> handkarten = new LinkedHashSet<>();


        for (Karte c : spieler.getCardHand().getHandKarte()) {
            handkarten.add(c.getValue());
        }

        int summe = 0;
        if (tisch.getAktivSpieler() == Main.tisch.getSpielerList()[0])
            System.out.println("HandKarten: "+ handkarten);
        //aufaddieren
        for (Integer c : handkarten) {
            summe += c;
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
        tisch.takeChips(weisseChips, 0);
        spieler.setWhiteChips(spieler.getWhiteChips() + weisseChips);

        int punktzahl = (spieler.getWhiteChips() * -1) + (spieler.getBlackChips() * -10);
        spieler.setPoints(punktzahl);

        if (Main.server != null) {
            try {
                server.incAenderung();
            } catch (RemoteException e) {
            }
        }
    }


    /**
     * Tauscht 10 weiße Chips gegen 1 schwarzen Chip aus.
     *
     * @param playerID
     * @return Wenn nicht genügend weiße Chips vorhanden sind und
     * Wenn nicht genügend schwarze Chips auf dem Tisch liegen: transaktion = false
     * sonst: transaktion = true
     */
    public boolean chipsTauschen(int playerID) {
        boolean transaktion = false;

        if (tisch.getSpielerList()[playerID].getWhiteChips() >= 10 && tisch.getBlackChips() > 0) {

            tisch.getSpielerList()[playerID].setBlackChips(tisch.getSpielerList()[playerID].getBlackChips() + 1);
            tisch.getSpielerList()[playerID].setWhiteChips(tisch.getSpielerList()[playerID].getWhiteChips() - 10);

            tisch.takeChips(-10, 1);

            transaktion = true;
        }
        if (Main.server != null) {
            try {
                server.incAenderung();
            } catch (RemoteException e) {
            }
        }
        return transaktion;
    }

    /**
     * gibt einen Chip an den Tisch zurück. Nur moglich wenn
     * der Spieler keine Karte im Hand hat
     *
     * @param spieler Spieler der die Aktiion ausführt
     * @param chip    Chip den der Spieler Abgeben möchte
     * @return true wenn Chips zum zurückgeben vorhanden sind ansonsten false
     */
    public boolean chipAbgeben(Spieler spieler, Chip chip) {
        boolean aktion = false;
        if (tisch.getAktivSpieler().getCardCount() == 0) {
            if (tisch.getAktivSpieler().getBlackChips() > 0 || tisch.getAktivSpieler().getWhiteChips() > 0) {
                if (chip.getValue() == -1) {
                    tisch.getAktivSpieler().setWhiteChips(spieler.getWhiteChips() - 1);
                    tisch.takeChips(1, 0);
                    tisch.getAktivSpieler().setPoints(spieler.getPoints() + 1);

                } else {
                    tisch.getAktivSpieler().setBlackChips(spieler.getBlackChips() - 1);
                    tisch.takeChips(0, 1);
                    tisch.getAktivSpieler().setPoints(spieler.getPoints() + 10);

                }
                aktion = true;
                if (Main.server != null) {
                    try {
                        server.incAenderung();
                    } catch (RemoteException e) {
                    }
                }
            }
        }
        return aktion;


    }

    /**
     * @param spieler Beim Aussteigen wird keine neue Zuege erkannt
     */
    public void aussteigen(Spieler spieler) {

        if (tisch.getAktivSpieler().getName().equals(spieler.getName()) && spieler.inGame()) {
            tisch.getAktivSpieler().aussteigen();
            tisch.getAktivSpieler().setLetzerSpielerDurchgang(false);
            einSpielerUebrig();  //ueberpruefen wie viele Spieler diese Runde noch spielen
            if (Main.server != null) {
                try {
                    server.incAenderung();
                } catch (RemoteException e) {
                }
            }

        }

        /*spieler.getCardHand().setValueSum();
        int punkte = spieler.getCardHand().getValueSum();
        spieler.setPoints(punkte);*/
    }

    /**
     * erstellt eine Rangliste der Spieler
     *
     * @return sortierte LinkedHashMap mit Spielern und deren Punktestand
     */
    public Map<Spieler, Integer> ranglisteErstellen() {

        //Map mit spielern + Spielstand erstellen
        Map<Spieler, Integer> punktestand = new HashMap<Spieler, Integer>();
        for (Spieler s : tisch.getSpielerList()) {
            punktestand.put(s, s.getPoints());
        }

        //sortieren
        List<Map.Entry<Spieler, Integer>> spielstand = new LinkedList<Map.Entry<Spieler, Integer>>(punktestand.entrySet());
        Collections.sort(spielstand, new Comparator<Map.Entry<Spieler, Integer>>() {
            @Override
            public int compare(Map.Entry<Spieler, Integer> o1, Map.Entry<Spieler, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        //sortierete Map
        Map<Spieler, Integer> rangliste = new LinkedHashMap<Spieler, Integer>();
        for (Map.Entry<Spieler, Integer> aa : spielstand) {
            rangliste.put(aa.getKey(), aa.getValue());
        }

        return rangliste;
    }


    /**
     * rundeBeenden regelt das Ende einer Runde. Zusätzlich zu dem Abkassieren der Chips, wird überprüft, ob das Spiel zu Ende ist
     * oder eine neue Runde gestartet werden muss.
     */
    private void rundeBeenden() {

        for (int i = 0; i < anzSpieler; i++) { //jeder Spieler kassiert Chips
            chipsKassieren(Main.tisch.getSpielerList()[i]);
            //System.out.println("setOldScore(rundeBeenden):  "+ spielerListe[i].getName()+" - old: "+ spielerListe[i].getOldScore()+ " neu: "+ spielerListe[i].getPoints());
            Main.tisch.getSpielerList()[i].einsteigen();  //Spieler können wieder Züge machen

        }
        if (tisch.getWhiteChips() <= 0) {
            while (tisch.getWhiteChips() <= 0) {
                Map<Spieler, Integer> rangliste = ranglisteErstellenNurWeißeChips();
                Integer weißeChips = (new ArrayList<>(rangliste.values()).get(0));
                Spieler spieler = (new ArrayList<>(rangliste.keySet()).get(0));
                if (weißeChips > 10) {
                    spieler.setWhiteChips(spieler.whiteChips - 10);
                    spieler.setBlackChips(spieler.getBlackChips() + 1);
                    spieler.chipTausch();
                    tisch.takeChips(-10, 1);
                }

            }

        }

        if (spielArt == 0) { //normales Spiel
            for (int i = 0; i < anzSpieler; i++) { //spielerListe durchgehen

                if ((Main.tisch.getSpielerList()[i].getBlackChips() * 10 +
                        Main.tisch.getSpielerList()[i].getWhiteChips()) >= 40) {
                    alleAussteigen();
                    //ein Spieler hat -40 Punkte -> Spiel ist zu Ende
                    spielBeendet = true;

                    return;
                }
            }
        } else if(spielArt == 1 &&  //Rundenlimitiertes Spiel
                spielArtLimit <= tisch.getDurchgangNr()) {
            alleAussteigen();
            spielBeendet = true;
        }

        System.out.println("Runde beendet");
        rundeBeendet = true;
        return;
    }

    /**
     * erstellt eine Rangliste der Spieler, wobei nur Weiße Chips betrachtet werden
     *
     * @return sortierte LinkedHashMap mit Spielern und deren Anzahl an Weißen Chips
     */
    public Map<Spieler, Integer> ranglisteErstellenNurWeißeChips() {

        //Map mit spielern + Spielstand erstellen
        Map<Spieler, Integer> punktestand = new HashMap<Spieler, Integer>();
        for (Spieler s : tisch.getSpielerList()) {
            punktestand.put(s, s.getWhiteChips());
        }

        //sortieren
        List<Map.Entry<Spieler, Integer>> spielstand = new LinkedList<Map.Entry<Spieler, Integer>>(punktestand.entrySet());
        Collections.sort(spielstand, new Comparator<Map.Entry<Spieler, Integer>>() {
            @Override
            public int compare(Map.Entry<Spieler, Integer> o1, Map.Entry<Spieler, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });

        //sortierete Map
        Map<Spieler, Integer> rangliste = new LinkedHashMap<Spieler, Integer>();
        for (Map.Entry<Spieler, Integer> aa : spielstand) {
            rangliste.put(aa.getKey(), aa.getValue());
        }

        return rangliste;
    }


    /**
     * Initiiert eine neue Runde, d.h. Stapel mischen und neue Karten verteilen
     */
    public void initNeueRunde() {

        for (int i = 0; i < anzSpieler; i++) {
            Main.haende[i] = new Hand();
            tisch.spielerList[i].setCardHand(Main.haende[i]);
            tisch.spielerList[i].setOldScore(tisch.spielerList[i].getPoints());
        }

        tisch.initNachziehstapel();
        tisch.mischenNachziehstapel();


        //gebe jeden Spieler (anzSpieler) 6 Karten in Reihenfolge
        for (int i = 0; i < 6; i++) {

            for (int s = 0; s < anzSpieler; s++) {
                Main.haende[s].addKarte(tisch.karteZiehen());
            }

        }

        tisch.nextDurchgang();
        tisch.karteAblegen(tisch.karteZiehen()); //Ablagestapel
        einSpielerUebrig();
        return;

    }

    /**
     * getter-Methode für rundeBeendet
     *
     * @return rundeBeendet. Zeigt an, ob runde beendet wurde.
     */
    public boolean getRundeBeendet() {
        return rundeBeendet;
    }

    /**
     * gibt aus welche Botswierigkeit bisher wie oft im Spiel den ersten Platz erreicht hat
     */
    public void durchschnitt() {
        if (tisch.getDurchgangNr() != 0) {


            for (Map.Entry<Spieler, Integer> aa : ranglisteErstellen().entrySet()) {
                if (aa.getKey().getSchwierigkeit() == 1) {
                    win1++;
                    average.put(1, win1);
                    break;
                } else if (aa.getKey().getSchwierigkeit() == 2) {
                    win2++;
                    average.put(2, win2);
                    break;
                } else if (aa.getKey().getSchwierigkeit() == 3) {
                    win3++;
                    average.put(3, win3);
                    break;
                }
                break;
            }

            for (Map.Entry<Integer, Integer> entry : average.entrySet()) {
                System.out.println("Botschwierigkeit " + entry.getKey() + " hat " + entry.getValue() + "/" + tisch.getDurchgangNr() + " Runden gewonnen");
            }
        }
    }
}

