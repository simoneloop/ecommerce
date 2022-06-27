package com.ecommerce.ecommerce.repositories;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.ProductInPurchase;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Collection;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByNameAndEnabled(String name,boolean enabled);

    Product findByName(String name);
    Page<Product>findByTypoAndEnabled(String typo, PageRequest pageable,boolean enabled);
    Collection<Product> findByHotAndEnabled(Boolean isHot,boolean enabled);
    Collection<Product> findByEnabled(boolean enabled);


}
