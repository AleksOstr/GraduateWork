package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.VehicleBrand;

import java.util.UUID;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, UUID> {
}
