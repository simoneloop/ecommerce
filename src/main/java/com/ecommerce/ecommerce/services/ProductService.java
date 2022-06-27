package com.ecommerce.ecommerce.services;

import com.ecommerce.ecommerce.UTI.Support;
import com.ecommerce.ecommerce.UTI.exception.*;
import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.ProductInPurchase;
import com.ecommerce.ecommerce.entities.Purchase;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.repositories.ProductInPurchaseRepository;
import com.ecommerce.ecommerce.repositories.ProductRepository;
import com.ecommerce.ecommerce.repositories.PurchaseRepository;
import com.ecommerce.ecommerce.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.ecommerce.ecommerce.UTI.Consts.FIELD_SORT;
import static com.ecommerce.ecommerce.UTI.Consts.PAGE_DIMENSION;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;

    public Product addProduct(Product p){
        if(productRepository.findByNameAndEnabled(p.getName(),true)!=null)throw new ProductAlreadyExistException();
        productRepository.save(p);
        return productRepository.save(p);
    }


    @Transactional()
    public Product modifyProduct(Product p,String oldName){
        Product prod=productRepository.findByNameAndEnabled(oldName,true);
        prod.setName(p.getName());
        prod.setDescription(p.getDescription());
        prod.setPrice(p.getPrice());
        prod.setTypo(p.getTypo());
        prod.setQuantity(p.getQuantity());
        prod.setUrlPropic(p.getUrlPropic());
        log.info(""+p.isHot());
        prod.setHot(p.isHot());

        return prod;
    }
    public Collection<Product> getAll(){
        return productRepository.findByEnabled(true);
    }
    public Product getProduct(String name) {
        Product prod = productRepository.findByNameAndEnabled(name,true);
        if (prod != null) {
            return prod;
        }
        throw new ProductDoesNotExistException();
    }
    public Page<Product> getProductFilteredAndPageabled(String typo, String ordered,String page,String pageSize,String field){
        int nPage=page!=null?Integer.parseInt(page):0;
        int nDimension=pageSize!=null?Integer.parseInt(pageSize):PAGE_DIMENSION;
        PageRequest pageable;
        String f=field!=null?field:FIELD_SORT;
        if(ordered!=null && ordered.equals("descending")){
            pageable= PageRequest.of(nPage,nDimension).withSort(Sort.by(Sort.Direction.DESC,f));
        }
        else{
            pageable= PageRequest.of(nPage,nDimension).withSort(Sort.by(Sort.Direction.ASC,f));
        }
        Page<Product> products;
        if(typo!=null){
            products =productRepository.findByTypoAndEnabled(typo,pageable,true);
        }
        else{
            products =productRepository.findAll(pageable);
        }
        return products;
    }
    @Transactional()//basta lanciare una RuntimeException e triggera il rollback
    public void buyProduct(String email,String productName,String quantity) throws Exception {
        int qty=Integer.parseInt(quantity);
        Product p= productRepository.findByNameAndEnabled(productName,true);
        if(p==null){
            throw new ProductDoesNotExistException();
        }
        float amountToPay=qty*p.getPrice();
        int newQuantity=p.getQuantity()-qty;

        if(newQuantity<0){
            throw new QuantityProductUnavailableException();
        }
        Users user=userRepository.findByEmail(email);
        if(user==null){
            throw new UserDoesNotExistException();
        }
        Purchase purchase=new Purchase(null,null,user,p,qty,false);
        purchaseRepository.save(purchase);
        Support.validateCreditLimit(email,amountToPay);//DA SOSTITUIRE CON IL PAGAMENTO
        p.setQuantity(newQuantity);
        productRepository.flush();
    }
    public void modifyHotProduct(Map<String,Boolean> map){
        map.forEach((k,v)->{
            Product prod=productRepository.findByNameAndEnabled(k,true);
            prod.setHot(v);
        });
        productRepository.flush();

    }

    @Transactional()
    public void buyProduct(Users user,String productName,int quantity) throws RuntimeException {

        Product p= productRepository.findByNameAndEnabled(productName,true);
        if(p==null){
            throw new ProductDoesNotExistException();
        }
        int newQuantity=p.getQuantity()-quantity;

        if(newQuantity<0){
            throw new QuantityProductUnavailableException();
        }
        Purchase purchase=new Purchase(null,null,user,p,quantity,false);
        purchaseRepository.save(purchase);

        p.setQuantity(newQuantity);
        ProductInPurchase pipTmp=productInPurchaseRepository.findByBuyerAndBuyed(user,p);
        productInPurchaseRepository.delete(pipTmp);
        productRepository.flush();
    }

    @Transactional()
    public void buyMyCart(String email){
        Users user=userRepository.findByEmail(email);
        if(user==null)throw new UserDoesNotExistException();
        if(user.getShoppingCart().size()==0)throw new CartIsEmptyException();
        AtomicReference<Float> amountToPay= new AtomicReference<>(0.0F);
        user.getShoppingCart().forEach(pip-> amountToPay.updateAndGet(v -> new Float((float) (v + pip.getQuantity() * pip.getBuyed().getPrice()))));
        Support.validateCreditLimit(user.getEmail(),amountToPay.get());//DA SOSTITUIRE CON IL PAGAMENTO
        user.getShoppingCart().forEach(pip->buyProduct(user,pip.getBuyed().getName(),pip.getQuantity()));
    }
    public void deleteProducts(Collection<String>products){
        products.forEach(p->{
            Product prod=productRepository.findByNameAndEnabled(p,true);
            prod.setEnabled(false);
            productRepository.flush();

        });

    }

    public Collection<Product> getAllHotProducts(boolean isHot){
        return productRepository.findByHotAndEnabled(isHot,true);
    }
}
