import GUI.GUIChat;
import GUI.GuiHauptmenu;
import Main.*;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import RMI.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


import static org.junit.jupiter.api.Assertions.*;

public class ClientServerTest {
    ServerImpl server;
    RunServer runServer;
    RunClient client1;
    RunClient client2;
    Map<String,String> clientsMap = new LinkedHashMap<>();
    Main main;
    Tisch tisch;
    Spiellogik spiellogik;
    Spieler[] spielerM;
    GUIChat guiChat;

    /** setup für die Tests
     * @throws RemoteException
     * @throws NotBoundException
     * @throws AlreadyBoundException
     */
    @BeforeEach
    void setUp() throws RemoteException, NotBoundException, AlreadyBoundException {

        main = new Main();
        main.setTisch(null);
        main.setAnzSpieler(3);
        server = new ServerImpl();
        spielerM = new Spieler[3];
        clientsMap.put("1","client1");
        clientsMap.put("2","client2");
        spielerM[0] = new Spieler("test","0");


    }

    @AfterEach
    void close(){
        runServer.stop();
    }

    /** addClient-Methode und leaveServer-Methode von serverImpl testen
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws NotBoundException
     */
    @Test
    void leaveServer() throws RemoteException, AlreadyBoundException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();


        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        assertEquals("client1",runServer.getServer().getClients().get("1"));
        runServer.getServer().leaveServer("1");
        assertNull(runServer.getServer().getClients().get("1"));
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

