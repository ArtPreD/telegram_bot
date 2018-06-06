package ua.com.meraya.game;

import org.telegram.telegrambots.api.objects.Update;
import ua.com.meraya.database.entity.Option;
import ua.com.meraya.database.entity.Question;
import ua.com.meraya.database.entity.MyUser;
import ua.com.meraya.database.repository.QuestionRepository;
import ua.com.meraya.database.repository.UserRepository;
import ua.com.meraya.game.modules.GameHandler;
import ua.com.meraya.game.modules.MessageGenerator;
import ua.com.meraya.game.modules.AnswerValidator;
import ua.com.meraya.game.modules.QuestionListCreator;

import java.util.Comparator;
import java.util.List;

public class Game {

    private GameCallback gameCallback;
    private UserRepository userRepository;

    private AnswerValidator answerValidator;
    private MessageGenerator generator;
    private QuestionListCreator questionListCreator;
    private GameHandler gameHandler;

    private int attempts, score, multiplier, questionQuantity;
    private boolean isGameRunning, isQuestionRunning;

    private Question[] questions;
    private Question question;
    private List<Option> options;
    private Option[] optionsArray;
    private MyUser myMyUser;

    public Game(QuestionRepository questionRepository, GameCallback gameCallback, MyUser myMyUser,
                UserRepository userRepository, GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.userRepository = userRepository;
        this.gameCallback = gameCallback;
        this.answerValidator = new AnswerValidator();
        this.generator = new MessageGenerator();
        this.myMyUser = myMyUser;
        this.questionListCreator = new QuestionListCreator(questionRepository);
    }

    public void initGame(Update update) {
        questions = questionListCreator.createQuestionList();
        score = 0;
        attempts = 3;
        multiplier = 1;
        questionQuantity = 0;

        isGameRunning = true;
        game(update);
        isQuestionRunning = true;
    }

    public void game(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();

        if (text.equals("✅ Продолжить")) {
            isQuestionRunning = true;
            askQuestion(chatId);
            return;
        } else if (text.equals("\uD83D\uDEAB Сдаться")) {
            isGameRunning = false;
            isQuestionRunning = false;
            String message = "Что ж, это твой выбор. Ты заработал " + score + " очков.";
            gameCallback.sendMessageFromGame(generator.generateMessage(chatId, message,
                    isQuestionRunning), isGameRunning);
            if (myMyUser.getScore() < score){
                myMyUser.setScore(score);
                userRepository.save(myMyUser);
            }
            return;
        } else if (text.equals("1") || text.equals("2") || text.equals("3") || text.equals("4")) {
            if (answerValidator.validateAnswer(question, text, optionsArray)) {
                isQuestionRunning = false;
                gameCallback.sendMessageFromGame(generator.generateMessage(chatId, "Правильно! Идем дальше?",
                        isQuestionRunning), isGameRunning);
                score += 10 * multiplier;
                multiplier++;
                return;
            } else {
                if (attempts != 1) {
                    attempts--;
                    gameCallback.sendMessageFromGame(generator.generateMessage(chatId, "Не правильно, " +
                            "попробуй еще раз. " +
                            "У тебя осталось " + attempts + " попытки", isQuestionRunning), isGameRunning);
                    multiplier = 1;
                } else {
                    isQuestionRunning = false;
                    isGameRunning = false;
                    gameCallback.sendMessageFromGame(generator.generateMessage(chatId, "К сожалению ты " +
                            "проиграл. Ты заработал "
                            + score + " очков", isQuestionRunning), isGameRunning);
                    if (myMyUser.getScore() < score){
                        myMyUser.setScore(score);
                        userRepository.save(myMyUser);
                    }
                    return;
                }
            }
        }

        if (!isQuestionRunning) {
            isQuestionRunning = true;
            askQuestion(chatId);
        }
    }

    private void askQuestion(String chatId) {
        if (questionQuantity != questions.length) {
            question = questions[questionQuantity];
            options = question.getOptions();
            options.sort(new Comparator<Option>() {
                @Override
                public int compare(Option o1, Option o2) {
                    return (int) (o1.getId() - o2.getId());
                }
            });
            optionsArray = options.toArray(new Option[options.size()]);
            gameCallback.sendMessageFromGame(generator.generateMessage(chatId, question.getQuestion(),
                    isQuestionRunning), isGameRunning);

            String optionMessage = "";
            optionMessage = optionMessage.concat("Итак, варианты следующие: \n");
            int i = 1;
            for (Option option : optionsArray) {
                optionMessage = optionMessage.concat(i + ". " + option.getOption() + "\n");
                i++;
            }
            questionQuantity++;
            gameCallback.sendMessageFromGame(generator.generateMessage(chatId, optionMessage, isQuestionRunning),
                    isGameRunning);
        }else {
            isQuestionRunning = false;
            isGameRunning = false;
            gameCallback.sendMessageFromGame(generator.generateMessage(chatId, "Все, вопросы кончились. " +
                    "И ты победил! \uD83C\uDF7E \uD83C\uDF89 Зааработано " + score + " очков!", isQuestionRunning),
                    isGameRunning);
            if (myMyUser.getScore() < score){
                myMyUser.setScore(score);
                userRepository.save(myMyUser);
            }
        }
    }
}
