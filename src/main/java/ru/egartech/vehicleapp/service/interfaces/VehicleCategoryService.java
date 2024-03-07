package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.model.VehicleCategory;

public interface VehicleCategoryService {
    VehicleCategory create(String categoryName);

    VehicleCategory findByName(String categoryName);

    void updateCategory(VehicleCategory category);

}
