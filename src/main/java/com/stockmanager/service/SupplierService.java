package com.stockmanager.service;

import com.stockmanager.model.Supplier;
import com.stockmanager.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public List<Supplier> searchByName(String name) {
        return supplierRepository.findByNameContainingIgnoreCase(name);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public void delete(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable : " + id));
        if (!supplier.getProducts().isEmpty()) {
            throw new RuntimeException(
                    "Impossible de supprimer '" + supplier.getName() +
                    "' : il a " + supplier.getProducts().size() + " produit(s) associé(s)."
            );
        }
        supplierRepository.delete(supplier);
    }
}
