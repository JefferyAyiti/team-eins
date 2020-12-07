package RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
    static String IP;
    static String SERVER_NAME;
    static int PORT;

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
        System.setProperty("java.security.policy","file:///tmp/test.policy");

        server server = new ServerImpl();
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind(SERVER_NAME , server);


        System.out.println("Server started");
    }
}
