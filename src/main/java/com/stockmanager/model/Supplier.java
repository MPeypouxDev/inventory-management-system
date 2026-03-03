package com.stockmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "suppliers")

public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String contact;
    private String telephone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "supplier")
    private List<Product> products;
}