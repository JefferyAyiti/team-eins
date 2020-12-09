package RMI;

import RMI.Server.Server;

import java.util.Timer;
import java.util.TimerTask;

public class ClientThread implements Runnable{
    private RMIClient client;
    private Server server;
    Long aenderung = 0L;
    public ClientThread(Server server, RMIClient client){
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
