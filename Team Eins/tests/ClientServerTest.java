import GUI.GUIChat;
import GUI.GuiHauptmenu;
import Main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import RMI.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerTest {
    ServerImpl server;
    RunServer runServer;
    RunClient client1;
    RunClient client2;
    ClientThread clientThread;
    Main main;
    Tisch tisch;
    Spiellogik spiellogik;
    Spieler[] spielerM;

    @BeforeEach
    void setUp() throws RemoteException, NotBoundException, AlreadyBoundException {
        main = new Main();
        main.setAnzSpieler(3);
        server = new ServerImpl();
        spielerM = new Spieler[3];


    }


    /** addClient-Methode und leaveServer-Methode von serverImpl testen
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws NotBoundException
     */
    @Test
    void addClientUndleaveServer() throws RemoteException, AlreadyBoundException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        assertEquals(3,client1.getServer().getAnzahlSpieler());
        assertEquals("client1",client2.getServer().getClients().get("1"));
        assertEquals("client2",client2.getServer().getClients().get("2"));

        client2.getServer().leaveServer("2");
        assertEquals(false,client1.getServer().getClients().containsKey("2"));
        assertEquals(2,client1.getServer().getAnzahlSpieler());  //FEHLER? Anzahl an Spielern bleibt gleich



    }


    /** Test für changeName-Methode von serverImpl
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws NotBoundException
     */
    @Test
    void changeName() throws RemoteException, AlreadyBoundException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        client1.getServer().changeName("1","c");
        assertEquals("c",client1.getServer().getClients().get("1"));
    }


    /** Test für assignId-Methode von serverImpl
     * @throws AlreadyBoundException
     * @throws RemoteException
     * @throws NotBoundException
     */
    @Test
    void assignId() throws AlreadyBoundException, RemoteException, NotBoundException {

        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        int i = -1;
        for (Map.Entry<String, String> entry : client1.getServer().getClients().entrySet()) {
            i++;
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
        }

        //init tisch und spiellogik
        tisch = new Tisch(spielerM);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);

        assertEquals(1,server.assignId("1"));
        assertEquals(2,server.assignId("2"));
        assertEquals(-1,server.assignId("4"));

    }

    /** Test für karteNachziehen
     * @throws AlreadyBoundException
     * @throws RemoteException
     * @throws NotBoundException
     */
    @Test
    void karteNachziehen() throws AlreadyBoundException, RemoteException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        //init tisch und spiellogik
        int i = -1;
        for (Map.Entry<String, String> entry : client1.getServer().getClients().entrySet()) {
            i++;
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
        }
        tisch = new Tisch(spielerM);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[client1.getServer().getAnzahlSpieler()]);
        client1.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);
        tisch.setAktiv(1);
        int size = tisch.getNachziehStapelSize();
        client1.getServer().karteNachziehen(tisch.getSpielerList()[1]);

        //gucken ob für beide Spieler der Nachziehstapel eine Karte weniger hat
        assertEquals(size-1, client1.getServer().updateTisch().getNachziehStapelSize());
        assertEquals(size-1,client2.getServer().updateTisch().getNachziehStapelSize());
    }

    /** Test für karteLegen
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws NotBoundException
     */
    @Test
    void karteLegen() throws RemoteException, AlreadyBoundException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        //init tisch und spiellogik
        int i = -1;
        for (Map.Entry<String, String> entry : client1.getServer().getClients().entrySet()) {
            i++;
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
        }
        tisch = new Tisch(spielerM);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[client1.getServer().getAnzahlSpieler()]);
        client1.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);
        tisch.setAktiv(1);
        client1.getServer().karteNachziehen(tisch.getSpielerList()[1]);

        //gucken ob für beide Spieler der Nachziehstapel eine Karte weniger hat;
        tisch.karteAblegen(tisch.getSpielerList()[1].getCardHand().getKarte(0));
        int val =  tisch.getSpielerList()[1].getCardHand().getKarte(0).getValue();
        client1.getServer().karteLegen(tisch.getSpielerList()[1],new Karte(tisch.getSpielerList()[1].getCardHand().getKarte(0).getValue(),false));
        assertEquals(val,client2.getServer().updateTisch().getObereKarteAblagestapel().getValue());

    }

    /** Test für aussteigen
     * @throws RemoteException
     * @throws NotBoundException
     * @throws AlreadyBoundException
     */
    @Test
    void aussteigen() throws RemoteException, NotBoundException, AlreadyBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        //init tisch und spiellogik
        int i = -1;
        for (Map.Entry<String, String> entry : client1.getServer().getClients().entrySet()) {
            i++;
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
        }
        tisch = new Tisch(spielerM);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[client1.getServer().getAnzahlSpieler()]);
        client1.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);

        tisch.setAktiv(1);
        int handkartenSum = 0;

        //Alle spieler steigen hier aus
        client1.getServer().aussteigen(tisch.getSpielerList()[1]);
        assertEquals(false,tisch.getSpielerList()[1].inGame());
        client2.getServer().aussteigen(tisch.getSpielerList()[2]);
        client2.getServer().aussteigen(tisch.getSpielerList()[0]);

        //berechne Punktzahl für Handkarten des Spielers
        Set<Integer> handkarten = new LinkedHashSet<>();


        for (Karte c : tisch.getSpielerList()[1].getCardHand().getHandKarte()) {
            handkarten.add(c.getValue());
        }

        for (Integer c : handkarten) {
            handkartenSum += c;
        }
        assertEquals(tisch.getSpielerList()[1].getPoints(),-1*handkartenSum);

        tisch.setAktiv(1);

    }

    /** Test für chipsAbgeben und chipTauschen
     * @throws RemoteException
     * @throws NotBoundException
     * @throws AlreadyBoundException
     */
    @Test
    void chipsMethoden() throws RemoteException, NotBoundException, AlreadyBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        //init tisch und spiellogik
        int i = -1;
        for (Map.Entry<String, String> entry : client1.getServer().getClients().entrySet()) {
            i++;
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
        }
        tisch = new Tisch(spielerM);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[client1.getServer().getAnzahlSpieler()]);
        client1.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);
        client2.getServer().neueRunde(true);

        tisch.setAktiv(1);
        int handkartenSum = 0;

        //Alle spieler steigen hier aus

        //Spiler hat 10 weiße und 0 schwarze Chips
        tisch.getSpielerList()[1].setWhiteChips(10);
        tisch.getSpielerList()[1].setBlackChips(0);
        client1.getServer().chipsTauschen(1);

        assertEquals(1,tisch.getSpielerList()[1].getBlackChips());
        assertEquals(0,tisch.getSpielerList()[1].getWhiteChips());

        //Spieler hat keine Karten mehr
        tisch.getSpielerList()[1].setCardHand(new Hand());

        //Spieler gibt schwarzen Chip ab
        client1.getServer().chipAbgeben(tisch.getSpielerList()[1],new BlackChip());

        assertEquals(0,tisch.getSpielerList()[1].getBlackChips());
        assertEquals(0,tisch.getSpielerList()[1].getWhiteChips());

    }

    /** Test für replaceSpielerDurchBot
     * @throws AlreadyBoundException
     * @throws RemoteException
     * @throws NotBoundException
     */
    @Test
    void replaceSpielerDurchBot() throws AlreadyBoundException, RemoteException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        //init tisch und spiellogik
        int i = -1;
        for (Map.Entry<String, String> entry : client1.getServer().getClients().entrySet()) {
            i++;
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
        }
        tisch = new Tisch(spielerM);
        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[client1.getServer().getAnzahlSpieler()]);

        //spieler wird durch Bot ersetzt
        server.replaceSpielerDurchBot("1");
        Bot bot = new Bot("bot",1);
        assertEquals(bot.getClass(),tisch.getSpielerList()[1].getClass());
    }

    @Test
    void chatTest() throws AlreadyBoundException, RemoteException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        //einfache Message
        server.sendMessage("test","1");
        //coinflip
        server.sendMessage("/coinflip","2");
        //roll
        server.sendMessage("/roll 6","2");
        assertEquals("test",server.getChat().get(0).get(1));
        assertTrue("Zahl" == server.getChat().get(1).get(2) || "Kopf" == server.getChat().get(1).get(2));
        int test = Integer.parseInt(server.getChat().get(2).get(2));
        assertTrue(test > 0 && test <= 6);
    }




}
