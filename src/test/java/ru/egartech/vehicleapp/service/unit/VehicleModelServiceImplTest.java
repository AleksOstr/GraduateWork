package ru.egartech.vehicleapp.service.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.model.VehicleModel;
import ru.egartech.vehicleapp.repository.VehicleModelRepository;
import ru.egartech.vehicleapp.service.VehicleModelServiceImpl;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class VehicleModelServiceImplTest {

    private final String brandName = "name";
    private final String modelName = "name";

    @Mock
    private VehicleBrandService brandService;

    @Mock
    private VehicleModelRepository modelRepository;

    @InjectMocks
    private VehicleModelServiceImpl modelService;

    @Test
    void create_shouldCallRepository() {
        VehicleBrand brand = Mockito.mock(VehicleBrand.class);
        VehicleModel forSave = new VehicleModel();
        forSave.setName(modelName);
        forSave.setBrand(brand);
        forSave.setVehicles(new ArrayList<>());

        Mockito.when(brandService.findByName(brandName)).thenReturn(Optional.of(brand));
        Mockito.when(modelRepository.save(Mockito.any(VehicleModel.class))).thenReturn(forSave);

        VehicleModel actual = modelService.create(brandName, modelName);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(forSave, actual);
        Mockito.verify(brandService).findByName(brandName);
        Mockito.verify(modelRepository).save(forSave);
    }

    @Test
    void findByName_shouldCallRepository() {
        Optional<VehicleModel> model = Optional.of(Mockito.mock(VehicleModel.class));

        Mockito.when(modelRepository.findByNameIgnoreCase(modelName)).thenReturn(model);

        Optional<VehicleModel> actual = modelService.findByName(modelName);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(model, actual);
        Mockito.verify(modelRepository).findByNameIgnoreCase(modelName);
    }

    @Test
    void update_shouldCallRepository() {
        VehicleBrand brand = Mockito.mock(VehicleBrand.class);
        VehicleModel forUpdate = new VehicleModel();
        forUpdate.setId(UUID.randomUUID());
        forUpdate.setName(modelName);
        forUpdate.setBrand(brand);
        forUpdate.setVehicles(new ArrayList<>());

        Mockito.when(modelRepository.save(Mockito.any(VehicleModel.class))).thenReturn(forUpdate);

        VehicleModel actual = modelService.update(forUpdate);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(forUpdate, actual);
        Mockito.verify(modelRepository).save(forUpdate);
    }
}
