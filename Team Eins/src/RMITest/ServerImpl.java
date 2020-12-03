package RMITest;




import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements server {
    int MSGCount = 0;
    String messages = "";
    boolean mess = false;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public void storeNewMSG(String input) throws RemoteException {
        MSGCount = 0;
        messages = messages + "\n" + input;
        mess = true;
    }

    @Override
    public String getNewMSG() throws RemoteException {
        String sol;
        if(MSGCount != 2){
            MSGCount ++;
            sol = messages;
            return sol;
        }else{
            MSGCount = 0;
            messages= "";
            mess = false;
            return messages;
        }



    }

    @Override
    public boolean isNewMSG() throws RemoteException {
        return mess;
    }


}