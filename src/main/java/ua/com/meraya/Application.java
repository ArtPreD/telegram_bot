package ua.com.meraya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ua.com.meraya.database.repository.QuestionRepository;
import ua.com.meraya.database.repository.UserRepository;

@Configuration
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        QuestionRepository questionRepository = context.getBean(QuestionRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot(questionRepository, userRepository));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
