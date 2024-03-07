package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.model.VehicleType;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.List;

public interface VehicleTypeService {
    VehicleType create(String typeName);

    VehicleType findByName(String typeName);

    List<VehicleTypeResponse> findAll();

    void updateType(VehicleType type);
}
