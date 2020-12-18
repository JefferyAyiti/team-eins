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
    static ArrayList<String> chatrecord = new ArrayList<>();
    int anzClients = 0;
    long aenderung = 0;
    String host;
    boolean lock = true;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public boolean serverOpen() throws RemoteException {
        return lock;
    }

    @Override
    public void addClient(String uid, String name) {
        anzClients++;
        int max =0;
        try {
            max = getAnzahlSpieler();
        } catch (RemoteException e) {}
        if(anzClients <= max){
            aenderung++;
            clients.put(uid, name);
        }else{
            lock = false;
            System.out.println("Maximal Anzahl von Spieler erreicht. Keine Zugang");
        }

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
        System.out.println("assign id "+uid);
        System.out.println(Main.tisch.getSpielerList());

        for (int i = 0;i < anzSpieler;i++) {
            if(Main.tisch.getSpielerList()[i].getUid().equals(uid)) {
                return i;
            }
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

    }

    @Override
    public void updateClients(String message) throws RemoteException {

    }

    @Override
    public void shareMessage(String message) throws RemoteException {

    }
}