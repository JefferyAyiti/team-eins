package RMITest;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.setProperty("java.rmi.server.hostname","localhost");

        System.setProperty("java.security.policy","file:///tmp/test.policy");
        server server = new ServerImpl();
        Registry registry = LocateRegistry.createRegistry(8001);
        registry.bind("Server" , server);
        System.out.println("Server started");
    }
}


