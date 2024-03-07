package ru.egartech.vehicleapp.api.request;

import lombok.Data;

import java.util.Date;

@Data
public class VehicleRequest {
    private String brand;
    private String model;
    private String type;
    private String category;
    private String regNumber;
    private Date prodYear;
    private boolean hasTrailer;
}
