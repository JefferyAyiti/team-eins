package RMI;

import Main.Tisch;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface server extends Remote {
    void addClient(String client) throws RemoteException;
    List<String> getClients() throws RemoteException;
    int assignId(String name) throws RemoteException;

    Tisch update() throws RemoteException;
}
