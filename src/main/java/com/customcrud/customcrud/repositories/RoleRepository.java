package com.customcrud.customcrud.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.customcrud.customcrud.model.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
