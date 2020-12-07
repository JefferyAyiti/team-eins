package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {

    private static RMIClient client;
    private String CLIENT_NAME;

    public void starter(String IP, int Port, String Server_Name, String Client_Name) throws RemoteException, NotBoundException {
        CLIENT_NAME = Client_Name;
        //TODO Policy muss hinzugefügt werden
        System.setProperty("java.security.policy","file:///tmp/test.policy");

        client = new RMIClient(IP, Port, Server_Name);


        System.out.println("Client Started");
    }
}
