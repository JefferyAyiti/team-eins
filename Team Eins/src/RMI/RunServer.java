package RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
    static String IP;
    static String SERVER_NAME;
    static int PORT;
    static String myname;

    /**
     * Erstellt und startet den Server, mit angegebenen Server namen, IP und Port
     *
     * @param ip
     * @param server_Name
     * @param port
     * @throws AlreadyBoundException
     * @throws RemoteException
     */
    public RunServer(String ip, String server_Name, int port, String name) throws AlreadyBoundException, RemoteException {
        this.IP = ip;
        this.SERVER_NAME = server_Name;
        this.PORT = port;
        this.myname = name;
    }


    public server starting() throws RemoteException, AlreadyBoundException {



        System.setProperty("java.rmi.server.hostname",IP);
        // TODO Policy muss hinzugefügt werden
        System.setProperty("java.security.policy","file:///tmp/test.policy");

        server server = new ServerImpl();
        Registry registry = LocateRegistry.createRegistry(PORT);
        registry.bind(SERVER_NAME , server);
        server.addClient(myname);

        System.out.println("Server started");
        return server;
    }
}
