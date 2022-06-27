package com.ecommerce.ecommerce.repositories;

import com.ecommerce.ecommerce.entities.Purchase;
import com.ecommerce.ecommerce.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {
    Collection<Purchase> findByBuyer(Users u);
    Collection<Purchase> findByDone(boolean done);
    Purchase findById(Long id);
}
