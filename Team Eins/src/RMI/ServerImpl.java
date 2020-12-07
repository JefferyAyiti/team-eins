package RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements server{

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this,0);
    }
}
