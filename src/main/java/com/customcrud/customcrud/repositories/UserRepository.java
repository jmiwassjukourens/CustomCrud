package com.customcrud.customcrud.repositories;

import org.springframework.data.repository.CrudRepository;
import com.customcrud.customcrud.model.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
