package info.doula.dto;

import info.doula.domain.Administrator;
import info.doula.domain.AdministratorRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-05-25.
 */

public class UserBean implements UserDetails, Serializable {

    private static final long serialVersionUID = 4512638469899712510L;

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserBean(){}

    public UserBean(Administrator administrator) {
        username = administrator.getEmail();
        password = administrator.getPassword();
        authorities = translate(administrator.getRoles());
    }

    /**
     * Translates the List<Role> to a List<GrantedAuthority>
     * @param roles the input list of roles.
     * @return a list of granted authorities
     */
    private Collection<? extends GrantedAuthority> translate(List<AdministratorRole> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AdministratorRole role : roles) {
            String name = role.getRole().toUpperCase();
            //Make sure that all roles start with "ROLE_"
            if (!name.startsWith("ROLE_"))
                name = "ROLE_" + name;
            authorities.add(new SimpleGrantedAuthority(name));
        }
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

}
