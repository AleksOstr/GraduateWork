package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.VehicleBrand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий марок ТС
 */
@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, UUID> {
    List<VehicleBrand> findAllByNameIgnoreCase(String brandName);
    Optional<VehicleBrand> findByNameIgnoreCase(String brandName);
}
