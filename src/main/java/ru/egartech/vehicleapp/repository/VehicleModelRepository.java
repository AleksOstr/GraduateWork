package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.VehicleModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, UUID> {
    Optional<VehicleModel> findByModelNameIgnoreCase(String modelName);
}
