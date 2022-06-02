package com.ecommerce.ecommerce.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;



@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name = "quantity",nullable = false,unique = true)
    private int quantity;

    @Column(name="price",nullable = false,unique = true)
    private float price;

    @Column(name="url_propic",nullable = true,unique = true)
    private String urlPropic;
}
