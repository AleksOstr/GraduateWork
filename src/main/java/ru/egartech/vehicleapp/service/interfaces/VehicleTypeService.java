package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleTypeRequest;
import ru.egartech.vehicleapp.model.VehicleType;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.List;

public interface VehicleTypeService {
    VehicleTypeResponse create(VehicleTypeRequest request);
    VehicleType findByName(VehicleTypeRequest request);
    List<VehicleTypeResponse> findAll();

    void updateType(VehicleType type);
}
