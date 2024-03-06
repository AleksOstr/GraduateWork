package ru.egartech.vehicleapp.api.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;

public interface VehicleBrandController {
    String create(VehicleBrandRequest  request);
    String findByBrandName(VehicleBrandRequest request);
}
