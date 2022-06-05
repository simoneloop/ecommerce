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
@Table(name = "product_in_purchase")
@NoArgsConstructor
@AllArgsConstructor
public class ProductInPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="buyer")
    private Users buyer;

    @ManyToOne
    @JoinColumn(name="buyed")
    private Product buyed;

    @Column(name="quantity")
    private int quantity;


}