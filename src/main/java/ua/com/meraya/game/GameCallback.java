package ua.com.meraya.game;

import org.telegram.telegrambots.api.methods.send.SendMessage;

public interface GameCallback {

    void sendMessageFromGame(SendMessage sendMessage, boolean isGameRun);

}
