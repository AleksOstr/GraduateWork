package ru.egartech.vehicleapp.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.model.Vehicle;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.service.VehicleBrandServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class VehicleBrandServiceUnitTest {

    @Mock
    private VehicleBrandRepository repository;
    @InjectMocks
    private VehicleBrandServiceImpl service;

    @Test
    void shouldReturnOptionalByName() {
        String brandName = "brand";
        Optional<VehicleBrand> optionalBrand = setupOptionalBrand(brandName);

        Mockito.when(repository.findByNameIgnoreCase(brandName)).thenReturn(optionalBrand);

        Optional<VehicleBrand> found = service.findByName(brandName);

        assertNotNull(found);
        assertTrue(found.isPresent());
        assertEquals(brandName, found.get().getName());
    }

    @Test
    void shouldReturnCreatedBrand() {
        String brandName = "brand";
        VehicleBrand brand = setupOptionalBrand(brandName).get();

        Mockito.when(repository.save(any(VehicleBrand.class))).thenReturn(brand);

        VehicleBrand saved = service.create(brandName);

        assertNotNull(saved);
        assertEquals(brand.getId(), saved.getId());
        assertEquals(brand.getName(), saved.getName());
    }

    @Test
    void shouldReturnUpdatedBrand() {
        String brandName = "brand";
        VehicleBrand brand = setupOptionalBrand(brandName).get();

        Mockito.when(repository.save(any(VehicleBrand.class))).thenReturn(brand);

        VehicleBrand updated = service.update(brand);

        assertNotNull(updated);
        assertEquals(brand.getId(), updated.getId());
        assertEquals(brand.getName(), updated.getName());
    }

    private Optional<VehicleBrand> setupOptionalBrand(String brandName) {
        VehicleBrand brand = new VehicleBrand();
        brand.setId(UUID.randomUUID());
        brand.setName(brandName);
        return Optional.of(brand);
    }
}
