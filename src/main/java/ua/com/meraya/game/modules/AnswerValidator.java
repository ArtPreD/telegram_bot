package ua.com.meraya.game.modules;

import ua.com.meraya.database.entity.Option;
import ua.com.meraya.database.entity.Question;

public class AnswerValidator {

    public boolean validateAnswer(Question question, String answer, Option[] optionsArray){
        Option option = optionsArray[Integer.parseInt(answer) -1];
        return question.getAnswer().getOption().equals(option.getOption());
    }
}
