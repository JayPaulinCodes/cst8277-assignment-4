/**
 * File:  SecurityUser.java Course materials (23S) CST 8277
 *
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author (original) Mike Norman
 * 
 * Updated by:  Group 40
 *   41024610, Jacob, Paulin
 *   041053188, Taeung, Park 
 *   041065803, Doyoung, Kim 
 *   041053986, Dawon, Jun 
 */
package acmecollege.entity;

import acmecollege.rest.serializer.SecurityRoleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import static acmecollege.entity.SecurityUser.*;

@SuppressWarnings("unused")

/**
 * User class used for (JSR-375) Java EE Security authorization/authentication
 */
@Entity
@Table(name = "security_user")
@NamedQuery(
        name = SECURITY_USER_BY_NAME_QUERY,
        query = "SELECT u FROM SecurityUser u left join fetch u.student WHERE u.username = :param1")
@NamedQuery(
        name = SECURITY_USER_BY_STUDENT_ID_QUERY,
        query = "SELECT u FROM SecurityUser u left join fetch u.student WHERE u.student.id = :param1")
public class SecurityUser implements Serializable, Principal {
    /** Explicit set serialVersionUID */
    private static final long serialVersionUID = 1L;
    public static final String SECURITY_USER_BY_NAME_QUERY = "SecurityUser.userByName";
    public static final String SECURITY_USER_BY_STUDENT_ID_QUERY = "SecurityUser.userByStudentId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    protected int id;

    @Column(name = "username", nullable = false, length = 100)
    protected String username;

    @Column(name = "password_hash", nullable = false, length = 256)
    protected String pwHash;

    @OneToOne(optional = true)
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    protected Student student;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "user_has_role",
            joinColumns = @JoinColumn(referencedColumnName = "user_id", name = "user_id"), // this entity, which is SecurityUser
            inverseJoinColumns = @JoinColumn(referencedColumnName = "role_id", name = "role_id")) // the other entity, which is SecurityRole
    protected Set<SecurityRole> roles = new HashSet<SecurityRole>();

    public SecurityUser() {
        super();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwHash() {
        return pwHash;
    }
    
    public void setPwHash(String pwHash) {
        this.pwHash = pwHash;
    }

    // TODO SU01 - Setup custom JSON serializer
    @JsonSerialize(using = SecurityRoleSerializer.class)
    public Set<SecurityRole> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<SecurityRole> roles) {
        this.roles = roles;
    }

    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    // Principal
    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        // Only include member variables that really contribute to an object's identity
        // i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
        // they shouldn't be part of the hashCode calculation
        return prime * result + Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof SecurityUser otherSecurityUser) {
            // See comment (above) in hashCode():  Compare using only member variables that are
            // truly part of an object's identity
            return Objects.equals(this.getId(), otherSecurityUser.getId());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecurityUser [id = ").append(id).append(", username = ").append(username).append("]");
        return builder.toString();
    }
    
}
