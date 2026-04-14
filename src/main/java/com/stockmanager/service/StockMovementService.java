package com.stockmanager.service;

import com.stockmanager.config.SessionManager;
import com.stockmanager.model.Product;
import com.stockmanager.model.StockMovement;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    private final SessionManager sessionManager;

    public List<StockMovement> findAll() {
        return stockMovementRepository.findAll();
    }

    public List<StockMovement> searchByProductName(String name) {
        return stockMovementRepository.findByProductNameContainingIgnoreCase(name);
    }

    public List<StockMovement> findByProduct(Long productId) {
        return stockMovementRepository.findByProductId(productId);
    }

    public List<StockMovement> findByUser(Long userId) {
        return stockMovementRepository.findByUserId(userId);
    }

    @Transactional
    public StockMovement addMovement(StockMovement movement) {
        Product product = productRepository.findById(movement.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        if (movement.getType() == StockMovement.MovementType.ENTRY) {
            product.setStockQuantity(product.getStockQuantity() + movement.getQuantity());
        } else {
            if (product.getStockQuantity() < movement.getQuantity()) {
                throw new RuntimeException(
                        "Stock insuffisant : " + product.getStockQuantity() +
                        " disponible(s), " + movement.getQuantity() + " demandé(s)."
                );
            }
            product.setStockQuantity(product.getStockQuantity() - movement.getQuantity());
        }

        movement.setDate(LocalDateTime.now());
        movement.setUser(sessionManager.getCurrentUser());
        productRepository.save(product);
        return stockMovementRepository.save(movement);
    }
}
