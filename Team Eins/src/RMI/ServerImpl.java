package RMI;

import Main.*;
import javafx.util.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static Main.Main.*;

public class ServerImpl implements server {


    Map<String, String> clients = new LinkedHashMap<>();
    long aenderung = 0;
    String host;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }


    @Override
    public void addClient(String uid, String name) {
        aenderung++;
        clients.put(uid, name);
    }

    @Override
    public void leaveServer(String client) throws RemoteException {
        aenderung++;
        clients.remove(client);
    }

    @Override
    public Map<String, String> getClients() {
        return clients;
    }

    @Override
    public int assignId(String uid) {
        int i = 0;
        for (Map.Entry<String, String> entry : clients.entrySet()) {
            if(uid.equals(entry.getKey())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public void changeName(String uid, String name) throws RemoteException {
        clients.replace(uid, name);
    }

    @Override
    public boolean getGameStart() throws RemoteException {
        aenderung++;
        return gameRunning;
    }

    @Override
    public long getAenderung() throws RemoteException {
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
    public void neueRunde() throws RemoteException {
        spiellogik.initNeueRunde();
        aenderung++;
    }

    @Override
    public Map<Spieler, Integer> getRangliste() throws RemoteException {
        aenderung++;
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
        aenderung++;
        return spiellogik.getRundeBeendet();
    }

    @Override
    public boolean getSpielBeendet() throws RemoteException {
        aenderung++;
        return spiellogik.spielBeendet;

    }

    @Override
    public int getDurchgangNr() throws RemoteException {
        return tisch.getDurchgangNr();
    }

    @Override
    public Tisch updateTisch() throws RemoteException {
        return tisch;
    }

    @Override
    public void shuffleSpieler() throws RemoteException {
        ArrayList<Pair<String, String>> clientsArr = new ArrayList<>();
        for (Map.Entry<String, String> entry : clients.entrySet()) {
            clientsArr.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        Collections.shuffle(clientsArr);
        clients = new LinkedHashMap<>();
        for(Pair<String, String> c:clientsArr) {
            clients.put(c.getKey(), c.getValue());
        }
    }


}