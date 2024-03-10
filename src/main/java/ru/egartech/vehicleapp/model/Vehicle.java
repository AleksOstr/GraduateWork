package ru.egartech.vehicleapp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Сущность ТС
 */
@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn
    private VehicleBrand brand;

    @ManyToOne
    @JoinColumn
    private VehicleModel model;

    @ManyToOne
    @JoinColumn
    private VehicleType type;

    @ManyToOne
    @JoinColumn
    private VehicleCategory category;

    // Регистрационный номер
    @Column(name = "reg_number")
    private String regNumber;

    // Год выпуска
    @Column(name = "prod_year")
    private int prodYear;

    // Наличие прицепа
    @Column(name = "has_trailer")
    private boolean hasTrailer;

}
