package ru.egartech.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.egartech.vehicleapp.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    List<Vehicle> findAllByBrand(VehicleBrand brand);
    List<Vehicle> findAllByModel(VehicleModel model);
    List<Vehicle> findAllByType(VehicleType type);
    List<Vehicle> findAllByCategory(VehicleCategory category);
    List<Vehicle> findAllByProdYear(Date prodYear);
    List<Vehicle> findAllByHasTrailer(boolean hasTrailer);
    Optional<Vehicle> findByRegNumberIgnoreCase(String regNumber);

}
