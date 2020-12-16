package RMI;

import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import Main.*;

/**
 * Ein Thread der immer wieder überprüft, ob Änderungen auf dem Server gab
 * und dementsprechend den Client updated
 */
public class ClientThread implements Runnable{

    private RMIClient client;
    private server server;
    long aenderung = 0;

    /**Hier werden server und client initialisiert .
     * @param server Der Server mit dem die Clients verbunden sind.
     * @param client Der Client der den Thread startet.
     */
    public ClientThread(server server,RMIClient client){
        this.client = client;
        this.server = server;
    }


    /**
     * run-methode für den Thread.
     * Mit einem Timer wird alle 500 Millisekunden nach Änderungen auf dem Server geguckt.
     * Bei Änderung wird der Client geupdated.
     *
     */
    @Override
    public void run() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    if (server.getAenderung(Main.uniqueID) > aenderung){
                        aenderung = server.getAenderung(Main.uniqueID);
                        client.update();
                    }
                } catch (RemoteException e) {
                    System.err.println("Lost connection to Server");
                    e.printStackTrace();
                    t.cancel();
                    client.forceLeaveServer();
                }

            }
        }, 100,500);
    }

}
