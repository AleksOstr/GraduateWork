package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleCategoryRequest;
import ru.egartech.vehicleapp.model.VehicleCategory;
import ru.egartech.vehicleapp.service.response.VehicleCategoryResponse;

import java.util.List;

public interface VehicleCategoryService {
    VehicleCategoryResponse create(VehicleCategoryRequest request);

    VehicleCategory findByCategoryName(VehicleCategoryRequest request);
    List<VehicleCategoryResponse> findAll();

    void updateCategory(VehicleCategory category);

}
