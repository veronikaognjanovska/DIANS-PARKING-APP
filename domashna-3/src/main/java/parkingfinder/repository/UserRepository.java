package parkingfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import parkingfinder.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    @Modifying
    @Query("update User u set u.name = ?1, u.email= ?2 where u.password = ?3")
    void updateUserById(String name, String email, String password);



}

