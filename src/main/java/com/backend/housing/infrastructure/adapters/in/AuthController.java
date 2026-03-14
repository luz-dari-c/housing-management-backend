package com.backend.housing.infrastructure.adapters.in;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.housing.application.dto.auth.AuthResponse;
import com.backend.housing.application.dto.auth.LoginRequest;
import com.backend.housing.application.dto.auth.RegisterRequest;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.in.properties.LoginUseCase;
import com.backend.housing.domain.ports.in.properties.RegisterUserUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
private final RegisterUserUseCase registerUserUseCase;
private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase, RegisterUserUseCase registerUserUseCase) {
        this.loginUseCase = loginUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }
    

@PostMapping("/register")
public void register(@RequestBody @Valid RegisterRequest request){
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());

    registerUserUseCase.register(user);

}

@PostMapping("/login")
public AuthResponse login (@RequestBody @Valid LoginRequest request){
    String token = loginUseCase.login(request.getEmail(), request.getPassword());

    return new AuthResponse(token);
}

}