

package parkingfinder.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import parkingfinder.enumeration.UserRole;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;


@Entity
@Table(name="`User`")
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private String name;
    private String password;
    private String email;

    private Boolean enabled = true;

    //    @Builder.Default
    @Enumerated()
    private UserRole userRole = UserRole.USER;

    //@OneToMany
    //private List<RouteMatcher.Route> lastFiveLocations;
    public User() {}
    public User(String name,String email,String password,UserRole role) {
        this.name=name;
        this.email=email;
        this.password=password;
        this.userRole=role;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(userRole);
    }

    @Override
    public String getUsername() {
        return email;
    }


    private boolean isAccountNonExpired=true;
    private boolean isAccountNonLocked=true;
    private boolean isCredentialsNonExpired=true;
    private boolean isEnabled=true;

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}

