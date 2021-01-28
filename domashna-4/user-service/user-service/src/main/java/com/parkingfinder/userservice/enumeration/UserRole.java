package com.parkingfinder.userservice.enumeration;

import org.springframework.security.core.GrantedAuthority;

/**
 * User roles that can be used
 * @author Anastasija Petrovska
 */
public enum UserRole implements GrantedAuthority {
    /**
     * Admin role
     */
    ADMIN,
    /**
     * User role
     */
    USER;

    /**
     * Method that gets the authority
     * @return name - name with the authority
     */
    @Override
    public String getAuthority() {
        return name();
    }
}