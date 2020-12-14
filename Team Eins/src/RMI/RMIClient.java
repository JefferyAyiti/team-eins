package RMI;

import Main.Main;
import Main.Tisch;
import javafx.application.Platform;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static Main.Main.*;
import static Main.Main.myTurnUpdate;
import static Main.Main.server;

public class RMIClient {
    public server server;
    String cname;
    int round = 0;

    /**
     * @param IP Server IP
     * @param Port Server Port
     * @param Server_Name
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RMIClient(String IP, int Port, String Server_Name, String UID, String Client_Name) throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(IP, Port);
        server = (server) registry.lookup(Server_Name);
        server.addClient(UID, Client_Name);
        cname = Client_Name;
    }



    /**
     * tisch Attribut wird durch das neue Attribut vom Server ersetzt
     */
    public void update() {
        try {
            Main.tisch = server.updateTisch();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if(tisch.aktiv != ich || (tisch.aktiv == ich &&
                myTurnUpdate) ||
                round < tisch.getDurchgangNr()) {
            if(round < tisch.getDurchgangNr())
                round = tisch.getDurchgangNr();

            Platform.runLater(() -> Main.spieltischGui.buildStage(Main.classPrimaryStage));
            if (tisch.aktiv == ich) {
                myTurnUpdate = false;
            } else
                myTurnUpdate = true;
        }
    }

    /**
     * Startet RunClient. Dieser Thread aktualisiert das Attribut tisch in regelmäßigen abständen.
     */
    public void startClientThread(){
        Thread ct = new Thread(new ClientThread( server, this));
        ct.start();
    }
    public Tisch getTisch(){
        return Main.tisch;
    }

    /**
     * Zwingt den Client zurück in das Hauptmenü, wenn eine Connection refused exception in ClientThread.run entsteht.
     */
    public void forceLeaveServer(){
        hauptmenuGui.cleanupServer();
        System.out.println("Forced Client Disconnecting");

        joined = false;
        Platform.runLater(() -> hauptmenuGui.showSettingsMenu(Main.classPrimaryStage));
    }
}
