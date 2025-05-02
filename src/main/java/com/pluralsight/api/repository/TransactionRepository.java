package com.pluralsight.api.repository;

import com.pluralsight.api.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    // Métodos personalizados si los necesitas
}
