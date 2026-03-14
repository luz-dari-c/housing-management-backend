package com.backend.housing.infrastructure.adapters.in;

import org.springframework.web.bind.annotation.*;
import com.backend.housing.application.dto.auth.*;
import com.backend.housing.domain.ports.in.properties.LoginUseCase;
import com.backend.housing.domain.ports.in.properties.RegisterUserUseCase;
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
        
        
        user.setPrimerNombre(request.getPrimerNombre());
        user.setSegundoNombre(request.getSegundoNombre());
        user.setPrimerApellido(request.getPrimerApellido());
        user.setSegundoApellido(request.getSegundoApellido());
        user.setEmail(request.getEmail());
        user.setCedula(request.getCedula());
        user.setEdad(request.getEdad());
        user.setPassword(request.getPassword());

        registerUserUseCase.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request){
        String token = loginUseCase.login(request.getEmail(), request.getPassword());
        return new AuthResponse(token);
    }
}