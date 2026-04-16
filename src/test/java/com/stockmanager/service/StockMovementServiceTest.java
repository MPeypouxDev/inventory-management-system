package com.stockmanager.service;

import com.stockmanager.model.Product;
import com.stockmanager.model.StockMovement;
import com.stockmanager.model.User;
import com.stockmanager.repository.StockMovementRepository;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.config.SessionManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockMovementServiceTest {

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SessionManager sessionManager;

    @InjectMocks
    private StockMovementService stockMovementService;

    private StockMovement testStockMovement;

    @BeforeEach
    void setUp() {
        testStockMovement = new StockMovement();
        testStockMovement.setId(1L);
        testStockMovement.setDate(LocalDateTime.now());
        testStockMovement.setQuantity(5);
        testStockMovement.setReason("Réapprovisionnement initial");
        testStockMovement.setType(StockMovement.MovementType.ENTRY);
        testStockMovement.setProduct(new Product());
        testStockMovement.setId(1L);
    }

    @Test
    @DisplayName("addMovement() doit lancer une exception si le produit n'existe pas")
    void add_shouldThrowException_whenProductNotFound() {
        Product product = new Product();
        product.setId(99L);
        testStockMovement.setProduct(product);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stockMovementService.addMovement(testStockMovement));

        assertEquals("Produit introuvable", exception.getMessage());
    }

    @Test
    @DisplayName("addMovement() doit lancer une exception si le type de mouvement est OUTGOING et que le stock produit est insuffisant")
    void add_shouldThrowException_whenStockQuantityIsNotEnough() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(2);

        testStockMovement.setProduct(product);
        testStockMovement.setType(StockMovement.MovementType.OUTGOING);
        testStockMovement.setQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class,
                () -> stockMovementService.addMovement(testStockMovement));
    }

    @Test
    @DisplayName("addMovement() doit augmenter le stock si ENTRY")
    void add_shouldIncreaseStock_whenTypeIsEntry() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        testStockMovement.setProduct(product);
        testStockMovement.setType(StockMovement.MovementType.ENTRY);
        testStockMovement.setQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        stockMovementService.addMovement(testStockMovement);

        assertEquals(15, product.getStockQuantity());
    }

    @Test
    @DisplayName("addMovement() doit diminuer le stock si OUTGOING")
    void add_shouldDecreaseStock_whenTypeIsOutgoing() {
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);

        testStockMovement.setProduct(product);
        testStockMovement.setType(StockMovement.MovementType.OUTGOING);
        testStockMovement.setQuantity(5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        stockMovementService.addMovement(testStockMovement);

        assertEquals(5, product.getStockQuantity());
    }

    @Test
    @DisplayName("findByProduct() doit retourner les mouvements du produit sélectionné")
    void findByProduct_shouldReturnOneProduct() {
        Product product = new Product();
        product.setId(1L);

        testStockMovement.setProduct(product);

        when(stockMovementRepository.findByProductId(1L)).thenReturn(List.of(testStockMovement));

        List<StockMovement> result = stockMovementService.findByProduct(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findByUser() doit retourner les mouvements de l'utilisateur sélectionné")
    void findByUser_shouldReturnMovement_whenUserIsTheOwner() {
        User user = new User();
        user.setId(1L);

        testStockMovement.setUser(user);

        when(stockMovementRepository.findByUserId(1L)).thenReturn(List.of(testStockMovement));

        List<StockMovement> result = stockMovementService.findByUser(1L);

        assertEquals(1, result.size());
    }
}
