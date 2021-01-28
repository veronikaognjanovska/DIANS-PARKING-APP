package com.parkingfinder.userservice.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.parkingfinder.userservice.enumeration.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

/**
 * User database model
 * Model for User management in database
 * @author Anastasija Petrovska
 */

@Entity
@Table(name="`User`")
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @NotNull
    @Size(min = 5)
    private String name;

    @NotNull
    @Size(min = 8, message = "Лозинката мора да содржи барем 8 знаци")
    private String password;

    @Email(message = "Невалидна email адреса")
    @Size(min = 5)
    private String email;

    private boolean enabled = true;

    @Enumerated()
    private UserRole userRole = UserRole.USER;

    /**
     * Method that represents a default constructor for the User class
     */
    public User() {}

    /**
     * Method that represents a constructor with arguments for the User class
     * @param name - string that represents the name of the user
     * @param email - string that represents the email of the user
     * @param password - string that represents the password of the user
     * @param role - UserRole that represents the role of the user
     */
    public User(String name,String email,String password,UserRole role) {
        this.name=name;
        this.email=email;
        this.password=password;
        this.userRole=role;
    }

    /**
     * Method that returns a list of authorities
     * @return List - a list of user roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(userRole);
    }

    /**
     * A method that returns the username of a user
     * @return email - the email of the user
     */
    @Override
    public String getUsername() {
        return email;
    }


    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=true;

    /**
     * Method that returns if the user's account is not expired, or if it is expired
     * @return isAccountNonExpired - a boolean type which is true if the account is non expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    /**
     * Method that returns if the user's account is not locked, or if it is locked
     * @return isAccountNonLocked - a boolean type which is true if the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    /**
     * Method that returns if the credentials of the user's account are not expired
     * @return isCredentialNonExpired - a boolean type which is true if the credentials are not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    /**
     * Method which returns if the user has been enabled
     * @return isEnabled - a boolean type which is true if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}