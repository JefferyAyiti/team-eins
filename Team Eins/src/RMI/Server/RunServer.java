package RMI.Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
    static String IP;
    static String SERVER_NAME;
    static int PORT;

    public static void main(String[] args) {
        try {
            RunServer test = new RunServer("localhost", "lamaland", 1099);
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt und startet den Server, mit angegebenen Server namen, IP und Port
     *
     * @param ip
     * @param server_Name
     * @param port
     * @throws AlreadyBoundException
     * @throws RemoteException
     */
    public RunServer(String ip, String server_Name, int port) throws AlreadyBoundException, RemoteException {
        this.IP = ip;
        this.SERVER_NAME = server_Name;
        this.PORT = port;
        starting();
    }

    private void starting() throws RemoteException, AlreadyBoundException {
        System.setProperty("java.rmi.server.hostname",IP);
        // TODO Policy muss hinzugefügt werden
        System.setProperty("java.security.policy","all.policy");

        LamaServer server = new Server();
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind(SERVER_NAME , server);

        System.out.println("Server started");
    }


}
