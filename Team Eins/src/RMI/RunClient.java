package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {

    private static RMIClient client;
    private String CLIENT_NAME;

    /**
     * @param IP Server IP
     * @param Port Server Port
     * @param Server_Name
     * @param Client_Name
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RunClient(String IP, int Port, String Server_Name, String Client_Name) throws RemoteException, NotBoundException {
        CLIENT_NAME = Client_Name;
        //TODO Policy muss hinzugefügt werden
        System.setProperty("java.security.policy","file:///tmp/test.policy");

        client = new RMIClient(IP, Port, Server_Name);


        System.out.println("Client Started");
    }
}
