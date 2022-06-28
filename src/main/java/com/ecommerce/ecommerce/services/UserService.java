package com.ecommerce.ecommerce.services;

import com.ecommerce.ecommerce.UTI.exception.ProductDoesNotExistException;
import com.ecommerce.ecommerce.UTI.exception.QuantityProductUnavailableException;
import com.ecommerce.ecommerce.UTI.exception.UserAlreadyExistException;
import com.ecommerce.ecommerce.UTI.exception.UserDoesNotExistException;
import com.ecommerce.ecommerce.entities.*;
import com.ecommerce.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ProductInPurchaseRepository productInPurchaseRepository;

    @Autowired
    private final PurchaseRepository purchaseRepository;

    private final PasswordEncoder passwordEncoder;



    public Users saveUser(Users user) throws RuntimeException {
        /*
            TODO controlli su i dati inseriti
         */

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException();
        }
        log.info("son qua");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role=roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);
        log.info("Saving new user {} to the database",user.getEmail());
        return userRepository.save(user);
    }

    public Role saveRole(Role role){
        log.info("Saving new role {} to the database",role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String email,String roleName){
        log.info("Adding new role {} to the user {}",roleName,email);
        Users user=userRepository.findByEmail(email);
        Role role=roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }
    public Users getUser(String email){
        log.info("Fetching user {}",email);
        return userRepository.findByEmail(email);
    }
    public List<Users> getUsers(){
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Transactional()
    public Collection<ProductInPurchase> addToCart(String email,String nameProduct,String quantity){
        int qty=Integer.parseInt(quantity);
        Product prod=productRepository.findByNameIgnoreCaseAndEnabled(nameProduct,true);
        if(prod==null)throw new ProductDoesNotExistException();
        Users user=userRepository.findByEmail(email);
        if(user==null)throw new UserDoesNotExistException();
        ProductInPurchase pip= productInPurchaseRepository.findByBuyerAndBuyed(user,prod);
        if(pip==null){
            if(qty>prod.getQuantity())throw new QuantityProductUnavailableException();
            pip=new ProductInPurchase(null,user,prod,qty);
            productInPurchaseRepository.save(pip);
            user.getShoppingCart().add(pip);
        }
        else{
            int total=pip.getQuantity()+qty;
            if(total>prod.getQuantity())throw new QuantityProductUnavailableException();
            pip.setQuantity(pip.getQuantity()+qty);
        }

        return user.getShoppingCart();
    }

    public Collection<ProductInPurchase>setQuantityToCart(String email,String productName, String quantity){
        int qty=Integer.parseInt(quantity);
        Product prod=productRepository.findByNameIgnoreCaseAndEnabled(productName,true);
        if(prod==null)throw new ProductDoesNotExistException();
        Users user=userRepository.findByEmail(email);
        if(user==null)throw new UserDoesNotExistException();
        ProductInPurchase pip= productInPurchaseRepository.findByBuyerAndBuyed(user,prod);
        if(pip==null){
            if(qty>prod.getQuantity())throw new QuantityProductUnavailableException();
            pip=new ProductInPurchase(null,user,prod,qty);
            productInPurchaseRepository.save(pip);
            user.getShoppingCart().add(pip);
        }
        else{
            if(qty>prod.getQuantity() && qty>pip.getQuantity())throw new QuantityProductUnavailableException();
            pip.setQuantity(qty);
        }

        return user.getShoppingCart();
    }

    public Collection<ProductInPurchase> removeFromCart(String email,String nameProduct){
        Users user=userRepository.findByEmail(email);
        Product prod=productRepository.findByNameIgnoreCase(nameProduct);
        ProductInPurchase pip=productInPurchaseRepository.findByBuyerAndBuyed(user,prod);
        if(pip==null)throw new ProductDoesNotExistException();
        productInPurchaseRepository.delete(pip);
        return user.getShoppingCart();
    }

    public Collection<ProductInPurchase> getUserCart(String email){
        Users u=userRepository.findByEmail(email);
        List<ProductInPurchase> cart=u.getShoppingCart();
        Collections.sort(cart);
        return cart;
    }



    //di autenticazione, lasciare così
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user=userRepository.findByEmail(email);
        if(user==null){
            log.error("USER NOT FOUND in database");
            throw new UsernameNotFoundException("USER NOT FOUND in database");
        }
        else{
            log.info("USER {} FOUND in database",email);
        }
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        user.getRoles().forEach(
                role-> {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public Users modifyMyDetails(String email, Users details){
        Users user=userRepository.findByEmail(email);
        if(user==null)throw new UserDoesNotExistException();
        user.setFirstName(details.getFirstName());
        user.setLastName(details.getLastName());
        user.setEmail(details.getEmail());
        user.setPhoneNumber(details.getPhoneNumber());
        user.setAddress(details.getAddress());
        user.setUrlPropic(details.getUrlPropic());
        return user;
    }

    public Users getMyDetails(String email){
        Users user=userRepository.findByEmail(email);
        if(user==null)throw new UserDoesNotExistException();
        return user;
    }

    public Collection<Purchase> getMyOrders(String email){
        Users user=userRepository.findByEmail(email);
        return purchaseRepository.findByBuyer(user);
    }





}
