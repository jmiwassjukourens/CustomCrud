package com.andres.curso.springboot.app.springbootcrud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andres.curso.springboot.app.springbootcrud.entities.Role;
import com.andres.curso.springboot.app.springbootcrud.entities.User;
import com.andres.curso.springboot.app.springbootcrud.repositories.RoleRepository;
import com.andres.curso.springboot.app.springbootcrud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {

        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return repository.existsByUsername(username);
    }
    
    @Override
    @Transactional
    public void delete(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!repository.existsById(user.getId())) {
            throw new RuntimeException("User does not exist and cannot be deleted");
        }
        repository.delete(user);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be a valid positive number");
        }
        if (!repository.existsById(id)) {
            throw new RuntimeException("No user found with ID: " + id);
        }
        repository.deleteById(id);
    }
    

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User do not found with ID: " + id));
    }
    
@Override
@Transactional
public User update(Long id, User userDetails) {
    User existingUser = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

    // Update only necessary fields
    existingUser.setUsername(userDetails.getUsername());
    existingUser.setRoles(userDetails.getRoles());
    existingUser.setPassword(userDetails.getPassword());
 
    return repository.save(existingUser); // This will update the existing user
}
    
}
