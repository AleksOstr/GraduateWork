package ru.egartech.vehicleapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.model.Vehicle;
import ru.egartech.vehicleapp.repository.VehicleRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleService;
import ru.egartech.vehicleapp.service.response.VehicleResponse;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VehicleServiceTest {

    @Autowired
    VehicleService service;

    @Autowired
    VehicleRepository repository;

    @Test
    void createTest() {
        VehicleRequest request = new VehicleRequest();
        request.setBrand("brand");
        request.setModel("model");
        request.setType("type");
        request.setCategory("cat");
        request.setProdYear(LocalDate.parse("2024-01-01"));
        request.setHasTrailer("нет");
        request.setRegNumber("number");

        VehicleResponse response = service.create(request);
        Vehicle vehicle = repository.findById(response.getId()).orElseThrow();

        Assertions.assertEquals(vehicle.getBrand().getVehicles().size(), 1);
    }
}
