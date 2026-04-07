package com.stockmanager.service;

import com.stockmanager.model.Product;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> findLowStockProducts() {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getStockQuantity() <= p.getAlertThreshold())
                .toList();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id : " + id));

        List<?> movements = stockMovementRepository.findByProductId(id);
        if (!movements.isEmpty()) {
            throw new RuntimeException(
                    "Impossible de supprimer '" + product.getName() +
                    "' : il possède " + movements.size() + " mouvement(s) de stock."
            );
        }

        productRepository.delete(product);
    }
}
