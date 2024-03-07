package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.model.VehicleModel;

public interface VehicleModelService {
    VehicleModel create(String brandName, String modelName);

    VehicleModel findByName(String modelName);

    void updateModel(VehicleModel model);

}
