package com.parkingfinder.webapp.config;

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
 * Web security configuration
 * */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider, PasswordEncoder passwordEncoder) {
        this.customUsernamePasswordAuthenticationProvider = customUsernamePasswordAuthenticationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Method for setting up authentication for endpoints
     * @param http - HttpSecurity object security builder
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/assets/**",
                        "/css/**", "/images/**", "/register/**", "/login**",
                        "/parking/**", "/point**","/route/**", "/authentication/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/login-error")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login");
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
