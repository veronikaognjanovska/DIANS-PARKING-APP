package com.parkingfinder.userservice.repository;

import com.parkingfinder.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for finding users from the database
 * @author Anastasija Petrovska
 */

public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     * Method  that finds a user by email, if the user exists
     * @param email - string that represents the user's email
     * @return optional User, if it can be found in the database
     */
    Optional<User> findByEmail(String email);

    /**
     * Method that updates the user by id
     * @param name - string that reperesents the user's name
     * @param email - string that represents the user's e-mail
     * @param password - string that represents the user's password
     */
    @Modifying
    @Query("update User u set u.name = ?1, u.email= ?2 where u.password = ?3")
    void updateUserById(String name, String email, String password);

}
