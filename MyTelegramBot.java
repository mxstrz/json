package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final UI ui;
    private String chatId = "5696701037"; // Замініть на свій chat ID

    public MyTelegramBot(UI ui) {
        this.ui = ui;
        ui.setBot(this);
    }

    @Override
    public String getBotUsername() {
        return "femboy_bot"; // Замініть на ім'я вашого бота
    }

    @Override
    public String getBotToken() {
        return "8115607337:AAHe9l39Sgz0VDwCXIqSAcd1K_iE3L7C_Lw"; // Замініть на свій токен
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userMessage = update.getMessage().getText();
            String userName = update.getMessage().getFrom().getFirstName();

            ui.displayMessageFromBot(userName + ": " + userMessage);

            UserData userData = new UserData(userName, userMessage);
            DB.save(userData);

            System.out.println("Новий чат ID: " + chatId);
        } else {
            System.out.println("Отримано не текстове повідомлення");
        }
    }

    public void sendToTelegram(String text) {
        if (chatId == null || chatId.isEmpty()) {
            System.out.println("chatId відсутній. Повідомлення не надіслано.");
            return;
        }

        System.out.println("Відправлення повідомлення до Telegram: " + text);
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
            System.out.println("Повідомлення успішно надіслано!");
        } catch (TelegramApiException e) {
            System.out.println("Помилка надсилання повідомлення: " + e.getMessage());
            e.printStackTrace();
        }
    }
}