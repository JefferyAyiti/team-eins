package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClient extends Remote {
    void getMessage(String message) throws RemoteException;
}
