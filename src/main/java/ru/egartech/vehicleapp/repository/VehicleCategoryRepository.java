package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.VehicleCategory;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий категорий ТС
 */
@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, UUID> {
    Optional<VehicleCategory> findByNameIgnoreCase(String categoryName);
}
