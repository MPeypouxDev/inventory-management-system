package com.stockmanager.service;

import com.stockmanager.model.Product;
import com.stockmanager.model.Supplier;
import com.stockmanager.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    private Supplier testSupplier;

    @BeforeEach
    void setUp() {
        testSupplier = new Supplier();
        testSupplier.setId(1L);
        testSupplier.setAddress("15 Rue de la Paix Paris");
        testSupplier.setContact("Jean Dupont");
        testSupplier.setEmail("contact@techpro.fr");
        testSupplier.setName("TechPro");
        testSupplier.setTelephone("0123456789");
        testSupplier.setProducts(new ArrayList<>());
    }

    @Test
    @DisplayName("delete() doit lancer une exception si le fournisseur n'existe pas")
    void delete_shouldThrowException_whenSupplierNotFound() {
        when(supplierRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.delete(99L));

        assertEquals("Fournisseur introuvable : 99", exception.getMessage());
    }

    @Test
    @DisplayName("delete() doit lancer une exception si le fournisseur a des produits associés")
    void delete_shouldThrowException_whenSupplierHasProducts() {
        testSupplier.setProducts(List.of(new Product()));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> supplierService.delete(1L));

        assertTrue(exception.getMessage().contains("produit"));
    }

    @Test
    @DisplayName("delete() doit supprimer le fournisseur si il n'a pas de produits associés")
    void delete_shouldDeleteSupplier_whenNoProducts() {
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(testSupplier));

        supplierService.delete(1L);
        verify(supplierRepository, times(1)).delete(testSupplier);
    }
}
