package com.backend.housing.infrastructure.persistence.entities.users;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public RolEntity() {}

    public RolEntity(String name) {
        this.name = name;
    }

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}