package RMITest;


import java.rmi.Remote;
import java.rmi.RemoteException;

interface server extends Remote {

    void storeNewMSG(String input) throws RemoteException;
    String getNewMSG() throws RemoteException;
    boolean isNewMSG() throws  RemoteException;
}
