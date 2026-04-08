package com.backend.housing.domain.ports.in.users;


public interface LoginUseCase {
    String login (String email, String password);
}