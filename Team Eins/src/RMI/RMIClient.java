package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private final server server;

    public RMIClient(String IP, int Port, String Server_Name) throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(IP, Port);
        server = (server) registry.lookup(Server_Name);
    }
}
