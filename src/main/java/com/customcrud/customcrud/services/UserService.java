package com.customcrud.customcrud.services;

import java.util.List;

import com.customcrud.customcrud.model.entities.User;

public interface UserService {

    List<User> findAll();

    User save(User user);

}
