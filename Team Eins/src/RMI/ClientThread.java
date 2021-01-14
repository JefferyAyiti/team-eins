package RMI;

import GUI.GuiSpieltisch;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import Main.*;
import javafx.scene.layout.VBox;

import static Main.Main.chatbox;
import static Main.Main.server;

/**
 * Ein Thread der immer wieder überprüft, ob Änderungen auf dem Server gab
 * und dementsprechend den Client updated
 */
public class ClientThread implements Runnable {

    private RMIClient client;
    private server server;
    long aenderung = 0;

    /**
     * Hier werden server und client initialisiert .
     *
     * @param server Der Server mit dem die Clients verbunden sind.
     * @param client Der Client der den Thread startet.
     */
    public ClientThread(server server, RMIClient client) {
        this.client = client;
        this.server = server;
    }


    /**
     * run-methode für den Thread.
     * Mit einem Timer wird alle 500 Millisekunden nach Änderungen auf dem Server geguckt.
     * Bei Änderung wird der Client geupdated.
     */
    @Override
    public void run() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {

                    if(!server.serverOpen() || server.getSpieler(client.cname).getLeftServer()){
                        server = null;
                    }
                    if (server.getAenderung(Main.uniqueID) > aenderung) {
                        aenderung = server.getAenderung(Main.uniqueID);
                        Platform.runLater(() -> {
                                    chatbox.messages.getChildren().clear();
                                    chatbox.messages.getChildren().addAll(chatbox.buildChat());
                                    chatbox.scroll.setContent(chatbox.messages);
                                }
                        );
                        client.update();
                    }
                } catch (RemoteException | NullPointerException e) {
                    System.err.println(e.toString());
                    System.err.println("Lost connection to Server");
                    t.cancel();
                    client.forceLeaveServer();
                }
            }
        }, 100, 500);
    }

}
