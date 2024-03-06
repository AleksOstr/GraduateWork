package ru.egartech.vehicleapp.api.request;

import lombok.Data;

@Data
public class VehicleModelRequest {
    private String brandName;
    private String modelName;
}
