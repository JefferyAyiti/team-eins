package RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RunServer {
    static String IP;
    static String SERVER_NAME;
    static int PORT;
    static String myname;
    static String uid;
    static Registry registry;

    /**
     * Erstellt und startet den Server, mit angegebenen Server namen, IP und Port
     *
     * @param ip
     * @param server_Name
     * @param port
     * @throws AlreadyBoundException
     * @throws RemoteException
     */
    public RunServer(String ip, String server_Name, int port, String uid, String name)
            throws AlreadyBoundException, RemoteException {
        this.IP = ip;
        this.SERVER_NAME = server_Name;
        this.PORT = port;
        this.myname = name;
        this.uid = uid;
    }


    public server starting() throws RemoteException, AlreadyBoundException {



        System.setProperty("java.rmi.server.hostname","25.101.225.129");
        // TODO Policy muss hinzugefügt werden
        System.setProperty("java.security.policy","file:///tmp/test.policy");

        server server = new ServerImpl();
        registry = LocateRegistry.createRegistry(PORT);
        registry.bind(SERVER_NAME , server);
        server.addClient(uid, myname);

        System.out.println("Server started");
        return server;
    }

    public void stop() {
        try {
            UnicastRemoteObject.unexportObject(registry, true);
            registry.unbind(SERVER_NAME);
            System.out.println("Server beendet");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
