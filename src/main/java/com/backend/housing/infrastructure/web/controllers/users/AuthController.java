package com.backend.housing.infrastructure.web.controllers.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.housing.application.dto.request.users.LoginRequest;
import com.backend.housing.application.dto.request.users.RegisterRequest;
import com.backend.housing.application.dto.response.auth.AuthResponse;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.LoginUseCase;
import com.backend.housing.domain.ports.in.users.RegisterUserUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    
    @Value("${app.default-profile-picture:https://ui-avatars.com/api/?name=John+Doe&background=0D8F81&color=fff&size=200}")
    private String defaultProfilePicture;

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
        user.setPhoneNumber(request.getPhoneNumber()); // NUEVO
        user.setProfilePictureUrl(defaultProfilePicture); // NUEVO - Foto por defecto

        registerUserUseCase.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request){
        String token = loginUseCase.login(request.getEmail(), request.getPassword());
        return new AuthResponse(token);
    }
}