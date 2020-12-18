package parkingfinder.config;


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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUsernamePasswordAuthenticationProvider customUsernamePasswordAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;
    public WebSecurityConfig(CustomUsernamePasswordAuthenticationProvider authenticationProvider, PasswordEncoder passwordEncoder) {
        this.customUsernamePasswordAuthenticationProvider = authenticationProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/assets/**",
                        "/css/**", "/images/**", "/register",
                        "/parking/**","/point/**").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/sign-in").permitAll()
                .loginProcessingUrl("/sign-in-post")
                .failureUrl("/sign-in-error")
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/sign-in");
                //.and()
                //.exceptionHandling().accessDeniedPage("/error");
        // koi starani ke se dostapni na neatentikuvan koristnik
        // kako greshkata ke se prikaze
        // kade ke go redirektira posle najava
        // dokolku ne smee da pristapi na dadena strana 404 error
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder.encode("1234"))
//                .authorities("USER")
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder.encode("1234"))
//                .authorities("ADMIN");
        auth.authenticationProvider(customUsernamePasswordAuthenticationProvider);
    }

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
