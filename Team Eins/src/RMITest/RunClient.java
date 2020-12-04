package RMITest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {
    private static JTextField MSGfield;
    private static RMIClient client;

    public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
        System.setProperty("java.security.policy","file:///tmp/test.policy");
        String name = "client1";

        Timer timer;

        client = new RMIClient();

        JFrame frame = new JFrame(name);
        frame.setSize(800,500);

        JButton sendButton = new JButton("send");
        JTextField textfield = new JTextField();
        MSGfield = new JTextField();

        frame.add(sendButton, BorderLayout.SOUTH);
        frame.add(textfield, BorderLayout.NORTH);
        frame.add(MSGfield, BorderLayout.CENTER);


        frame.setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.storeNewMSG(name +": " +textfield.getText());
                    textfield.setText("");

                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        });
        timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pullMSG();
            }
        });

        timer.start();


    }

    private static void pullMSG() {
        try {
            if(client.serverNewMSG()){
                MSGfield.setText(client.getNewMSG());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
