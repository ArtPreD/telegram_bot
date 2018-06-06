package ua.com.meraya.game;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import ua.com.meraya.database.entity.MyUser;
import ua.com.meraya.database.repository.QuestionRepository;
import ua.com.meraya.database.repository.UserRepository;
import ua.com.meraya.game.modules.GameHandler;

public class GameController {

    private ControllerCallback controllerCallback;
    private boolean isGameRunning;
    private Game game;
    private QuestionRepository questionRepository;
    private UserRepository userRepository;
    private GameHandler gameHandler;
    private User user;
    private MyUser myMyUser;


    public GameController(QuestionRepository questionRepository, ControllerCallback controllerCallback,
                          UserRepository userRepository) {
        this.userRepository = userRepository;
        this.controllerCallback = controllerCallback;
        this.questionRepository = questionRepository;
        gameHandler = new GameHandler();
    }

    private GameCallback gameCallback = new GameCallback() {
        @Override
        public void sendMessageFromGame(SendMessage sendMessage, boolean isGameRun) {
            controllerCallback.sendMessageController(sendMessage, isGameRun);
        }
    };

    public void receiveMessage(Update update, MyUser myMyUser) {
        this.myMyUser = myMyUser;
        gameControl(update);
    }

    private void gameControl(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();

      if (message.equals("\uD83C\uDFB2 Начать")) {
            controllerCallback.send(chatId, "Поехали, первый вопрос... \n");

            user = update.getMessage().getFrom();
            game = gameHandler.findGameByUser(user.getId());
            if (game == null) {
                game = new Game(questionRepository, gameCallback, myMyUser, userRepository, gameHandler);
                gameHandler.addGame(user.getId(), game);
            }
            isGameRunning = true;
            game.initGame(update);
            return;
        }

        game = gameHandler.findGameByUser(user.getId());
        game.game(update);
    }
}
