package com.ecommerce.ecommerce.repositories;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByName(String name);
    Collection<Product> findByTypo(String typo);
    Page<Product>findByTypo(String typo, PageRequest pageable);
    Collection<Product> findByHot(Boolean isHot);

}
