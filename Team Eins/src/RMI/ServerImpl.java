package RMI;

import Main.Tisch;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import static Main.Main.tisch;

public class ServerImpl implements server{

    public Long aenderung;
    List<String> clients = new LinkedList<>();

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public void addClient(String client) {
        clients.add(client);
    }

    @Override
    public List<String> getClients() {
        return clients;
    }

    @Override
    public int assignId(String name) {
        for(int i = 0; i < clients.size(); i++) {
            if(clients.get(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Tisch update() {
        return tisch;
    }


}
