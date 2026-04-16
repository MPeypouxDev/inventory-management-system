package com.stockmanager.service;

import com.stockmanager.model.Product;
import com.stockmanager.model.StockMovement;
import com.stockmanager.repository.ProductRepository;
import com.stockmanager.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop Pro 15");
        testProduct.setStockQuantity(10);
        testProduct.setAlertThreshold(5);
    }

    @Test
    @DisplayName("delete() doit lancer une exception si le produit n'existe pas")
    void delete_shouldThrowException_whenProductNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.delete(99L));

        assertEquals("Produit introuvable avec l'id : 99", exception.getMessage());
    }

    @Test
    @DisplayName("delete() doit lancer une exception si le produit a des mouvements")
    void delete_shouldThrowException_whenProductHasMovements() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(stockMovementRepository.findByProductId(1L))
                .thenReturn(List.of(new StockMovement()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.delete(1L));

        assertTrue(exception.getMessage().contains("mouvement"));
    }

    @Test
    @DisplayName("delete() doit supprimer le produit s'il n'a pas de mouvements")
    void delete_shouldDeleteProduct_whenNoMovements() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(stockMovementRepository.findByProductId(1L))
                .thenReturn(Collections.emptyList());

        productService.delete(1L);
        verify(productRepository, times(1)).delete(testProduct);
    }

    @Test
    @DisplayName("findLowStockProducts() retourne seulement les produits en alerte")
    void findLowStockProducts_shouldReturnOnly_whenProductsAreLowStock() {
        Product lowStockProduct = new Product();
        lowStockProduct.setId(1L);
        lowStockProduct.setName("Tapis de souris");
        lowStockProduct.setStockQuantity(2);
        lowStockProduct.setAlertThreshold(3);

        Product highStockProduct = new Product();
        highStockProduct.setId(2L);
        highStockProduct.setName("Câble HDMI");
        highStockProduct.setStockQuantity(25);
        highStockProduct.setAlertThreshold(5);

        when(productRepository.findAll()).thenReturn(List.of(lowStockProduct, highStockProduct));

        List<Product> result = productService.findLowStockProducts();

        assertEquals(1, result.size());
        assertEquals("Tapis de souris", result.getFirst().getName());
    }

    @Test
    @DisplayName("searchByName() retourne seulement le produit avec le nom exact saisi")
    void searchByName_shouldReturnOnlyProduct_whenTheNameIsExactSame() {
        when(productRepository.findByNameContainingIgnoreCase("Laptop")).thenReturn(List.of(testProduct));

        List<Product> result = productService.searchByName("Laptop");

        assertEquals(1, result.size());
        assertEquals("Laptop Pro 15", result.getFirst().getName());
    }
}
