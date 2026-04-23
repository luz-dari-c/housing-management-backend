package com.backend.housing.domain.entity.users;

public class Rol {

    private Long id;
    private String name;

    public Rol() {}

    public Rol(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Rol(String name) {
        this.name = name;
    }

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