package RMI;

import Main.Tisch;
import RMI.Server.Server;
import RMI.Server.LamaServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    private final LamaServer server;
    private Tisch tisch;

    /**
     * @param IP Server IP
     * @param Port Server Port
     * @param Server_Name
     * @throws RemoteException
     * @throws NotBoundException
     */
    public RMIClient(String IP, int Port, String Server_Name) throws RemoteException, NotBoundException {

        Registry registry = LocateRegistry.getRegistry(IP, Port);
        server = (LamaServer) registry.lookup(Server_Name);
    }

    /**
     * tisch Attribut wird durch das neue Attribut vom Server ersetzt
     */
    public void update() {
        tisch = server.update();
        //TODO Gui.update() (?)
    }

    /**
     * Startet RunClient. Dieser Thread aktualisiert das Attribut tisch in regelmäßigen abständen.
     */
    public void startClientThread(){
        ClientThread ct = new ClientThread((Server) server, this);
        ct.run();
    }
    public Tisch getTisch(){
        return tisch;
    }
}
