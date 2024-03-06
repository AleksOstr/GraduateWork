package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;
import ru.egartech.vehicleapp.model.VehicleBrand;

public interface VehicleBrandService {

    VehicleBrand create(VehicleBrandRequest request);
    VehicleBrand findByBrandName(VehicleBrandRequest request);
    VehicleBrand updateBrand(VehicleBrand brand);
}
