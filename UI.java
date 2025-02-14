package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UI extends JFrame {
    private final JPanel chatPanel;
    private final JTextField inputField;
    private int y;
    private MyTelegramBot myTelegramBot;

    public UI() {
        setTitle("Чат Бот");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        chatPanel = new JPanel();
        chatPanel.setBackground(Color.BLACK);
        chatPanel.setLayout(null);
        chatPanel.setPreferredSize(new Dimension(400, 500));

        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.BLACK);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setBackground(Color.DARK_GRAY);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputField.setPreferredSize(new Dimension(300, 30));

        JButton sendButton = new JButton("Відправити");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        List<UserData> messages = DB.read();
        for (UserData userData : messages) {
            addMessage(userData.userName + ": " + userData.message);
        }

        setVisible(true);
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            String userName = "Ви";
            UserData userData = new UserData(userName, text);
            DB.save(userData);
            addMessage(userName + ": " + text);

            if (myTelegramBot != null) {
                myTelegramBot.sendToTelegram(text);
            } else {
                System.out.println("Бот не підключений.");
            }

            inputField.setText("");
        }
    }

    private void addMessage(String text) {
        JLabel messageLabel = new JLabel(text);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setBounds(10, y, 380, 30);
        chatPanel.add(messageLabel);
        chatPanel.revalidate();
        chatPanel.repaint();
        y += 35;
    }

    public void setBot(MyTelegramBot bot) {
        this.myTelegramBot = bot;
    }

    public void displayMessageFromBot(String message) {
        addMessage("Бот: " + message);
    }
}