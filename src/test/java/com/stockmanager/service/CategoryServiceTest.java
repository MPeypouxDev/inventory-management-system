package com.stockmanager.service;

import com.stockmanager.model.Category;
import com.stockmanager.model.Product;
import com.stockmanager.repository.CategoryRepository;
import com.stockmanager.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Bureautique");
        testCategory.setDescription("Fournitures de bureau");
        testCategory.setProducts(new ArrayList<>());
    }

    @Test
    @DisplayName("delete() doit lancer une exception si la catégorie n'existe pas")
    void delete_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoryService.delete(99L));

        assertEquals("Catégorie introuvable : 99", exception.getMessage());
    }

    @Test
    @DisplayName("delete() doit lancer une exception si la catégorie a des produits associés")
    void delete_shouldThrowException_whenCategoryHasProducts() {
        testCategory.setProducts(List.of(new Product()));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoryService.delete(1L));

        assertTrue(exception.getMessage().contains("produit"));
    }

    @Test
    @DisplayName("delete() doit supprimer la catégorie si elle n'a pas de produits associés")
    void delete_shouldDeleteCategory_whenNoProducts() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        categoryService.delete(1L);
        verify(categoryRepository, times(1)).delete(testCategory);
    }

    @Test
    @DisplayName("findAll() doit retourner la liste complète de toutes les catégories")
    void findAll_shouldReturnList_ofAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(testCategory, new Category()));

        List<Category> result = categoryService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("save() doit sauvegarder la catégorie si elle remplie les conditions nécessaires")
    void save_shouldSaveCategory_whenAllConditionsAreCorrect() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Câblage");
        category.setDescription("Câble informatique divers");
        category.setProducts(new ArrayList<>());

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.save(category);

        assertNotNull(result);
        assertEquals("Câblage", result.getName());
    }
}
