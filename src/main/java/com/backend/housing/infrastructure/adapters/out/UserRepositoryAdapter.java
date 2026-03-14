
package com.backend.housing.infrastructure.adapters.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.backend.housing.domain.entity.User;
import com.backend.housing.domain.ports.out.UserRepositoryPort;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {
    
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
    
    @Override
    public Optional<User> findById(Long id) {
        return springDataUserRepository.findById(id);
    }
    
    @Override
    public List<User> findAll() {
        return springDataUserRepository.findAll();
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }
    
    @Override
    public boolean existsById(Long id) {  
        return springDataUserRepository.existsById(id);
    }
    
    @Override
    public void deleteById(Long id) {
        springDataUserRepository.deleteById(id);
    }
    
    @Override
    public List<User> findByActive(boolean active) {
        
        return springDataUserRepository.findByActive(active);
    }
    
    @Override
    public long countByRole(String roleName) {
        
        return springDataUserRepository.countByRoleName(roleName);
    }
}