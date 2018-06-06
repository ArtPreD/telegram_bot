package ua.com.meraya.game.modules;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MessageGenerator {

    public SendMessage generateMessage(String chatId, String message, boolean isQuestionRunning){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if (isQuestionRunning){
            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow keyboardFirstRow = new KeyboardRow();
            keyboardFirstRow.add("\u0031");
            keyboardFirstRow.add("\u0032");

            KeyboardRow keyboardSecondRow = new KeyboardRow();
            keyboardSecondRow.add("\u0033");
            keyboardSecondRow.add("\u0034");

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
        }else {
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardFirstRow = new KeyboardRow();

            keyboardFirstRow.add("✅ Продолжить");
            keyboardFirstRow.add("\uD83D\uDEAB Сдаться");

            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
        }

        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }
}
