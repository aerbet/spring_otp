package kg.zhaparov.spring_otp.repository;

import kg.zhaparov.spring_otp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
