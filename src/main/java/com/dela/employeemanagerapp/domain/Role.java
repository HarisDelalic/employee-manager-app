package com.dela.employeemanagerapp.domain;

import com.dela.employeemanagerapp.domain.enums.RoleEnum;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleEnum name;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "role_authorities",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "authority_id", referencedColumnName = "id"
            )
    )
    private Set<Authority> authorities;

    public static Role of(RoleEnum roleEnum) {
        return Role.builder().name(roleEnum).build();
    }

    // When we send role from frontend, we are just specify roleName,
    // so objectMapper needs to know how to construct role object based on just role name
    public Role(String name) {
        this.name = RoleEnum.valueOf(name);
    }
}
