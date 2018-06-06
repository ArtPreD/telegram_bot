package ua.com.meraya.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.meraya.database.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findById(long id);
}
