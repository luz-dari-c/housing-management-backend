package com.backend.housing.domain.ports.in.users;


import com.backend.housing.domain.entity.users.User;

public interface RegisterUserUseCase {

    User register(User user);

}