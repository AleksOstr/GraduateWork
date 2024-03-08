package ru.egartech.vehicleapp.api.request;

import lombok.Data;

@Data
public class SearchRequest {
    private String brand;
    private String model;
    private String category;
    private String regNumber;
    private String prodYear;
}
