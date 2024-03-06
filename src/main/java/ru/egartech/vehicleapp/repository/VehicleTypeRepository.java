package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.VehicleType;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, UUID> {
    Optional<VehicleType> findByTypeNameIgnoreCase(String name);
}
