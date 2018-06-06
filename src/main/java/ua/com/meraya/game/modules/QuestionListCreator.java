package ua.com.meraya.game.modules;

import ua.com.meraya.database.entity.Question;
import ua.com.meraya.database.repository.QuestionRepository;

import java.util.Collections;
import java.util.List;

public class QuestionListCreator {

    private final QuestionRepository questionRepository;

    public QuestionListCreator(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question[] createQuestionList(){
        List<Question> list = questionRepository.findAll();
        Collections.shuffle(list);
        return list.toArray(new Question[list.size()]);
    }
}
