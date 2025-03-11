package com.app.springbootcrud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.springbootcrud.entities.Role;
import com.app.springbootcrud.entities.User;
import com.app.springbootcrud.repositories.RoleRepository;
import com.app.springbootcrud.repositories.UserRepository;

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
        System.out.println("pasa por aqui 4");
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        System.out.println("pasa por aqui 5");
        System.out.println(existingUser.getId());
        System.out.println(existingUser.getUsername());
        System.out.println(existingUser.getRoles());
        
        // Actualizar campos necesarios
        existingUser.setUsername(userDetails.getUsername());
        
        // Gestión de roles: se asigna siempre el rol "ROLE_USER"
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRoleUser.ifPresent(roles::add);
        existingUser.setRoles(roles);
        
        System.out.println("pasa por aqui 6");
        // Si lo necesitas, puedes actualizar la contraseña u otros campos
        // existingUser.setPassword(userDetails.getPassword());
    
        return repository.save(existingUser);
    }
    
    
}
