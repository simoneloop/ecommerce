package com.ecommerce.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id",nullable = false)
    private Long id;

    @Column(name="first_name",nullable = false)
    private String firstName;

    @Column(name="last_name",nullable = false)
    private String lastName;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name="phone_number",nullable = false,unique = true)
    private String phoneNumber;


    @Column(name="address",nullable = true)
    private String address;
    @Column(name="password",nullable = false)
    private String password;
    @Column(name="url_propic",nullable = true)
    private String urlPropic;

    @ManyToMany()
    private Collection<Role> roles=new ArrayList<>();

    @ManyToMany()
    private Collection<Product> shoppingCart=new ArrayList<>();
}
