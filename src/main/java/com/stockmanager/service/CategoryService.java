package com.stockmanager.service;

import com.stockmanager.model.Category;
import com.stockmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable : " + id));
        if (!category.getProducts().isEmpty()) {
            throw new RuntimeException(
                    "Impossible de supprimer '" + category.getName() +
                    "' : elle contient " + category.getProducts().size() + " produit(s)."
            );
        }
        categoryRepository.delete(category);
    }
}
