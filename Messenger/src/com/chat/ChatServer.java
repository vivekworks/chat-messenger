package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    static ArrayList<Socket> clientSockets;
    static ArrayList<String> logins;

    public ChatServer() {
        clientSockets = new ArrayList<>();
        logins = new ArrayList<>();
        try {
            ServerSocket serverSock = new ServerSocket(8400);
            while (true) {
                Socket client = serverSock.accept();
                new AcceptClient(client);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public class AcceptClient extends Thread {
        Socket clientSocket;
        DataInputStream dataIn;
        DataOutputStream dataOut;

        AcceptClient(Socket client) {
            clientSocket = client;
            try {
                dataIn = new DataInputStream(client.getInputStream());
                dataOut = new DataOutputStream(client.getOutputStream());
                clientSockets.add(clientSocket);
                logins.add(dataIn.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
            start();
        }

        public void run() {
            while (true) {
                try {
                    String clientMsg = dataIn.readUTF();
                    String[] messageTokens = clientMsg.split("_");
                    for (Socket client : clientSockets)
                        new DataOutputStream(client.getOutputStream()).writeUTF(messageTokens[0] + " " + messageTokens[2]);
                    if (messageTokens[1].equals("LOGOUT")) {
                        clientSockets.remove(logins.indexOf(messageTokens[0]));
                        logins.remove(messageTokens[0]);
                    }
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        }
    }
}