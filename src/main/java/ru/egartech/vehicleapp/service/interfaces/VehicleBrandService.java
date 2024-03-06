package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.service.response.VehicleBrandResponse;

public interface VehicleBrandService {

    VehicleBrandResponse create(VehicleBrandRequest request);
    VehicleBrandResponse findByBrandName(VehicleBrandRequest request);
    void updateBrand(VehicleBrand brand);
}
