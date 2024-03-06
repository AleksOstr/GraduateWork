package ru.egartech.vehicleapp.service.response;

import lombok.Data;

import java.util.UUID;

@Data
public class VehicleResponse {
    private UUID id;
    private String brand;
    private String model;
    private String type;
    private String category;
    private String regNumber;
    private String prodYear;
    private boolean hasTrailer;
}
