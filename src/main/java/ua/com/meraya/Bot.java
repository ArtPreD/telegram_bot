package ua.com.meraya;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.com.meraya.database.entity.MyUser;
import ua.com.meraya.database.repository.QuestionRepository;
import ua.com.meraya.database.repository.UserRepository;
import ua.com.meraya.game.ControllerCallback;
import ua.com.meraya.game.GameController;
import ua.com.meraya.info.About;
import ua.com.meraya.info.HighScore;
import ua.com.meraya.info.Rule;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private static final String USERNAME = "Merayas_bot";
    private static final String TOKEN = ***********;

    private GameController gameController;
    private ControllerCallback controllerCallback;
    private UserRepository userRepository;
    private HighScore highScore;

    public Bot (QuestionRepository questionRepository, UserRepository userRepository){
        this.userRepository = userRepository;
        this.highScore = new HighScore(userRepository);
        controllerCallback = new ControllerCallback() {
            @Override
            public void send(String chatId, String message) {
                sendMsg(chatId, message);
            }

            @Override
            public void sendMessageController(SendMessage sendMessage, boolean isGameRun) {
                try {
                    System.out.println(sendMessage.getText());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                if (!isGameRun){
                    sendMsg(sendMessage.getChatId(), "Попробуешь еще раз?");
                }
            }
        };
        gameController = new GameController(questionRepository, controllerCallback, userRepository);
    }

    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        String username = update.getMessage().getFrom().getUserName();

        if (username == null){
            username = "'тут мог быть твой ник'";
        }else {
            username = username.replace("_", "");
        }


        if (message.equals("/start")) {
            controllerCallback.send(chatId, "Привет, " + username
                    + ", поиграем в викторину? Она даже поможет чему-то научиться :)");
            return;
        }else if (message.equals("\uD83D\uDCDC Правила")) {
            controllerCallback.send(chatId, Rule.ruleMessage());
            return;
        } else if (message.equals("\uD83D\uDCDD Рекорды")) {
            controllerCallback.send(chatId, highScore.getLeaders());
            return;
        } else if (message.equals("\uD83C\uDFF7 Обо мне")) {
            controllerCallback.send(chatId, About.aboutMessage());
            return;
        }

        if (message.equals("\uD83C\uDFB2 Начать")) {
            MyUser myMyUser = userRepository.findByUserId(update.getMessage().getFrom().getId());
            if (myMyUser == null) {
                myMyUser = new MyUser();
                myMyUser.setFirstName(update.getMessage().getFrom().getFirstName());
                myMyUser.setLastName(update.getMessage().getFrom().getLastName());
                myMyUser.setUserId(update.getMessage().getFrom().getId());
                myMyUser.setUsername(update.getMessage().getFrom().getUserName());
                myMyUser.setScore(0);
                userRepository.save(myMyUser);
            }
            gameController.receiveMessage(update, myMyUser);
        }else if(message.equals("1") || message.equals("2") || message.equals("3")
                || message.equals("4") || message.equals("✅ Продолжить") || message.equals("\uD83D\uDEAB Сдаться")){
            MyUser myMyUser = userRepository.findByUserId(update.getMessage().getFrom().getId());
            gameController.receiveMessage(update, myMyUser);
    }else {
            sendMsg(update.getMessage().getChatId().toString(), "Моя твоя не понимать. Давай общаться" +
                    "по моим правилам :)");
        }
    }

    private synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow keyboardFirstRow = new KeyboardRow();
            keyboardFirstRow.add("\uD83C\uDFB2 Начать");
            keyboardFirstRow.add("\uD83D\uDCDC Правила");

            KeyboardRow keyboardSecondRow = new KeyboardRow();
            keyboardSecondRow.add("\uD83D\uDCDD Рекорды");
            keyboardSecondRow.add("\uD83C\uDFF7 Обо мне");

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(chatId);
        sendMessage.setText(s);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }
}
