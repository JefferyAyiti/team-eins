package RMI;

import Main.Chip;
import Main.Karte;
import Main.Spieler;
import Main.Tisch;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import static Main.Main.spiellogik;

public class ServerImpl implements server {

    public Long aenderung;
    Map<String, String> clients = new HashMap<>();

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
    public int assignId(String name) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

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