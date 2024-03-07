package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.VehicleModelRequest;
import ru.egartech.vehicleapp.model.VehicleModel;
import ru.egartech.vehicleapp.service.response.VehicleModelResponse;

public interface VehicleModelService {
    VehicleModelResponse create(VehicleModelRequest request);

    VehicleModel findByModelName(VehicleModelRequest request);

    void updateModel(VehicleModel model);

}
