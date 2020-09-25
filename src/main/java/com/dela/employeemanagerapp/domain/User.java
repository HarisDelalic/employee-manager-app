package com.dela.employeemanagerapp.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String email;
    private String profileImageUrl;
    private LocalDate lastLoginDate;
    private LocalDate lastLoginDateDisplay;
    private LocalDate joinDate;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            )
    )
    private Set<Role> roles;
    private boolean isActive;
    private boolean isLocked;

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
