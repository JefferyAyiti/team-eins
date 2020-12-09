package RMI;

import javafx.application.Platform;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientThread implements Runnable{
    private RMIClient client;
    private server server;
    long aenderung = 0;
    public ClientThread(server server,RMIClient client){
        this.client = client;
        this.server = server;
    }


    @Override
    public void run() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try {
                    if (server.getAenderung() > aenderung){
                        aenderung = server.getAenderung();
                        client.update();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }, 100,500);
    }

}
