package ru.egartech.vehicleapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
// Сущность ТС
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    private VehicleBrand brand;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    private VehicleModel model;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    private VehicleType type;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn
    private VehicleCategory category;

    // Регистрационный номер
    @Column(name = "reg_number")
    private String regNumber;

    // Год выпуска
    @Column(name = "prod_year")
    @DateTimeFormat(pattern = "YYYY")
    private Date prodYear;

    // Наличие прицепа
    @Column(name = "has_trailer")
    private boolean hasTrailer;

}
