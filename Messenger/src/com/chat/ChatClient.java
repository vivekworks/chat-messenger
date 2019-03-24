package com.chat;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends JFrame implements Runnable {
    Socket socket;
    JTextArea textArea;
    Thread thread;
    DataInputStream dataIn;
    DataOutputStream dataOut;
    String loginName;
    JButton sendButton, logoutButton;
    JTextField textField;

    public ChatClient(String login) throws IOException {
        super(login);
        loginName = login;
        textArea = new JTextArea(10, 20);
        socket = new Socket("localhost", 8400);
        dataIn = new DataInputStream(socket.getInputStream());
        dataOut = new DataOutputStream(socket.getOutputStream());
        dataOut.writeUTF(loginName);
        dataOut.writeUTF(loginName + "_LOGIN_has logged in.");
        textField = new JTextField(20);
        logoutButton = new JButton("Logout");
        sendButton = new JButton("Send");
        thread = new Thread(this);
        thread.start();
        setup();
    }

    void setup() {
        setSize(300, 300);
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        panel.add(textField);
        panel.add(sendButton);
        panel.add(logoutButton);
        getContentPane().add(panel);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendChat(null);
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dataOut.writeUTF(loginName + "_LOGOUT_has logged out.");
                } catch (IOException o) {
                    o.printStackTrace();
                }
            }
        });
        sendButton.addActionListener(this::sendChat);
        logoutButton.addActionListener((event) -> {
            try {
                dataOut.writeUTF(loginName + "_LOGOUT_has logged out.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                textArea.append("\n" + dataIn.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void sendChat(ActionEvent event) {
        if (textField.getText() != null && !textField.getText().equals("")) {
            try {
                dataOut.writeUTF(loginName + ": _DATA_ " + textField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
            textField.setText("");
        }
    }

}
