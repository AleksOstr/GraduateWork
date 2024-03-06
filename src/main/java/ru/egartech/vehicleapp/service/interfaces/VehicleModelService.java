package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;
import ru.egartech.vehicleapp.api.request.VehicleModelRequest;
import ru.egartech.vehicleapp.model.VehicleModel;

public interface VehicleModelService {
    VehicleModel create(VehicleModelRequest request);
    VehicleBrandRequest findByhModelName(VehicleModelRequest request);

}
