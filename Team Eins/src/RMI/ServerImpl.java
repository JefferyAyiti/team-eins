package RMI;

import Main.Chip;
import Main.Karte;
import Main.Spieler;
import Main.Tisch;
import javafx.scene.control.Label;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedHashMap;
import java.util.Map;

import static Main.Main.gameRunning;
import static Main.Main.spiellogik;

public class ServerImpl implements server {


    Map<String, String> clients = new LinkedHashMap<>();
    long aenderung = 0;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }


    @Override
    public void addClient(String uid, String name) {
        clients.put(uid, name);
    }

    @Override
    public void leaveServer(String client) throws RemoteException {
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

    }

    @Override
    public void chipsTauschen(Spieler spieler) throws RemoteException {

    }

    @Override
    public void aussteigen(Spieler spieler) throws RemoteException {

    }

    @Override
    public Tisch update() throws RemoteException {
        return null;
    }

}