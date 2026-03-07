package com.backend.housing.infrastructure.adapters.out;

import org.springframework.stereotype.Component;
import java.util.Optional;
import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort { // <--- EL NOMBRE DEBE SER ESTE
    
    private final SpringDataUserRepository springDataUserRepository; 

    public UserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public User save(User user) {
        return springDataUserRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email);
    }
}