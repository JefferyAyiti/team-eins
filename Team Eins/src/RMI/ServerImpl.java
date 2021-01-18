package RMI;

import Main.*;
import javafx.scene.control.Label;
import org.apache.xpath.operations.Bool;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static Main.Main.*;

public class ServerImpl implements server {


    Map<String, String> clients = new LinkedHashMap<>();

    static ArrayList<List<String>> chatrecord = new ArrayList<>();

    Map<String, Long> pings = new LinkedHashMap<>();
    int anzClients = 0;
    long aenderung = 0;
    String host;
    boolean lock = true;
    int ammountReadyClients = 0;
    Map<String,Boolean> readyClients = new HashMap<>();

    public ServerImpl() throws RemoteException {

        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public boolean serverOpen() throws RemoteException {
        return lock;
    }

    @Override
    public int getAnzClients() throws RemoteException {
        return anzClients;
    }

    @Override
    public int getAnzReadyClients() throws RemoteException {
        int readyClientsInt = 0;
        for(Boolean ready: readyClients.values()){
            if(ready){
                readyClientsInt++;
            }
        }

        return readyClientsInt;
    }

    @Override
    public void addClient(String uid, String name) {
        if(!gameRunning) {
            int max = 0;
            try {
                max = getAnzahlSpieler();
            } catch (RemoteException e) {
            }
            if (anzClients < max) {
                anzClients++;
                aenderung++;
                clients.put(uid, name);
                readyClients.put(uid,false);
                pings.put(uid, System.currentTimeMillis());
            } else {
                lock = false;
                System.out.println("Maximal Anzahl von Spieler erreicht. Keine Zugang");
            }
        }
    }

    @Override
    public void leaveServer(String client) throws RemoteException {
        aenderung++;
        anzClients--;
        clients.remove(client);
        readyClients.remove(client);
    }

    @Override
    public Map<String, String> getClients() {
        return clients;
    }

    @Override
    public int assignId(String uid) {
        for (int i = 0;i < anzSpieler;i++) {
            if(Main.tisch.getSpielerList()[i].getUid().equals(uid)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public boolean getGameStart(String uid) throws RemoteException {
        pings.put(uid, System.currentTimeMillis());
        return gameRunning;
    }

    @Override
    public long getAenderung(String uid) throws RemoteException {
        pings.put(uid, System.currentTimeMillis());
        return aenderung;
    }

    @Override
    public void incAenderung() throws RemoteException {
        aenderung++;
    }

    @Override
    public int getAnzahlSpieler() throws RemoteException {
        return anzSpieler;
    }

    @Override
    public void neueRunde(boolean countUp,String uid) throws RemoteException {
        if(countUp){
            ammountReadyClients++;
            readyClients.remove(uid);
            readyClients.put(uid,true);
        }
        if(getAnzReadyClients() >= anzClients){
            spiellogik.initNeueRunde();
            ammountReadyClients = 0;
            for(String id: server.getClients().keySet()){
                readyClients.put(uid,false);
            }
            System.out.println(readyClients.size());

        }
        aenderung++;
    }

    @Override
    public void checkForNewRound() throws RemoteException {
        if(getAnzReadyClients() >= anzClients){
            neueRunde(false,uniqueID);
        }
    }

    @Override
    public Map<Spieler, Integer> getRangliste() throws RemoteException {
        return spiellogik.ranglisteErstellen();

    }
    @Override
    public void setHost(String uid) throws RemoteException{
        host = uid;
    }
    @Override
    public String getHost() throws RemoteException{
        return host;
    }
    @Override
    public int getBotLevel() throws  RemoteException{
        return botlevel;
    }

    @Override
    public void closeServer() throws RemoteException {
        lock = false;
    }


    //Spielzüge

    @Override
    public void karteLegen(Spieler spieler, Karte karte) throws RemoteException {
        spiellogik.karteLegen(spieler, karte);
        aenderung++;
    }

    @Override
    public void karteNachziehen(Spieler spieler) throws RemoteException {
        spiellogik.karteNachziehen(spieler);
        aenderung++;
    }

    @Override
    public void chipAbgeben(Spieler spieler, Chip chip) throws RemoteException {
        spiellogik.chipAbgeben(spieler,chip);
        aenderung++;

    }

    @Override
    public void chipsTauschen(int playerId) throws RemoteException {
        spiellogik.chipsTauschen(playerId);
        aenderung++;
    }

    @Override
    public void aussteigen(Spieler spieler) throws RemoteException {
        spiellogik.aussteigen(spieler);
        aenderung++;
    }

    @Override
    public boolean getRundeBeendet() throws RemoteException {
        return spiellogik.getRundeBeendet();
    }

    @Override
    public boolean getSpielBeendet() throws RemoteException {
        return spiellogik.spielBeendet;

    }


    @Override
    public Tisch updateTisch() throws RemoteException {
        return tisch;
    }

    @Override
    public void checkTimeout() throws RemoteException {
        for(Map.Entry<String, Long> ping : pings.entrySet()) {
            if(!ping.getKey().equals(uniqueID) &&
                    ping.getValue()+5000 < System.currentTimeMillis() && gameRunning) {
                replaceSpielerDurchBot(ping.getKey());
            }
        }
    }


    public void replaceSpielerDurchBot(String uid) throws RemoteException {
        Spieler s;
        for(int i = 0;i < anzSpieler;i++) {
            s = tisch.getSpielerList()[i];
            if(s.getUid().equals(uid) && !(s instanceof Bot) && !s.getLeftServer()) {
                System.out.println("Spieler " + s.getName() + " wird durch Bot ersetzt");
                leaveServer(uid);
                s.setLeftServer(true);
                Bot spiel = new Bot("[Bot] "+
                        tisch.getSpielerList()[i].getName(),
                        botlevel == 0 ? (int) (Math.random() * 3 + 1) : botlevel);
                spiel.setBlackChips(s.getBlackChips());
                spiel.setWhiteChips(s.getWhiteChips());
                spiel.setCardHand(s.getCardHand());
                spiel.setOldScore(s.getOldScore());
                spiel.setPoints(s.getPoints());
                tisch.spielerList[i] = spiel;
                aenderung++;

            }
        }
    }

    @Override
    public ArrayList<List<String>> getChat() throws RemoteException {
        return chatrecord;
    }

    @Override
    public void sendMessage(String message, String uid) throws RemoteException {
        List<String> zeile = new LinkedList<>();

        zeile.add(clients.get(uid));

        if(!message.equals("")) {
            if(message.equals("/coinflip")) {

                zeile.add(message);
                zeile.add(Math.random() >= 0.5?"Kopf":"Zahl");
            } else
                if(message.length() > 6 && message.substring(0,6).equals("/roll ")) {
                    int die = Integer.valueOf(message.substring(6));

                    zeile.add(Integer.toString(die));
                    zeile.add(Integer.toString((int)((Math.random() * die) + 1)));

                } else
                {
                    zeile.add(message);
                }
            chatrecord.add(zeile);
        }
    aenderung++;
    }

    /** getter-Methode für main
     * @return main
     */
    public Main getMain(){
        return getMain();
    }

    @Override
    public void changeName(String uid, String name) throws RemoteException {
        clients.replace(uid, name);
    }


    @Override
    public int getDurchgangNr() throws RemoteException {
        return tisch.getDurchgangNr();
    }

    @Override
    public Spieler getSpieler(String name) throws RemoteException {
        Spieler sol = null;
        for (int i = 0;i < anzSpieler;i++) {
            if(Main.tisch.getSpielerList()[i].getName().equals(name)) {
                sol =  tisch.getSpielerList()[i];
            }
        }
        return sol;
    }

}