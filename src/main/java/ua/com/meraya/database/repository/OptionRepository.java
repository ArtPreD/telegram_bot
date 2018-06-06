package ua.com.meraya.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.meraya.database.entity.Option;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Option findById(long id);
}
