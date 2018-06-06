package ua.com.meraya.game;

import org.telegram.telegrambots.api.methods.send.SendMessage;

public interface ControllerCallback {

    void send(String chatId, String message);
    void sendMessageController(SendMessage sendMessage, boolean isGameRun);
}
