package com.ecommerce.ecommerce;

import com.ecommerce.ecommerce.entities.Role;
import com.ecommerce.ecommerce.entities.Users;
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
    CommandLineRunner run(UserService userService){
        return args -> {
            userService.saveRole(new Role(null,"ROLE_USER"));
            userService.saveRole(new Role(null,"ROLE_ADMIN"));


            userService.saveUser(new Users(null,"simone","lopez","simonelopez@hotmail.it","3517204474","viagBruno5","ryumille12","img1.png",new ArrayList<>()));
            userService.saveUser(new Users(null,"simo","lop","simolop@hotmail.it","mannaiaal numero","asd","asd","asd.png",new ArrayList<>()));

            userService.addRoleToUser("simonelopez@hotmail.it","ROLE_ADMIN");
            userService.addRoleToUser("simolop@hotmail.it","ROLE_USER");
        };
    }
}
