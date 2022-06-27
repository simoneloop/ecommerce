package com.ecommerce.ecommerce.services;

import com.ecommerce.ecommerce.entities.Purchase;
import com.ecommerce.ecommerce.repositories.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Collection<Purchase> getAllPurchase(){
        return purchaseRepository.findAll();
    }

    public void setPurchaseDone(Long id,Boolean done){
        Purchase p=purchaseRepository.findById(id);
        p.setDone(done);
        purchaseRepository.flush();
        return;
    }
}
