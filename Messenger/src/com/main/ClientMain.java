package com.main;

import com.chat.ChatClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ClientMain extends JFrame {
    JTextField userName;
    JButton loginButton;

    public ClientMain() {
        userName = new JTextField(10);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this::login);
        JPanel panel = new JPanel();
        panel.add(userName);
        panel.add(loginButton);
        userName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login(null);
                }
            }
        });
        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login");
        setSize(300, 100);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ClientMain();
    }

    public void login(ActionEvent event) {
        if (userName.getText() != null && !userName.getText().equals("")) {
            try {
                new ChatClient(userName.getText());
                setVisible(false);
                dispose();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }
}
