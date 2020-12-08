package RMI;

import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class ClientThread implements Runnable{
    private RMIClient client;
    private ServerImpl server;
    Long aenderung = 0L;
    public ClientThread(ServerImpl server,RMIClient client){
        this.client = client;
        this.server = server;
    }


    @Override
    public void run() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (server.aenderung > aenderung){
                    aenderung = server.aenderung;
                    client.update();
                }

            }
        }, 100,500);
    }

}
