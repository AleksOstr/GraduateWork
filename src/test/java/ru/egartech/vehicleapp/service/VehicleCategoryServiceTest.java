package ru.egartech.vehicleapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleCategory;
import ru.egartech.vehicleapp.repository.VehicleCategoryRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleCategoryService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class VehicleCategoryServiceTest {

    @Autowired
    private VehicleCategoryRepository repository;
    @Autowired
    private VehicleCategoryService service;

    @Test
    void createTest() {
        String expectedName = "category";

        VehicleCategory actual = service.create(expectedName);

        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expectedName, actual.getName());
    }

    @Test
    void createExceptionTest() {
        String categoryName = "category";
        VehicleCategory category = new VehicleCategory();
        category.setName(categoryName);
        repository.save(category);

        Exception exception = Assertions.assertThrows(ExistingValueException.class, () -> service.create(categoryName));
        String message = exception.getMessage();

        Assertions.assertEquals("Category with name: category already exists", message);
    }

    @Test
    void findByNameTest() {
        String categoryName = "name";
        VehicleCategory saved = service.create(categoryName);

        VehicleCategory found = service.findByName(categoryName).orElseThrow();

        Assertions.assertEquals(saved.getId(), found.getId());
        Assertions.assertEquals(saved.getName(), found.getName());
    }

    @Test
    void findByNameExceptionTest() {
        Exception exception = Assertions.assertThrows(ValueNotFoundException.class, () -> service.findByName("name"));
        String message = exception.getMessage();

        Assertions.assertEquals("Category with name: name not found", message);
    }

    @Test
    void updateTest() {
        String oldName = "old name";
        VehicleCategory saved = service.create(oldName);
        saved.setName("new name");
        VehicleCategory updated = service.update(saved);

        Assertions.assertEquals(saved.getId(), updated.getId());
        Assertions.assertEquals(saved.getName(), updated.getName());
        Assertions.assertNotEquals(oldName, updated.getName());
    }

    @BeforeEach
    void cleanDB() {
        repository.deleteAll();
    }
}
