package com.dela.employeemanagerapp.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
