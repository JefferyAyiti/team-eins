package RMI;

import Main.*;
import com.sun.jdi.connect.spi.TransportService;
import javafx.application.Platform;
import javafx.scene.control.Label;
//import org.apache.xpath.operations.Bool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import static Main.Main.*;

public class ServerImpl implements server {


    Map<String, String> clients = new LinkedHashMap<>();

    static ArrayList<List<String>> chatrecord = new ArrayList<>();
    static ArrayList<List<String>> censoredChatrecord = new ArrayList<>();

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
    public void addClient(String uid, String name) throws RemoteException {
        if(!gameRunning && !isInGame(uid) ) {
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

    /**
     * chat hat sich verändert. Serverchat muss geupated werden
     */
    public void chatAenderung(){
        aenderung++;
        if(chatbox != null){
            Platform.runLater(() -> {
                        chatbox.messages.getChildren().clear();
                        chatbox.messages.getChildren().addAll(chatbox.buildChat());
                        chatbox.scroll.setContent(chatbox.messages);
                    }
            );
        }

    }

    @Override
    public int getAnzahlSpieler() throws RemoteException {
        return anzSpieler;
    }

    @Override
    public void setCardHand(int spielerId, ArrayList<HandKarte> hand) throws RemoteException {
        tisch.getSpielerList()[spielerId].getCardHand().setHandKarten(hand);
        aenderung++;
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
                spiel.setUID(uid);
                spiel.setBlackChips(s.getBlackChips());
                spiel.setWhiteChips(s.getWhiteChips());
                spiel.setCardHand(s.getCardHand());
                spiel.setOldScore(s.getOldScore());
                spiel.setPoints(s.getPoints());
                if(!s.inGame()){
                    spiel.aussteigen();
                }
                tisch.spielerList[i] = spiel;
                aenderung++;

            }
        }
    }

    @Override
    public ArrayList<List<String>> getChat(boolean censored) throws RemoteException {
        return censored? censoredChatrecord: chatrecord;
    }

    @Override
    public void sendMessage(String message, String uid) throws RemoteException {
        List<String> zeile = new LinkedList<>();
        List<String> censoredZeile = new LinkedList<>();

        zeile.add(clients.get(uid));
        censoredZeile.add(clients.get(uid));


        if(!message.equals("")) {
            if(message.equals("/coinflip")) {

                String kopfzahl = Math.random() >= 0.5?"Kopf":"Zahl";
                zeile.add(message);
                zeile.add(kopfzahl);
                censoredZeile.add(message);
                censoredZeile.add(kopfzahl);


            } else
                if(message.length() > 6 && message.substring(0,6).equals("/roll ")) {
                    int die = Integer.valueOf(message.substring(6));
                    int erg = (int)((Math.random() * die) + 1);

                    zeile.add(Integer.toString(die));
                    censoredZeile.add(Integer.toString(die));
                    zeile.add(Integer.toString(erg));
                    censoredZeile.add(Integer.toString(erg));

                } else
                {
                    zeile.add(message);
                    censoredZeile.add(schimpfwortFilter(message));
                }
            chatrecord.add(zeile);
                censoredChatrecord.add(censoredZeile);
        }
        chatAenderung();
    }

    String schimpfwortFilter(String msg) {
        String[] msgwords = msg.split(" ");
        List<String> filterwords = new LinkedList<>();
        try {
            File filePath = new File("Team Eins\\src\\GUI\\schimpfworte.txt");
            String path = filePath.getAbsolutePath();
            File file =
                    new File(path);
            filterwords = Files.readAllLines(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println("Schimpfwortliste nicht gefunden");
        }

        for(int n = 0;n < msgwords.length;n++) {
            if(filterwords.contains(capitalize(msgwords[n]))) {
                String newword = "";
            for(int i = 0; i < msgwords[n].length(); i++) {
                newword += "*";
            }
                msgwords[n]  = newword;
            }
        }

        return String.join(" ", msgwords);
    }

    /**
     * Großer erster Buchstaben bei einem Wort, sodass der Schimpfwortfilter anschlägt
     * @param input
     * @return
     */
    public static String capitalize(String input) {
        if (input == null || input.length() <= 0) {
            return input;
        }
        char[] chars = new char[1];
        input.getChars(0, 1, chars, 0);
        if (Character.isUpperCase(chars[0])) {
            return input;
        } else {
            StringBuilder buffer = new StringBuilder(input.length());
            buffer.append(Character.toUpperCase(chars[0]));
            buffer.append(input.toCharArray(), 1, input.length()-1);
            return buffer.toString();
        }
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
    @Override
    public void reconnect(String UID, String name) throws RemoteException {
        Spieler s;
        Spieler newPlayer;
        for(int i = 0; i< anzSpieler; i++){
            s = tisch.getSpielerList()[i];
            if(s.getUid().equals(UID) && (s instanceof Bot)){
                System.out.println(name + " ist wieder im Spiel");
                changeName(UID, name);
                s.setLeftServer(false);

                newPlayer = new Spieler(name,UID);
                newPlayer.setBlackChips(s.getBlackChips());
                newPlayer.setWhiteChips(s.getWhiteChips());
                newPlayer.setCardHand(s.getCardHand());
                newPlayer.setOldScore(s.getOldScore());
                newPlayer.setPoints(s.getPoints());

                if(!s.inGame()){
                    newPlayer.aussteigen();
                }

                getGameStart(UID);
                clients.put(UID, name);
                readyClients.put(UID,false);
                anzClients++;

                tisch.spielerList[i] = newPlayer;
                aenderung++;

                System.out.println(newPlayer instanceof Bot);

            }
        }
    }

    @Override
    public boolean isInGame(String UID)  throws RemoteException{
        Spieler s;
        boolean out = false;
        if(tisch != null){
            for(int i = 0; i< anzSpieler; i++) {
                s = tisch.getSpielerList()[i];
                if (s.getUid().equals(UID)) {
                    out = true;
                }
            }
        }

        return out;
    }


}