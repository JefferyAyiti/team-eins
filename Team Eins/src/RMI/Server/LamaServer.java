package RMI.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LamaServer extends Remote {
    void karteLegen() throws RemoteException;
    void karteNachziehen() throws RemoteException;
    void chipsKassieren() throws RemoteException;
    void chipAbgeben() throws RemoteException;
    void chipsTauschen() throws RemoteException;
    void aussteigen() throws RemoteException;
    void alleAussteigen() throws RemoteException;
    void ranglisteErstellen() throws RemoteException;
    void rundeBeenden() throws RemoteException;
    void rangListeErstellenNurWeißeChips() throws RemoteException;
    void initNeueRunde() throws RemoteException;
    void update() throws RemoteException;
}
