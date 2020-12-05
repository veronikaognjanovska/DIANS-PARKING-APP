package parkingapp.app.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.RouteMatcher;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Entity
@Data
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer ID;

    private String userName;
    private String password;
    private String email;

    private Boolean enabled = true;

    //@OneToMany
    //private List<RouteMatcher.Route> lastFiveLocations;

    @Builder.Default
    private UserRole userRole = UserRole.USER;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}