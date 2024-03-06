package ru.egartech.vehicleapp.api.request;

import lombok.Data;

@Data
public class VehicleRequest {
    private String brand;
    private String model;
    private String type;
    private String category;
    private String regNumber;
    private String prodYear;
    private boolean hasTrailer;
}
