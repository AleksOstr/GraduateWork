package ru.egartech.vehicleapp.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.service.interfaces.VehicleService;
import ru.egartech.vehicleapp.service.interfaces.VehicleTypeService;

@Service
@RequiredArgsConstructor
public class InitH2Data implements CommandLineRunner {

    private final VehicleService vehicleService;
    private final VehicleTypeService typeService;

    @Override
    public void run(String... args) throws Exception {
        createType("Легковой");
        createType("Грузовой");

        createVehicle("LADA", "Vesta", "Легковой", "M", "А001АА123", "2020",
                "нет");
        createVehicle("LADA", "2112", "Легковой", "M", "Р123КУ123", "2007",
                "да");
        createVehicle("SKODA", "Rapid", "Легковой", "M", "К123АР123", "2021",
                "нет");
        createVehicle("KAMAZ", "6520", "Грузовой", "N", "Н321НН123", "2024",
                "нет");
    }

    private void createType(String typeName) {
        typeService.create(typeName);
    }

    private void createVehicle(String brand, String model, String type, String category, String regNumber,
                               String prodYear, String hasTrailer) {
        VehicleRequest request = new VehicleRequest();
        request.setBrand(brand);
        request.setModel(model);
        request.setType(type);
        request.setCategory(category);
        request.setRegNumber(regNumber);
        request.setProdYear(prodYear);
        request.setHasTrailer(hasTrailer);
        vehicleService.create(request);
    }
}
