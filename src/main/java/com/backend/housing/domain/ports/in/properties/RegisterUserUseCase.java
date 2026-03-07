package com.backend.housing.domain.ports.in.properties;

import com.backend.housing.domain.entity.User;

public interface RegisterUserUseCase {

    User register(User user);

}