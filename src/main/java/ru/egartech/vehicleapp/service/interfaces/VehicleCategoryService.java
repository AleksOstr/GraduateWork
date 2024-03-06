package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleCategoryRequest;
import ru.egartech.vehicleapp.model.VehicleCategory;

import java.util.List;

public interface VehicleCategoryService {
    VehicleCategory create(VehicleCategoryRequest request);
    List<VehicleCategory> findAll();
}
