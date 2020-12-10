package RMI;

import Main.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface server extends Remote {


    void addClient(String uid, String client) throws RemoteException;
    void leaveServer(String client) throws RemoteException;
    Map<String, String> getClients() throws RemoteException;
    int assignId(String name) throws RemoteException;
    void changeName(String uid, String name) throws RemoteException;
    boolean getGameStart() throws RemoteException;
    long getAenderung() throws RemoteException;
    void incAenderung() throws RemoteException;
    int getAnzahlSpieler() throws RemoteException;
    void neueRunde() throws RemoteException;
    Map<Spieler, Integer> getRangliste() throws RemoteException;

    void karteLegen(Spieler spieler, Karte karte) throws RemoteException;
    void karteNachziehen(Spieler spieler) throws RemoteException;
    void chipAbgeben(Spieler spieler, Chip chip) throws RemoteException;
    void chipsTauschen(Spieler spieler) throws RemoteException;
    void aussteigen(Spieler spieler) throws RemoteException;
    boolean getRundeBeendet() throws RemoteException;
    boolean getSpielBeendet() throws RemoteException;

    Tisch updateTisch() throws RemoteException;
}
