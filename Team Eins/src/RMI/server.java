package RMI;

import Main.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface server extends Remote {


    void addClient(String uid, String client) throws RemoteException;
    void leaveServer(String client) throws RemoteException;
    Map<String, String> getClients() throws RemoteException;
    int assignId(String name) throws RemoteException;
    void changeName(String uid, String name) throws RemoteException;
    boolean getGameStart(String uid) throws RemoteException;
    long getAenderung(String uid) throws RemoteException;
    void incAenderung() throws RemoteException;
    int getAnzahlSpieler() throws RemoteException;
    void neueRunde() throws RemoteException;
    Map<Spieler, Integer> getRangliste() throws RemoteException;
    void setHost(String uid) throws RemoteException;
    String getHost() throws RemoteException;
    int getBotLevel()throws RemoteException;

    void closeServer() throws  RemoteException;
    boolean serverOpen() throws RemoteException;

    int getAnzClients() throws  RemoteException;
    int getAnzReadyClients() throws RemoteException;

    void karteLegen(Spieler spieler, Karte karte) throws RemoteException;
    void karteNachziehen(Spieler spieler) throws RemoteException;
    void chipAbgeben(Spieler spieler, Chip chip) throws RemoteException;
    void chipsTauschen(int playerID) throws RemoteException;
    void aussteigen(Spieler spieler) throws RemoteException;
    boolean getRundeBeendet() throws RemoteException;
    boolean getSpielBeendet() throws RemoteException;
    int getDurchgangNr() throws RemoteException;
    Tisch updateTisch() throws RemoteException;

    void replaceSpielerDurchBot(String uid) throws RemoteException;
    List<List<String>> getChat() throws RemoteException;
    void sendMessage(String message, String uid)throws RemoteException;

    void checkTimeout() throws RemoteException;

}
