package RMI.Server;

import RMITest.RMIClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements LamaServer {
    private List<RMIClient> clientsForBroadcast;

    public Server() throws RemoteException {
        UnicastRemoteObject.exportObject(this,0);
        clientsForBroadcast = new ArrayList<>();
    }
    @Override
    public void update(){}

}
