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

    /**
     * @param name
     * @return Index der Spielerliste am Tisch
     * @throws RemoteException
     * Weißt jedem CLient den Index der Spielerliste am Tisch zu
     */
    int assignId(String name) throws RemoteException;

    boolean getGameStart(String uid) throws RemoteException;
    long getAenderung(String uid) throws RemoteException;
    void incAenderung() throws RemoteException;
    int getAnzahlSpieler() throws RemoteException;
    void neueRunde(boolean countUp) throws RemoteException;
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
    Tisch updateTisch() throws RemoteException;


    /**
     * @throws RemoteException
     * Prüft ob ein Client seit längerem nicht mehr auf den Server zugegriffen hat,
     * falls ja wird replaceSpielerDurchBot() aufgerufen
     */
    void checkTimeout() throws RemoteException;

    /**
     * @param uid Spieler-ID
     * @throws RemoteException
     * Spieler wird durch Bot ersetzt bei Leave/Timeout
     */
    void replaceSpielerDurchBot(String uid) throws RemoteException;

    /**
     * @return Liste der Chatnachrichten und deren Absender
     * @throws RemoteException
     */
    List<List<String>> getChat() throws RemoteException;


    /**
     * @param message Inhalt der Nachricht
     * @param uid Absender der Nachrich
     * @throws RemoteException
     * Speicher die Chatnachricht auf dem Server und
     * führt Funktionen wie /roll und /coinflip einmalig aus
     */
    void sendMessage(String message, String uid)throws RemoteException;



    void checkForNewRound() throws RemoteException;
}
