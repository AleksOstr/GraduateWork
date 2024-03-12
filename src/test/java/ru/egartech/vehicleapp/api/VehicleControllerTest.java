package ru.egartech.vehicleapp.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.egartech.vehicleapp.api.request.SearchRequest;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.service.interfaces.VehicleService;
import ru.egartech.vehicleapp.service.response.VehicleResponse;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController controller;

    @Test
    void getMainPaige_shouldCallService() {
        VehicleResponse response = Mockito.mock(VehicleResponse.class);

        Mockito.when(vehicleService.findAll()).thenReturn(List.of(response));

        String actual = controller.getMainPaige(Mockito.mock(Model.class));

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleService).findAll();
    }

    @Test
    void filter_shouldCallService() {
        VehicleResponse response = Mockito.mock(VehicleResponse.class);

        Mockito.when(vehicleService.findAllByRequest(Mockito.any(SearchRequest.class))).thenReturn(List.of(response));

        String actual = controller.filter(Mockito.mock(Model.class), Mockito.mock(SearchRequest.class));

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleService).findAllByRequest(Mockito.any(SearchRequest.class));
    }

    @Test
    void getCreationPaige_shouldCallService() {
        VehicleTypeResponse response = Mockito.mock(VehicleTypeResponse.class);

        Mockito.when(vehicleService.getTypes()).thenReturn(List.of(response));

        String actual = controller.getCreationPage(Mockito.mock(Model.class));

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleService).getTypes();
    }

    @Test
    void createVehicle_shouldCallService() {
        String actual = controller.createVehicle(Mockito.mock(VehicleRequest.class));

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleService).create(Mockito.any(VehicleRequest.class));
    }

    @Test
    void getUpdatePaige_shouldCallService() {
        String regNumber = "regNumber";
        VehicleTypeResponse typeResponse = Mockito.mock(VehicleTypeResponse.class);
        VehicleResponse vehicleResponse = Mockito.mock(VehicleResponse.class);

        Mockito.when(vehicleService.findByRegNumber(regNumber)).thenReturn(vehicleResponse);
        Mockito.when(vehicleService.getTypes()).thenReturn(List.of(typeResponse));

        String actual = controller.getUpdatePage(regNumber, Mockito.mock(Model.class));

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleService).findByRegNumber(regNumber);
        Mockito.verify(vehicleService).getTypes();
    }

    @Test
    void updateVehicle() {
        String regNumber = "regNumber";
        VehicleRequest request = Mockito.mock(VehicleRequest.class);
        String actual = controller.updateVehicle(regNumber, request);

        Assertions.assertNotNull(actual);
        Mockito.verify(vehicleService).update(request, regNumber);
    }
}
