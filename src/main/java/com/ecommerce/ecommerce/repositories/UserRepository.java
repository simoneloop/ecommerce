package com.ecommerce.ecommerce.repositories;

import com.ecommerce.ecommerce.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByEmail(String email);
    boolean existsByEmail(String email);

}
