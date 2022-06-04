package com.ecommerce.ecommerce.services;

import com.ecommerce.ecommerce.UTI.Support;
import com.ecommerce.ecommerce.UTI.exception.ProductDoesNotExistException;
import com.ecommerce.ecommerce.UTI.exception.QuantityProductUnavailableException;
import com.ecommerce.ecommerce.UTI.exception.UserDoesNotExistException;
import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.Purchase;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.repositories.ProductRepository;
import com.ecommerce.ecommerce.repositories.PurchaseRepository;
import com.ecommerce.ecommerce.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.Date;

import static com.ecommerce.ecommerce.UTI.Consts.FIELD_SORT;
import static com.ecommerce.ecommerce.UTI.Consts.PAGE_DIMENSION;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Product addProduct(Product p){
        productRepository.save(p);
        return productRepository.save(p);
    }

    //TODO versioning
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
    @Transactional()//basta lanciare una RuntimeException e triggera il rollback
    public void buyProduct(String email,String productName,String quantity) throws Exception {
        int qty=Integer.parseInt(quantity);
        Product p= productRepository.findByName(productName);
        if(p==null){
            throw new ProductDoesNotExistException();
        }
        float amountToPay=p.getQuantity()*p.getPrice();
        int newQuantity=p.getQuantity()-Integer.parseInt(quantity);

        if(newQuantity<0){
            throw new QuantityProductUnavailableException();
        }
        Users user=userRepository.findByEmail(email);
        if(user==null){
            throw new UserDoesNotExistException();
        }
        Purchase purchase=new Purchase(null,null,user,p,qty);
        purchaseRepository.save(purchase);
        Support.validateCreditLimit(email,amountToPay);//DA SOSTITUIRE CON IL PAGAMENTO
        p.setQuantity(newQuantity);
        productRepository.flush();
    }

    public Collection<Product> getAllHotProducts(boolean isHot){
        return productRepository.findByHot(isHot);
    }
}
