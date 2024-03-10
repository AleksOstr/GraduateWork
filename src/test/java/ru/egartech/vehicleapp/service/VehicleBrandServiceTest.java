package ru.egartech.vehicleapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VehicleBrandServiceTest {

    @Autowired
    private VehicleBrandRepository repository;
    @Autowired
    private VehicleBrandService service;

    @Test
    void createTest() {
        String expectedName = "brand";

        VehicleBrand actual = service.create(expectedName);

        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expectedName, actual.getName());

        repository.delete(actual);
    }

    @Test
    void createExceptionTest() {
        String brandName = "brand";
        VehicleBrand brand = new VehicleBrand();
        brand.setName(brandName);
        repository.save(brand);

        Exception exception = Assertions.assertThrows(ExistingValueException.class, () -> service.create(brandName));
        String message = exception.getMessage();

        Assertions.assertEquals("Brand with name: brand already exists", message);

        repository.delete(brand);
    }

    @Test
    void findByNameTest() {
        String brandName = "brand";
        VehicleBrand brand = new VehicleBrand();
        brand.setName(brandName);
        VehicleBrand saved = repository.save(brand);

        VehicleBrand found = service.findByName(brandName);

        Assertions.assertEquals(saved.getId(), found.getId());
        Assertions.assertEquals(saved.getName(), found.getName());

        repository.delete(saved);
    }

    @Test
    void updateTest() {
        String oldName = "old name";
        VehicleBrand saved = service.create(oldName);
        saved.setName("new name");
        VehicleBrand updated = service.updateBrand(saved);

        Assertions.assertEquals(saved.getId(), updated.getId());
        Assertions.assertEquals(saved.getName(), updated.getName());
        Assertions.assertNotEquals(oldName, updated.getName());

        repository.delete(updated);
    }
}
