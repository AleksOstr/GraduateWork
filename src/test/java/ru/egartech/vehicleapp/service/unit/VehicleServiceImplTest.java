package ru.egartech.vehicleapp.service.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.egartech.vehicleapp.api.request.SearchRequest;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.model.*;
import ru.egartech.vehicleapp.repository.VehicleRepository;
import ru.egartech.vehicleapp.service.VehicleServiceImpl;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;
import ru.egartech.vehicleapp.service.interfaces.VehicleCategoryService;
import ru.egartech.vehicleapp.service.interfaces.VehicleModelService;
import ru.egartech.vehicleapp.service.interfaces.VehicleTypeService;
import ru.egartech.vehicleapp.service.response.VehicleResponse;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;
import ru.egartech.vehicleapp.specification.VehicleSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceImplTest {
    private final String brandName = "brand";
    private final String modelName = "model";
    private final String typeName = "type";
    private final String categoryName = "category";
    private final String regNumber = "А001АА111";
    private final String prodYear = "2024";
    private final boolean hasTrailer = false;

    @Mock
    private VehicleBrandService brandService;

    @Mock
    private VehicleModelService modelService;

    @Mock
    private VehicleTypeService typeService;

    @Mock
    private VehicleCategoryService categoryService;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void createWithExistingData_shouldCallServicesAndRepository() {
        Optional<VehicleBrand> brand = Optional.of(Mockito.mock(VehicleBrand.class));
        Optional<VehicleModel> model = Optional.of(Mockito.mock(VehicleModel.class));
        Optional<VehicleType> type = Optional.of(Mockito.mock(VehicleType.class));
        Optional<VehicleCategory> category = Optional.of(Mockito.mock(VehicleCategory.class));

        VehicleRequest request = setupRequest();

        Vehicle vehicleForSave = setupVehicle(brand.get(), model.get(), type.get(), category.get());

        Mockito.when(brandService.findByName(brandName)).thenReturn(brand);
        Mockito.when(modelService.findByName(modelName)).thenReturn(model);
        Mockito.when(categoryService.findByName(categoryName)).thenReturn(category);
        Mockito.when(typeService.findByName(typeName)).thenReturn(type);
        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(regNumber)).thenReturn(Optional.empty());
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(vehicleForSave);

        VehicleResponse actual = vehicleService.create(request);

        Mockito.verify(brandService).findByName(brandName);
        Mockito.verify(modelService).findByName(modelName);
        Mockito.verify(categoryService).findByName(categoryName);
        Mockito.verify(typeService).findByName(typeName);
        Mockito.verify(vehicleRepository).findByRegNumberIgnoreCase(regNumber);
        Mockito.verify(vehicleRepository).save(vehicleForSave);

        Assertions.assertNotNull(actual);
    }

    @Test
    void createWithNewData_shouldCallServicesAndRepository() {
        Optional<VehicleBrand> brand = Optional.empty();
        Optional<VehicleModel> model = Optional.empty();
        Optional<VehicleType> type = Optional.empty();
        Optional<VehicleCategory> category = Optional.empty();
        
        VehicleBrand brandMock = Mockito.mock(VehicleBrand.class);
        VehicleModel modelMock = Mockito.mock(VehicleModel.class);
        VehicleType typeMock = Mockito.mock(VehicleType.class);
        VehicleCategory categoryMock = Mockito.mock(VehicleCategory.class);

        VehicleRequest request = setupRequest();
        Vehicle vehicleForSave = setupVehicle(brandMock, modelMock, typeMock, categoryMock);

        Mockito.when(brandService.findByName(brandName)).thenReturn(brand);
        Mockito.when(brandService.create(brandName)).thenReturn(brandMock);
        Mockito.when(modelService.findByName(modelName)).thenReturn(model);
        Mockito.when(modelService.create(brandName, modelName)).thenReturn(modelMock);
        Mockito.when(categoryService.findByName(categoryName)).thenReturn(category);
        Mockito.when(categoryService.create(categoryName)).thenReturn(categoryMock);
        Mockito.when(typeService.findByName(typeName)).thenReturn(type);
        Mockito.when(typeService.create(typeName)).thenReturn(typeMock);
        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(regNumber)).thenReturn(Optional.empty());
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(vehicleForSave);

        VehicleResponse actual = vehicleService.create(request);

        Mockito.verify(brandService).findByName(brandName);
        Mockito.verify(brandService).create(brandName);
        Mockito.verify(modelService).findByName(modelName);
        Mockito.verify(modelService).create(brandName,modelName);
        Mockito.verify(categoryService).findByName(categoryName);
        Mockito.verify(categoryService).create(categoryName);
        Mockito.verify(typeService).findByName(typeName);
        Mockito.verify(typeService).create(typeName);
        Mockito.verify(vehicleRepository).findByRegNumberIgnoreCase(regNumber);
        Mockito.verify(vehicleRepository).save(vehicleForSave);

        Assertions.assertNotNull(actual);
    }

    @Test
    void create_shouldThrowException() {
        VehicleRequest request = setupRequest();

        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(request.getRegNumber())).thenReturn(Optional.of(Mockito.mock(Vehicle.class)));

        Assertions.assertThrows(ExistingValueException.class,  () -> vehicleService.create(request));
        Mockito.verify(vehicleRepository).findByRegNumberIgnoreCase(request.getRegNumber());
    }

    @Test
    void updateWithExistingData_shouldCallServicesAndRepository() {
        Optional<VehicleBrand> brandOptional = Optional.of(Mockito.mock(VehicleBrand.class));
        Optional<VehicleModel> modelOptional = Optional.of(Mockito.mock(VehicleModel.class));
        Optional<VehicleType> typeOptional = Optional.of(Mockito.mock(VehicleType.class));
        Optional<VehicleCategory> categoryOptional = Optional.of(Mockito.mock(VehicleCategory.class));

        VehicleRequest request = setupRequest();

        Vehicle vehicleMock = Mockito.mock(Vehicle.class);
        Mockito.when(vehicleMock.getBrand()).thenReturn(brandOptional.get());
        Mockito.when(vehicleMock.getModel()).thenReturn(modelOptional.get());
        Mockito.when(vehicleMock.getType()).thenReturn(typeOptional.get());
        Mockito.when(vehicleMock.getCategory()).thenReturn(categoryOptional.get());
        Mockito.when(vehicleMock.getRegNumber()).thenReturn(regNumber);

        VehicleBrand brandMock = brandOptional.get();
        Mockito.when(brandMock.getName()).thenReturn("");
        Mockito.when(brandMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));
        VehicleModel modelMock = modelOptional.get();
        Mockito.when(modelMock.getName()).thenReturn("");
        Mockito.when(modelMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));
        VehicleType typeMock = typeOptional.get();
        Mockito.when(typeMock.getName()).thenReturn("");
        Mockito.when(typeMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));
        VehicleCategory categoryMock = categoryOptional.get();
        Mockito.when(categoryMock.getName()).thenReturn("");
        Mockito.when(categoryMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));

        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(regNumber)).thenReturn(Optional.of(vehicleMock));
        Mockito.when(vehicleRepository.save(vehicleMock)).thenReturn(vehicleMock);
        Mockito.when(brandService.findByName(brandName)).thenReturn(brandOptional);
        Mockito.when(modelService.findByName(modelName)).thenReturn(modelOptional);
        Mockito.when(typeService.findByName(typeName)).thenReturn(typeOptional);
        Mockito.when(categoryService.findByName(categoryName)).thenReturn(categoryOptional);

        VehicleResponse actual = vehicleService.update(request, regNumber);

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleRepository).findByRegNumberIgnoreCase(regNumber);
        Mockito.verify(vehicleRepository).save(vehicleMock);
        Mockito.verify(brandService).update(brandMock);
        Mockito.verify(brandService).findByName(brandName);
        Mockito.verify(modelService).update(modelMock);
        Mockito.verify(modelService).findByName(modelName);
        Mockito.verify(typeService).update(typeMock);
        Mockito.verify(typeService).findByName(typeName);
        Mockito.verify(categoryService).update(categoryMock);
        Mockito.verify(categoryService).findByName(categoryName);
    }

    @Test
    void updateWithNewData_shouldCallServicesAndRepository() {
        Optional<VehicleBrand> brandOptional = Optional.empty();
        Optional<VehicleModel> modelOptional = Optional.empty();
        Optional<VehicleType> typeOptional = Optional.empty();
        Optional<VehicleCategory> categoryOptional = Optional.empty();

        VehicleBrand brandMock = Mockito.mock(VehicleBrand.class);
        VehicleModel modelMock = Mockito.mock(VehicleModel.class);
        VehicleType typeMock = Mockito.mock(VehicleType.class);
        VehicleCategory categoryMock = Mockito.mock(VehicleCategory.class);

        VehicleRequest request = setupRequest();

        Vehicle vehicleMock = Mockito.mock(Vehicle.class);
        Mockito.when(vehicleMock.getBrand()).thenReturn(brandMock);
        Mockito.when(vehicleMock.getModel()).thenReturn(modelMock);
        Mockito.when(vehicleMock.getType()).thenReturn(typeMock);
        Mockito.when(vehicleMock.getCategory()).thenReturn(categoryMock);
        Mockito.when(vehicleMock.getRegNumber()).thenReturn(regNumber);

        Mockito.when(brandMock.getName()).thenReturn("");
        Mockito.when(brandMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));
        Mockito.when(modelMock.getName()).thenReturn("");
        Mockito.when(modelMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));
        Mockito.when(typeMock.getName()).thenReturn("");
        Mockito.when(typeMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));
        Mockito.when(categoryMock.getName()).thenReturn("");
        Mockito.when(categoryMock.getVehicles()).thenReturn(new ArrayList<>(List.of(vehicleMock)));

        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(regNumber)).thenReturn(Optional.of(vehicleMock));
        Mockito.when(vehicleRepository.save(vehicleMock)).thenReturn(vehicleMock);
        Mockito.when(brandService.findByName(brandName)).thenReturn(brandOptional);
        Mockito.when(brandService.create(brandName)).thenReturn(brandMock);
        Mockito.when(modelService.findByName(modelName)).thenReturn(modelOptional);
        Mockito.when(modelService.create(brandName, modelName)).thenReturn(modelMock);
        Mockito.when(typeService.findByName(typeName)).thenReturn(typeOptional);
        Mockito.when(typeService.create(typeName)).thenReturn(typeMock);
        Mockito.when(categoryService.findByName(categoryName)).thenReturn(categoryOptional);
        Mockito.when(categoryService.create(categoryName)).thenReturn(categoryMock);

        VehicleResponse actual = vehicleService.update(request, regNumber);

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleRepository).findByRegNumberIgnoreCase(regNumber);
        Mockito.verify(vehicleRepository).save(vehicleMock);
        Mockito.verify(brandService, Mockito.times(2)).update(brandMock);
        Mockito.verify(brandService).findByName(brandName);
        Mockito.verify(brandService).create(brandName);
        Mockito.verify(modelService, Mockito.times(2)).update(modelMock);
        Mockito.verify(modelService).findByName(modelName);
        Mockito.verify(modelService).create(brandName, modelName);
        Mockito.verify(typeService, Mockito.times(2)).update(typeMock);
        Mockito.verify(typeService).findByName(typeName);
        Mockito.verify(typeService).create(typeName);
        Mockito.verify(categoryService, Mockito.times(2)).update(categoryMock);
        Mockito.verify(categoryService).findByName(categoryName);
        Mockito.verify(categoryService).create(categoryName);
    }

    @Test
    void update_shouldThrowException() {
        VehicleRequest request = setupRequest();

        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(request.getRegNumber())).thenReturn(Optional.of(Mockito.mock(Vehicle.class)));

        Assertions.assertThrows(ExistingValueException.class,  () -> vehicleService.update(request, regNumber));
        Mockito.verify(vehicleRepository, Mockito.times(2)).findByRegNumberIgnoreCase(request.getRegNumber());
    }

    @Test
    void findAll_shouldCallRepository() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        VehicleBrand brand = Mockito.mock(VehicleBrand.class);
        VehicleModel model = Mockito.mock(VehicleModel.class);
        VehicleType type = Mockito.mock(VehicleType.class);
        VehicleCategory category = Mockito.mock(VehicleCategory.class);
        Mockito.when(vehicle.getBrand()).thenReturn(brand);
        Mockito.when(vehicle.getModel()).thenReturn(model);
        Mockito.when(vehicle.getType()).thenReturn(type);
        Mockito.when(vehicle.getCategory()).thenReturn(category);

        Mockito.when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        List<VehicleResponse> actual = vehicleService.findAll();

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.size());
        Mockito.verify(vehicleRepository).findAll();
    }

    @Test
    void findByRegNumber_shouldCallRepository() {
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        VehicleBrand brand = Mockito.mock(VehicleBrand.class);
        VehicleModel model = Mockito.mock(VehicleModel.class);
        VehicleType type = Mockito.mock(VehicleType.class);
        VehicleCategory category = Mockito.mock(VehicleCategory.class);
        Mockito.when(vehicle.getBrand()).thenReturn(brand);
        Mockito.when(vehicle.getModel()).thenReturn(model);
        Mockito.when(vehicle.getType()).thenReturn(type);
        Mockito.when(vehicle.getCategory()).thenReturn(category);

        Mockito.when(vehicleRepository.findByRegNumberIgnoreCase(regNumber)).thenReturn(Optional.of(vehicle));

        VehicleResponse actual = vehicleService.findByRegNumber(regNumber);

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleRepository).findByRegNumberIgnoreCase(regNumber);
    }

    @Test
    void findAllByRequest_shouldCallRepository() {
        SearchRequest request = Mockito.mock(SearchRequest.class);
        Vehicle vehicle = Mockito.mock(Vehicle.class);
        VehicleBrand brand = Mockito.mock(VehicleBrand.class);
        VehicleModel model = Mockito.mock(VehicleModel.class);
        VehicleType type = Mockito.mock(VehicleType.class);
        VehicleCategory category = Mockito.mock(VehicleCategory.class);
        Mockito.when(vehicle.getBrand()).thenReturn(brand);
        Mockito.when(vehicle.getModel()).thenReturn(model);
        Mockito.when(vehicle.getType()).thenReturn(type);
        Mockito.when(vehicle.getCategory()).thenReturn(category);

        Mockito.when(vehicleRepository.findAll(Mockito.any(VehicleSpecification.class))).thenReturn(List.of(vehicle));

        List<VehicleResponse> actual = vehicleService.findAllByRequest(request);

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleRepository).findAll(Mockito.any(VehicleSpecification.class));
    }

    @Test
    void getTypes_shouldCallService() {
        VehicleTypeResponse response = Mockito.mock(VehicleTypeResponse.class);

        Mockito.when(typeService.findAll()).thenReturn(List.of(response));

        List<VehicleTypeResponse> actual = vehicleService.getTypes();

        Assertions.assertNotNull(actual);
        Mockito.verify(typeService).findAll();
    }

    private VehicleRequest setupRequest() {
        VehicleRequest request = new VehicleRequest();
        request.setBrand(brandName);
        request.setModel(modelName);
        request.setCategory(categoryName);
        request.setType(typeName);
        request.setProdYear(prodYear);
        request.setRegNumber(regNumber);
        request.setHasTrailer(hasTrailer ? "да" : "нет");
        return request;
    }
    
    private Vehicle setupVehicle(VehicleBrand brand, VehicleModel model, VehicleType type, VehicleCategory category) {
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setType(type);
        vehicle.setCategory(category);
        vehicle.setRegNumber(regNumber);
        vehicle.setProdYear(Integer.parseInt(prodYear));
        vehicle.setHasTrailer(hasTrailer);
        return vehicle;
    }
}
