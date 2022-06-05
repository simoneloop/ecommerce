package com.ecommerce.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String urlPropic;

    public UsersDTO(Users u){
        this.firstName=u.getFirstName();
        this.lastName=u.getLastName();
        this.email=u.getEmail();
        this.phoneNumber=u.getPhoneNumber();
        this.address=u.getAddress();

    }
}
