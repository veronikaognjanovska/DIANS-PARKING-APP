package com.parkingfinder.userservice.config;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.WebRequest;

/**
 * Web Security Configuration that enables web security throughout the application
 * @extends WebSecurityConfigurerAdapter class
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for the WebSecurityClass
     * @param authenticationProvider - object of the class CustomUsernamePasswordAuthenticationProvider
     * @param passwordEncoder - object of the class PasswordEncoder
     */
    public WebSecurityConfig(CustomUsernamePasswordAuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder) {
        this.customUsernamePasswordAuthenticationProvider = authenticationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method that is responsible for configuration of web security
     * @param http - object of class HttpSecurity
     * @throws Exception - throws exception if any of the requests are invalid
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/register").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/sign-in")
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    /**
     * Method that is responsible for configuration and providing authentication
     * @param auth - object of class AuthenticationManagerBuilder
     * @throws Exception if any of the requests are invalid
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customUsernamePasswordAuthenticationProvider);
    }

    /**
     * Method that returns any error attributes while dealing with web requests
     * @return null if there are errors in the web requests
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new ErrorAttributes() {
            @Override
            public Throwable getError(WebRequest webRequest) {
                return null;
            }
        };
    }

}