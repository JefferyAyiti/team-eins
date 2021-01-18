package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {

    public static RMIClient client;
    private String CLIENT_NAME;
    private String uId;

    /**
     * @param IP Server IP
     * @param Port Server Port
     * @param Server_Name
     * @param Client_Name
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RunClient(String IP, int Port, String Server_Name, String uniqueId, String Client_Name) throws RemoteException, NotBoundException {
        CLIENT_NAME = Client_Name;
        System.setProperty("java.security.policy","file:///tmp/test.policy");

        client = new RMIClient(IP, Port, Server_Name, uniqueId, Client_Name);
        uId = uniqueId;

        System.out.println("Client Started");
    }

    /** getter-Methode für server
     * @return server
     */
    public RMI.server getServer() {
        return client.getServer();
    }

    /** getter-Methode für uId
     * @return uid des Clients
     */
    public String getuId() {
        return uId;
    }
}
