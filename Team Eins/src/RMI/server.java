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

    /**
     * @return Gibt eine hashMap aus, die den Clients zugeordnet sind.
     * @throws RemoteException
     */
    Map<String, String> getClients() throws RemoteException;

    /**
     * @param name
     * @return Index der Spielerliste am Tisch
     * @throws RemoteException
     * Weißt jedem CLient den Index der Spielerliste am Tisch zu
     */
    int assignId(String name) throws RemoteException;

    /**
     * @param uid
     * @return gameRunning
     * @throws RemoteException
     */
    boolean getGameStart(String uid) throws RemoteException;
    long getAenderung(String uid) throws RemoteException;
    void incAenderung() throws RemoteException;
    int getAnzahlSpieler() throws RemoteException;

    /**
     * Speichert die neue Hand nach Umsortierung per Drag&Drop
     * @throws RemoteException
     */
    void setCardHand(int spielerId, ArrayList<HandKarte> hand) throws RemoteException;

    /**
     * Wenn alle Spieler ready sind, wird eine neue Runde gestartet.
     * @param countUp Flag um ammountReadyClients hochzuzählen. Wird für den Wartebildcshirm verwendet
     * @param uid Id des Clients/Server der ready ist
     * @throws RemoteException
     */
    void neueRunde(boolean countUp,String uid) throws RemoteException;

    /**
     * @return Spieler Rangliste die vom Server ausgewertet wird
     * @throws RemoteException
     */
    Map<Spieler, Integer> getRangliste() throws RemoteException;

    /**Setzt den Host.
     * @param uid
     * @throws RemoteException
     */
    void setHost(String uid) throws RemoteException;

    /**
     * @return Gibt den Host namen zurück.
     * @throws RemoteException
     */
    String getHost() throws RemoteException;

    /**
     * @return Bot Level als Integer.
     * @throws RemoteException
     */
    int getBotLevel()throws RemoteException;

    /**beendet den Server indem er den Flag Lock auf false setzt.
     * @throws RemoteException
     */
    void closeServer() throws  RemoteException;

    /**
     * @return ob der Server geschlossen ist oder nicht.(lock)
     * @throws RemoteException
     */
    boolean serverOpen() throws RemoteException;

    /**
     * @return Anzahl gesamter Clients im Spiel.
     * @throws RemoteException
     */
    int getAnzClients() throws  RemoteException;

    /**
     * @return Anzahl Clients die bereit für die nächste Runde sind.
     * @throws RemoteException
     */
    int getAnzReadyClients() throws RemoteException;

    /** Server Methode zum Karten legen.
     * @param spieler
     * @param karte
     * @throws RemoteException
     */
    void karteLegen(Spieler spieler, Karte karte) throws RemoteException;

    /**Server Methode zum Karten nachziehen.
     * @param spieler
     * @throws RemoteException
     */
    void karteNachziehen(Spieler spieler) throws RemoteException;

    /**Server Methode zum Chip abgeben.
     * @param spieler
     * @param chip
     * @throws RemoteException
     */
    void chipAbgeben(Spieler spieler, Chip chip) throws RemoteException;

    /**Server Methode zum Chips tauschen.
     * @param playerID
     * @throws RemoteException
     */
    void chipsTauschen(int playerID) throws RemoteException;

    /**Server Methode zum aussteigen.
     * @param spieler
     * @throws RemoteException
     */
    void aussteigen(Spieler spieler) throws RemoteException;

    /**
     * @return true wenn Runde beendet, false wenn Runde noch nicht vorbei ist.
     * @throws RemoteException
     */
    boolean getRundeBeendet() throws RemoteException;

    /**
     * @return true, wenn das SPiel vorbei ist, false wenn es noch nicht beendet ist.
     * @throws RemoteException
     */
    boolean getSpielBeendet() throws RemoteException;

    /**
     * @return aktuellen Tisch von der Server Seite.
     * @throws RemoteException
     */
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
    List<List<String>> getChat(boolean censored) throws RemoteException;


    /**
     * @param message Inhalt der Nachricht
     * @param uid Absender der Nachrich
     * @throws RemoteException
     * Speicher die Chatnachricht auf dem Server und
     * führt Funktionen wie /roll und /coinflip einmalig aus
     */
    void sendMessage(String message, String uid)throws RemoteException;


    /**Wird verwendet um zu prüfen, ob eine neue Runde startet. ammountReadyClient wird nicht erhöht
     * @throws RemoteException
     */
    void checkForNewRound() throws RemoteException;

    void changeName(String uid, String name) throws RemoteException;

    int getDurchgangNr() throws RemoteException;

    /**
     * @param name
     * @return Spieler mit dem zugehörigen Namen auf dem Server
     * @throws RemoteException
     */
    Spieler getSpieler(String name) throws RemoteException;

    int reconnect(String UID, String name) throws RemoteException;

    boolean isInGame(String UID)  throws RemoteException;
}