        assertEquals("client1",runServer.getServer().getClients().get("1"));
        client1.getServer().changeName("1","c");
        assertEquals("c",runServer.getServer().getClients().get("1"));


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

        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);
        main.setTisch(tisch);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");



        //init tisch und spiellogik

        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setServer(server);
        main.setSpiellogik(spiellogik);

        assertEquals(1,runServer.getServer().assignId("1"));
        assertEquals(2,runServer.getServer().assignId("2"));
        assertEquals(-1,runServer.getServer().assignId("4"));

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

        //init tisch und spiellogik
        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);

        main.setSpiellogik(spiellogik);
        main.setServer(server);
        main.setHaende(new Hand[runServer.getServer().getAnzahlSpieler()]);
        runServer.getServer().neueRunde(true,client1.getuId());
        runServer.getServer().neueRunde(true,client2.getuId());
        runServer.getServer().neueRunde(true,server.getHost());
        tisch.setAktiv(1);
        int size = tisch.getNachziehStapelSize();
        runServer.getServer().karteNachziehen(tisch.getSpielerList()[1]);

        //gucken ob für beide Spieler der Nachziehstapel eine Karte weniger hat
        assertEquals(size-1, runServer.getServer().updateTisch().getNachziehStapelSize());
        assertEquals(size-1,runServer.getServer().updateTisch().getNachziehStapelSize());

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

        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setServer(server);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[runServer.getServer().getAnzahlSpieler()]);
        runServer.getServer().neueRunde(true,client1.getuId());
        runServer.getServer().neueRunde(true,client2.getuId());
        runServer.getServer().neueRunde(true,runServer.getServer().getHost());
        tisch.setAktiv(1);
        runServer.getServer().karteNachziehen(tisch.getSpielerList()[1]);

        //gucken ob für beide Spieler der Nachziehstapel eine Karte weniger hat;
        tisch.karteAblegen(tisch.getSpielerList()[1].getCardHand().getKarte(0));
        int val =  tisch.getSpielerList()[1].getCardHand().getKarte(0).getValue();
        runServer.getServer().karteLegen(tisch.getSpielerList()[1],new Karte(tisch.getSpielerList()[1].getCardHand().getKarte(0).getValue(),false));
        assertEquals(val,runServer.getServer().updateTisch().getObereKarteAblagestapel().getValue());

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

        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");

        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setServer(server);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[runServer.getServer().getAnzahlSpieler()]);
        runServer.getServer().neueRunde(true,client1.getuId());
        runServer.getServer().neueRunde(true,client2.getuId());
        runServer.getServer().neueRunde(true,server.getHost());

        tisch.setAktiv(1);
        int handkartenSum = 0;

        //Alle spieler steigen hier aus
        runServer.getServer().aussteigen(tisch.getSpielerList()[1]);
        assertEquals(false,runServer.getServer().updateTisch().getSpielerList()[1].inGame());
        runServer.getServer().aussteigen(runServer.getServer().updateTisch().getSpielerList()[2]);
        runServer.getServer().aussteigen(runServer.getServer().updateTisch().getSpielerList()[0]);

        //berechne Punktzahl für Handkarten des Spielers
        Set<Integer> handkarten = new LinkedHashSet<>();


        for (Karte c : tisch.getSpielerList()[1].getCardHand().getHandKarte()) {
            handkarten.add(c.getValue());
        }

        for (Integer c : handkarten) {
            handkartenSum += c;
        }
        assertEquals(runServer.getServer().updateTisch().getSpielerList()[1].getPoints(),-1*handkartenSum);

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

        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setServer(server);
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[client1.getServer().getAnzahlSpieler()]);
        runServer.getServer().neueRunde(true,client1.getuId());
        runServer.getServer().neueRunde(true,client2.getuId());
        runServer.getServer().neueRunde(true,runServer.getServer().getHost());
        tisch.setAktiv(1);
        int handkartenSum = 0;

        //Alle spieler steigen hier aus

        //Spiler hat 10 weiße und 0 schwarze Chips
        tisch.getSpielerList()[1].setWhiteChips(10);
        tisch.getSpielerList()[1].setBlackChips(0);
        runServer.getServer().chipsTauschen(1);

        assertEquals(1,runServer.getServer().updateTisch().getSpielerList()[1].getBlackChips());
        assertEquals(0,runServer.getServer().updateTisch().getSpielerList()[1].getWhiteChips());

        //Spieler hat keine Karten mehr
        tisch.getSpielerList()[1].setCardHand(new Hand());

        //Spieler gibt schwarzen Chip ab
        runServer.getServer().chipAbgeben(tisch.getSpielerList()[1],new BlackChip());
        assertEquals(0,runServer.getServer().updateTisch().getSpielerList()[1].getBlackChips());
        assertEquals(0,runServer.getServer().updateTisch().getSpielerList()[1].getWhiteChips());

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

        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setServer(runServer.getServer());
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[runServer.getServer().getAnzahlSpieler()]);

        //spieler wird durch Bot ersetzt
        runServer.getServer().replaceSpielerDurchBot("1");
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
        runServer.getServer().sendMessage("test","1");
        //coinflip
        runServer.getServer().sendMessage("/coinflip","2");
        //roll
        runServer.getServer().sendMessage("/roll 6","2");
        assertEquals("test",runServer.getServer().getChat(false).get(0).get(1));
        assertTrue(
            "Zahl".equals(runServer.getServer().getChat(false).get(1).get(2)) ||
                "Kopf".equals(runServer.getServer().getChat(false).get(1).get(2)));
        int test = Integer.parseInt(runServer.getServer().getChat(false).get(2).get(2));
        assertTrue(test > 0 && test <= 6);
    }

    @Test
    void reconnect() throws AlreadyBoundException, RemoteException, NotBoundException {
        //server aufstellen
        runServer = new RunServer("localhost","testServer", 8001, "0","test");
        runServer.starting();

        int i = 1;
        for (Map.Entry<String, String> entry : clientsMap.entrySet()) {
            spielerM[i] = new Spieler(entry.getValue(), entry.getKey());
            i++;
        }
        tisch = new Tisch(spielerM);

        //clients starten
        client1 = new RunClient("localhost",8001, "testServer","1", "client1");
        client2 = new RunClient("localhost",8001, "testServer","2", "client2");


        spiellogik = new Spiellogik(tisch);
        main.setTisch(tisch);
        main.setServer(runServer.getServer());
        main.setSpiellogik(spiellogik);
        main.setHaende(new Hand[runServer.getServer().getAnzahlSpieler()]);

        assertEquals("client1",runServer.getServer().getClients().get("1"));
        runServer.getServer().leaveServer("1");
        runServer.getServer().replaceSpielerDurchBot("1");
        assertNull(runServer.getServer().getClients().get("1"));

        runServer.getServer().reconnect("1","client1");
        assertEquals("client1",runServer.getServer().getClients().get("1"));

    }




}
