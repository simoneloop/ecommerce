package com.ecommerce.ecommerce.services;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.Collection;

import static com.ecommerce.ecommerce.UTI.Consts.FIELD_SORT;
import static com.ecommerce.ecommerce.UTI.Consts.PAGE_DIMENSION;

@Service
@Slf4j
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product p){
        productRepository.save(p);
        return productRepository.save(p);
    }

    public Product modifyProduct(Product p){
        Product prod=productRepository.findByName(p.getName());
        productRepository.delete(prod);
        productRepository.flush();
        return productRepository.save(p);
    }
    public void deleteProduct(Product p){
        Product prod=productRepository.findByName(p.getName());
        productRepository.delete(prod);
    }
    public Collection<Product> getAll(){
        return productRepository.findAll();
    }

    public Page<Product> getProductFilteredAndPageabled(String typo, String ordered,String page,String pageSize,String field){
        int nPage=page!=null?Integer.parseInt(page):0;
        int nDimension=pageSize!=null?Integer.parseInt(pageSize):PAGE_DIMENSION;
        PageRequest pageable;
        String f=field!=null?field:FIELD_SORT;
        log.info("pageSize"+nDimension+"ciao");
        if(ordered!=null && ordered.equals("descending")){
            pageable= PageRequest.of(nPage,nDimension).withSort(Sort.by(Sort.Direction.DESC,f));
        }
        else{
            pageable= PageRequest.of(nPage,nDimension).withSort(Sort.by(Sort.Direction.ASC,f));
        }
        Page<Product> products;
        if(typo!=null){
            products =productRepository.findByTypo(typo,pageable);
        }
        else{
            products =productRepository.findAll(pageable);
        }
        return products;
    }
}
