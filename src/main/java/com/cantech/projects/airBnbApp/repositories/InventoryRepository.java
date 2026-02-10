package com.cantech.projects.airBnbApp.repositories;

import com.cantech.projects.airBnbApp.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
