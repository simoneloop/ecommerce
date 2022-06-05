package com.ecommerce.ecommerce.repositories;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.ProductInPurchase;
import com.ecommerce.ecommerce.entities.Purchase;
import com.ecommerce.ecommerce.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInPurchaseRepository extends JpaRepository<ProductInPurchase,Integer> {
    ProductInPurchase findByBuyerAndBuyed(Users u, Product p);
}
