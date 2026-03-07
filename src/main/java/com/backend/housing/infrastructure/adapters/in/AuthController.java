package com.backend.housing.infrastructure.adapters.in;



import org.springframework.web.bind.annotation.*;

import com.backend.housing.application.dto.auth.*;
import com.backend.housing.domain.ports.in.properties.LoginUseCase;
// Cambia estas líneas:
import com.backend.housing.domain.ports.in.properties.RegisterUserUseCase; // Agregado .properties
// Si no tienes una carpeta 'auth', el Login debe estar en properties o en la raíz de in


import com.backend.housing.domain.entity.User;

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