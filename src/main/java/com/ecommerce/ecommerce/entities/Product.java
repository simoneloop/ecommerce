package com.ecommerce.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;



@Data
@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private Long id;

    @Column(name="name",nullable = false,unique = true)
    private String name;

    @Column(name="description",nullable = true)
    private String description;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(name="price",nullable = false)
    private float price;

    @Column(name="typo",nullable = false)
    private String typo;

    @Column(name="url_propic",nullable = true)
    private String urlPropic;

    @Version
    //valore modificato ogni modifica dell'entità,
    // prende il valore all'inizio della transazione e lo confronta con quello alla fine se i due non combaciano chiama una
    //ObjectOptimisticLockingFailureException che essendo una RuntimeException triggera il rollback in ambienti transazionali
    @Column(name = "version", nullable = false)
    @JsonIgnore
    private Long version;

    @Column(name="hot",nullable = false)
    private boolean hot;


}
