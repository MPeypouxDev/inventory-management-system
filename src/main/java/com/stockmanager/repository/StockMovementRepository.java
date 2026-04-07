package com.stockmanager.repository;

import com.stockmanager.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductId(Long productId);
    List<StockMovement> findByProductNameContainingIgnoreCase(String name);
}
