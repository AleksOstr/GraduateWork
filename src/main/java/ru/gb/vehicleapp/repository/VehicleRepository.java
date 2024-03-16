package ru.gb.vehicleapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.gb.vehicleapp.model.Vehicle;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий ТС
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {

    /**
     * Поиск ТС по гос. номеру с игнорированием регистра
     * @param regNumber - гос. номер ТС
     * @return - ТС
     */
    Optional<Vehicle> findByRegNumberIgnoreCase(String regNumber);

}
