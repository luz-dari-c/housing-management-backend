package com.backend.housing.infrastructure.web.controllers.users;



import com.backend.housing.application.dto.request.users.LoginRequest;
import com.backend.housing.application.dto.request.users.RegisterRequest;
import com.backend.housing.application.dto.response.auth.AuthResponse;
import com.backend.housing.domain.entity.users.User;
import com.backend.housing.domain.ports.in.users.LoginUseCase;
import com.backend.housing.domain.ports.in.users.RegisterUserUseCase;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
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