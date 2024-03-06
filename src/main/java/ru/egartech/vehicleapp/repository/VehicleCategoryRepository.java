package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.VehicleCategory;

import java.util.UUID;

@Repository
public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, UUID> {
}
