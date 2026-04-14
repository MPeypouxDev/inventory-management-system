package com.stockmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movements_stock")

public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MovementType type;

    private Integer quantity;
    private LocalDateTime date = LocalDateTime.now();
    private String reason;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public enum MovementType {
        ENTRY("Entrée"),
        OUTGOING("Sortie");

        private final String label;

        MovementType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
