import java.sql.SQLOutput;
import java.util.*;

/**
 * Spiellogik regelt die Runden des Spiels
 */
public class Spiellogik {
    private final Stack<Spieler> letzteSpieler = new Stack();
    public final Tisch tisch;
    private final Spieler[] spielerListe;


    /** Initialisiere Spiellogik mit dem Tisch
     * @param tisch Das ist der Tisch, auf dem gespielt wird
     */
    public Spiellogik(Tisch tisch) {
        this.tisch = tisch;
        this.spielerListe = tisch.getSpielerList();
    }


    /**
     * einSpielerUebrig überprüft die Regel,ob es einen einzigen Spieler am Ende gibt
     * der nochmal seine Karten ablegen darf.
     */
    private void einSpielerUebrig(){

        int len = spielerListe.length;
        Spieler letzterSpieler = null;
        //uebruefen ob es einen einzigen Spieler gibt der noch Handkarten hat
        // -> dieser Spieler darf noch Karten ablegen
        int anzahlSpielerNichtFertig = 0;

        for (int i = 0; i < len; i++) { //spielerListe durchgehen und gucken wie viele Spieler noch spielen
            if (spielerListe[i].inGame()) {  //spieler ist ausgestiegen
                anzahlSpielerNichtFertig += 1;
                letzterSpieler = spielerListe[i];
            }

        }

        if (anzahlSpielerNichtFertig == 1) {
            letzterSpieler.setLetzerSpielerDurchgang(true);//nur noch ein Spieler hat Handkarten
        }
        else { //gibt noch mehr als 1 Spieler die Handkarten haben
            if (tisch.getDurchgangNr() >= 2){
                tisch.naechste();
            }

        }


    }
    /** rundeBeenden regelt das Ende einer Runde. Zusätzlich zu dem Abkassieren der Chips, wird überprüft, ob das Spiel zu Ende ist
     * oder eine neue Runde gestartet werden muss.
     * @throws Exception
     */
    private void rundeBeenden() throws Exception {
        int len = spielerListe.length;
        for (int i = 0; i < len; i++) { //jeder Spieler kassiert Chips
        chipsKassieren(spielerListe[i]);
        spielerListe[i].einsteigen();  //Spieler können wieder Züge machen

    }
        for (int i = 0; i < len; i++) { //spielerListe durchgehen
            if (spielerListe[i].points == -40) {
                System.out.println(ranglisteErstellen());  //ein Spieler hat -40 Punkte -> Spiel ist zu Ende
                return;
            }
        }

        initNeueRunde();
        return ;
    }




    /**
     * Ein Spieler legt eine Karte aus seiner Hand auf den Ablagestapel
     *
     * @param spieler Spieler der dran ist
     * @param karte Karte die gelegt werden soll
     * @return boolean der anzeigt, ob der Zug erfolgreich war
     */
    public boolean karteLegen(Spieler spieler, Karte karte) {
        if(tisch.getAktivSpieler() == spieler && spieler.inGame()){
            try {
                if (tisch.getObereKarteAblagestapel().value == karte.value //gleicher Wert
                    || tisch.getObereKarteAblagestapel().value == karte.value - 1   //Handkarte ist um eins größer als die oberste Ablagekarte
                    || (tisch.getObereKarteAblagestapel().value == 6 && karte.value == 10) //Lama auf 6
                    || (tisch.getObereKarteAblagestapel().value == 10 && karte.value == 1)){

                    spieler.getCardHand().removeKarte((HandKarte) karte);
                    tisch.karteAblegen(karte);
                    if(spieler.cardHand.getHandKarte().size()== 0){   //hat der Spieler noch Handkarten?
                        spieler.setLetzerSpielerDurchgang(false);
                        spieler.aussteigen();    // Spieler kann keinen Zug mehr machen
                        rundeBeenden();    //ein Spieler hat keine Karten mehr oder der letzte Spieler ist fertig mit seinem Zug

                    }
                    if(!spieler.isLetzerSpielerDurchgang()){  //Spieler darf noch seine Karten ablegen
                        tisch.naechste();
                    }
                    return true;



            }   else {
                return false;
            }

        } catch (Exception e) {
                e.printStackTrace();
                return false;

        }}

         return false;


    }


    /**
     * Ein Spieler zieht eine Karte vom Nachziehstapel,diese wird seiner Hand hinzugefügt
     * @param spieler Spieler der dran ist
     * @return boolean der anzeigt, ob der Zug erfolgreich war
     */
    public boolean karteNachziehen(Spieler spieler){
        if(!spieler.isLetzerSpielerDurchgang() && tisch.getAktivSpieler() == spieler){
            try {

                 spieler.getCardHand().addKarte(tisch.karteZiehen());
                 tisch.naechste();
                 return false;

            } catch (Exception e) {
                return false;

            }}
        else {
            return false;
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
        if(tisch.getAktivSpieler() == spieler){
            spieler.aussteigen();
            einSpielerUebrig();
        }

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

        einSpielerUebrig();
        return;

        //TODO Alle ausgestiegene Spieler einsteigen lassen(geklärt oben)
    }

}

