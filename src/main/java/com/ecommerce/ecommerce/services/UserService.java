package com.ecommerce.ecommerce.services;

import com.ecommerce.ecommerce.UTI.exception.ProductDoesNotExistException;
import com.ecommerce.ecommerce.UTI.exception.QuantityProductUnavailableException;
import com.ecommerce.ecommerce.UTI.exception.UserAlreadyExistException;
import com.ecommerce.ecommerce.UTI.exception.UserDoesNotExistException;
import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.ProductInPurchase;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.repositories.ProductInPurchaseRepository;
import com.ecommerce.ecommerce.repositories.ProductRepository;
import com.ecommerce.ecommerce.repositories.RoleRepository;
import com.ecommerce.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
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

    private final PasswordEncoder passwordEncoder;



    public Users saveUser(Users user) throws Exception {
        /*
            TODO controlli su i dati inseriti
         */
        log.info("Saving new user {} to the database",user.getEmail());
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        Product prod=productRepository.findByName(nameProduct);
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

    public Collection<ProductInPurchase> getUserCart(String email){
        return userRepository.findByEmail(email).getShoppingCart();
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



}
