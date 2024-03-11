package ru.egartech.vehicleapp.service.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.service.VehicleBrandServiceImpl;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class VehicleBrandServiceImplTest {
    private final String brandName = "name";
    @Mock
    private VehicleBrandRepository brandRepository;

    @InjectMocks
    private VehicleBrandServiceImpl brandService;

    @Test
    void create_shouldCallRepository() {
        VehicleBrand forSave = new VehicleBrand();
        forSave.setName(brandName);
        forSave.setVehicles(new ArrayList<>());
        forSave.setModels(new ArrayList<>());

        Mockito.when(brandRepository.save(Mockito.any(VehicleBrand.class))).thenReturn(forSave);
        VehicleBrand actual = brandService.create(brandName);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(forSave, actual);
        Mockito.verify(brandRepository).save(forSave);
    }

    @Test
    void findByName_shouldCallRepository() {
        Optional<VehicleBrand> brand = Optional.of(Mockito.mock(VehicleBrand.class));

        Mockito.when(brandRepository.findByNameIgnoreCase(brandName)).thenReturn(brand);

        Optional<VehicleBrand> actual = brandService.findByName(brandName);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(brand, actual);
        Mockito.verify(brandRepository).findByNameIgnoreCase(brandName);
    }

    @Test
    void update_shouldCallRepository() {
        VehicleBrand forUpdate = new VehicleBrand();
        forUpdate.setId(UUID.randomUUID());
        forUpdate.setName(brandName);
        forUpdate.setVehicles(new ArrayList<>());
        forUpdate.setModels(new ArrayList<>());

        Mockito.when(brandRepository.save(Mockito.any(VehicleBrand.class))).thenReturn(forUpdate);

        VehicleBrand actual = brandService.update(forUpdate);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(forUpdate, actual);
        Mockito.verify(brandRepository).save(forUpdate);
    }
}
