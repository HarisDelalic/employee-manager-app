package com.dela.employeemanagerapp.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
