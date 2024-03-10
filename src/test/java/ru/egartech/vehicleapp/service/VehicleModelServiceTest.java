package ru.egartech.vehicleapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleModel;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.repository.VehicleModelRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;
import ru.egartech.vehicleapp.service.interfaces.VehicleModelService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VehicleModelServiceTest {

    @Autowired
    private VehicleModelRepository modelRepository;
    @Autowired
    private VehicleBrandRepository brandRepository;
    @Autowired
    private VehicleModelService modelService;
    @Autowired
    private VehicleBrandService brandService;

    @Test
    void createTest() {
        String brandName = "brand";
        String modelName = "model";
        brandService.create(brandName);

        VehicleModel model = modelService.create(brandName, modelName);

        Assertions.assertNotNull(model.getId());
        Assertions.assertEquals(modelName, model.getName());
        Assertions.assertEquals(brandName, model.getBrand().getName());
    }

    @Test
    void createExceptionTest() {
        String brandName = "brand";
        String modelName = "model";
        brandService.create(brandName);
        modelService.create(brandName, modelName);

        Exception exception = Assertions.assertThrows(ExistingValueException.class, () -> modelService.create(brandName, modelName));
        String message = exception.getMessage();

        Assertions.assertEquals("Model with name: model already exists", message);
    }

    @Test
    void findByNameTest() {
        String brandName = "brand";
        String modelName = "model";
        brandService.create(brandName);
        VehicleModel model = modelService.create(brandName, modelName);

        VehicleModel found = modelService.findByName(modelName);

        Assertions.assertEquals(model.getId(), found.getId());
        Assertions.assertEquals(model.getBrand().getName(), found.getBrand().getName());
        Assertions.assertEquals(model.getName(), found.getName());
    }

    @Test
    void findByNameExceptionTest() {
        Exception exception = Assertions.assertThrows(ValueNotFoundException.class, () -> modelService.findByName("name"));
        String message = exception.getMessage();

        Assertions.assertEquals("Model with name: name not found", message);
    }

    @Test
    void updateTest() {
        String brandName = "brand";
        String modelOldName = "model";
        brandService.create(brandName);
        VehicleModel model = modelService.create(brandName, modelOldName);

        model.setName("new name");
        VehicleModel updated = modelService.updateModel(model);

        Assertions.assertEquals(model.getId(), updated.getId());
        Assertions.assertEquals(model.getName(), updated.getName());
        Assertions.assertNotEquals(modelOldName, updated.getName());
    }

    @BeforeEach
    void cleanDB() {
        brandRepository.deleteAll();
        modelRepository.deleteAll();
    }
}
