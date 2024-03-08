package ru.egartech.vehicleapp.api.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleRequest {
    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    private String type;

    @NotNull
    private String category;

    @NotNull
    @Pattern(regexp = "[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$")
    private String regNumber;

    @NotNull
    private String prodYear;

    @NotNull
    private String hasTrailer;
}
