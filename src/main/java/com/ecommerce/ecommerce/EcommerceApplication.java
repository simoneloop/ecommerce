package com.ecommerce.ecommerce;

import com.ecommerce.ecommerce.entities.Product;
import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;
import com.ecommerce.ecommerce.services.ProductService;
import com.ecommerce.ecommerce.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, ProductService productService){
        return args -> {
            userService.saveRole(new Role(null,"ROLE_USER"));
            userService.saveRole(new Role(null,"ROLE_ADMIN"));


            userService.saveUser(new Users(null,"simone","lopez","simonelopez@hotmail.it","3517204474","viagBruno5","ryumille12","img1.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","simolop@hotmail.it","mannaiaal numero","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","email1","numero1","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","email2","numero2","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","email3","numero3","asd","asd","asd.png",new ArrayList<>(),new ArrayList<>()));


            userService.addRoleToUser("simonelopez@hotmail.it","ROLE_ADMIN");
            userService.addRoleToUser("simolop@hotmail.it","ROLE_USER");


            productService.addProduct(new Product(null,"collanagioiosa","una gioiosa collana",2,(float)3,"collana",null,null));
            productService.addProduct(new Product(null,"collanasfarzosa","una sfarzosa collana",4,(float)4,"collana",null,null));
            productService.addProduct(new Product(null,"collanagraziosa","una graziosa collana",15,(float)5,"collana",null,null));
            productService.addProduct(new Product(null,"bracciale gioioso","un gioioso bracciale",21,(float)3.5,"bracciale",null,null));
            productService.addProduct(new Product(null,"bracciale sfarzoso","uno sfarzoso bracciale",21,(float)4.5,"bracciale",null,null));
            productService.addProduct(new Product(null,"bracciale grazioso","un grazioso bracciale",21,(float)5.5,"bracciale",null,null));
            productService.addProduct(new Product(null,"orecchino gioioso","un gioioso orecchino",21,(float)3.6,"orecchino",null,null));
            productService.addProduct(new Product(null,"orecchino sfarzoso","uno sfarzoso orecchino",21,(float)4.6,"orecchino",null,null));
            productService.addProduct(new Product(null,"orecchino grazioso","un grazioso orecchino",21,(float)5.6,"orecchino",null,null));

        };
    }
}
