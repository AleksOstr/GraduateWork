package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.model.VehicleModel;
import ru.egartech.vehicleapp.repository.VehicleModelRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;
import ru.egartech.vehicleapp.service.interfaces.VehicleModelService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository modelRepository;
    private final VehicleBrandService brandService;

    @Override
    public VehicleModel create(String brandName, String modelName) throws ExistingValueException {
        if (modelRepository.findByNameIgnoreCase(modelName).isPresent()) {
            throw new ExistingValueException("Model with name: " + modelName +
                    " already exists");
        }
        VehicleBrand brand = getBrand(brandName);
        VehicleModel model = new VehicleModel();
        model.setName(modelName);
        model.setBrand(brand);
        model.setVehicles(new ArrayList<>());
        brand.getModels().add(model);
        return modelRepository.save(model);
    }

    @Override
    public VehicleModel findByName(String modelName) throws ValueNotFoundException {
        return modelRepository.findByNameIgnoreCase(modelName)
                .orElseThrow(() -> new ValueNotFoundException("Model with name: " + modelName + " not found"));
    }

    @Override
    public void updateModel(VehicleModel model) {
        modelRepository.save(model);
    }

    private VehicleBrand getBrand(String brandName) throws ValueNotFoundException {
        return brandService.findByName(brandName);
    }
}
