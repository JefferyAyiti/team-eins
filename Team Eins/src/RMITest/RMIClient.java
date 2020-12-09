package RMITest;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private final server server;

    public RMIClient() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("25.73.135.237", 8001);
        server = (server) registry.lookup("Server");
    }

    String getNewMSG() throws RemoteException {
        if(server.isNewMSG()){
            return server.getNewMSG();
        }else{
            return "";
        }
    }

    void storeNewMSG(String input) throws RemoteException {
        server.storeNewMSG(input);
    }
    boolean serverNewMSG() throws RemoteException {
        return server.isNewMSG();
    }

}
