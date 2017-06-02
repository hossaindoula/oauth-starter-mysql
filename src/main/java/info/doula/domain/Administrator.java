package info.doula.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * Created by saad on 9/30/2016.
 */

@Entity
@Table(name = "administrators")
public class Administrator implements Serializable {

    private static final long serialVersionUID = 6334339049654769188L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Length(min = 1, max = 127)
    private String firstName;

    @Length(min = 1, max = 127)
    private String lastName;

    @NotNull
    @Length(min = 3, max = 255)
    private String email;


    @Length(min = 5, max = 100)
    private String comment;

    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "administrators_id"), inverseJoinColumns = @JoinColumn(name = "administrators_role_id"))
    private List<AdministratorRole> roles;

    public Administrator() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<AdministratorRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AdministratorRole> roles) {
        this.roles = roles;
    }
}
