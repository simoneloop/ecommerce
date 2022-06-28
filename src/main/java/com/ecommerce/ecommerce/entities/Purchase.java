package com.ecommerce.ecommerce.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Data
@Entity
@Table(name = "purchase")
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "purchase_time")
    private Date purchaseTime;

    @ManyToOne()
    @JoinColumn(name="buyer")
    private Users buyer;

    @ManyToOne
    @JoinColumn(name="buyed")
    private Product buyed;

    @Column(name="quantity")
    private int quantity;

    @Column(name="fixed_price")
    private double fixed_price;

    @Column(name="done")
    private boolean done;



}
