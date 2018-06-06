package ua.com.meraya.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.meraya.database.entity.MyUser;

public interface UserRepository extends JpaRepository<MyUser, Long>{
    MyUser findByUserId(int userId);
}
