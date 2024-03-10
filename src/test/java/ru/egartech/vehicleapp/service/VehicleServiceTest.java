package ru.egartech.vehicleapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.repository.*;
import ru.egartech.vehicleapp.service.interfaces.VehicleService;
import ru.egartech.vehicleapp.service.response.VehicleResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VehicleServiceTest {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleBrandRepository brandRepository;
    @Autowired
    private VehicleModelRepository modelRepository;
    @Autowired
    private VehicleTypeRepository typeRepository;
    @Autowired
    private VehicleCategoryRepository categoryRepository;

    @Test
    void createWithNewRegNumberTest() {
        VehicleRequest request = new VehicleRequest();
        request.setBrand("brand");
        request.setModel("model");
        request.setType("type");
        request.setCategory("category");
        request.setRegNumber("А001АА111");
        request.setProdYear("2024");
        request.setHasTrailer("да");

        VehicleResponse response = vehicleService.create(request);

        assertNotNull(response.getId());
        assertEquals(request.getBrand(), response.getBrand());
        assertEquals(request.getModel(), response.getModel());
        assertEquals(request.getCategory(), response.getCategory());
        assertEquals(request.getType(), response.getType());
        assertEquals(request.getRegNumber(), response.getRegNumber());
        assertEquals(request.getProdYear(), String.valueOf(response.getProdYear()));
        assertEquals(request.getHasTrailer(), response.getHasTrailer());
    }

    @Test
    void createWithExistingRegNumberTest() {
        VehicleRequest request = new VehicleRequest();
        request.setBrand("brand");
        request.setModel("model");
        request.setType("type");
        request.setCategory("category");
        request.setRegNumber("А001АА111");
        request.setProdYear("2024");
        request.setHasTrailer("да");

        vehicleService.create(request);

        Exception exception = assertThrows(ExistingValueException.class, () -> vehicleService.create(request));
        String message = exception.getMessage();

        assertEquals("Транспортное средство с гос. номером: А001АА111 уже существует", message);
    }

    @BeforeEach
    void cleanDB(){
        vehicleRepository.deleteAll();
        modelRepository.deleteAll();
        brandRepository.deleteAll();
        typeRepository.deleteAll();
        categoryRepository.deleteAll();
    }

}
