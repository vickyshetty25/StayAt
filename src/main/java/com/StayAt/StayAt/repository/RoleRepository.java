package com.StayAt.StayAt.repository;

import com.StayAt.StayAt.models.ERole;
import com.StayAt.StayAt.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
