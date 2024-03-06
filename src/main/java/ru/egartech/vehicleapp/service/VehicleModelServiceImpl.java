package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;
import ru.egartech.vehicleapp.api.request.VehicleModelRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.model.VehicleModel;
import ru.egartech.vehicleapp.repository.VehicleModelRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;
import ru.egartech.vehicleapp.service.interfaces.VehicleModelService;
import ru.egartech.vehicleapp.service.response.VehicleModelResponse;

@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository modelRepository;
    private final VehicleBrandService brandService;

    @Override
    public VehicleModelResponse create(VehicleModelRequest request) throws ExistingValueException {
        String modelName = request.getModelName();
        if (modelRepository.findByModelNameIgnoreCase(modelName).isPresent()) {
            throw new ExistingValueException("Model with name: " + modelName + " already exists");
        }
        VehicleBrand brand = getBrand(request.getBrandName());
        VehicleModel model = new VehicleModel();
        model.setModelName(modelName);
        model.setBrand(brand);
        model = modelRepository.save(model);
        brand.getModels().add(model);
        brandService.updateBrand(brand);
        return mapToResponse(model);
    }

    @Override
    public VehicleModel findByModelName(VehicleModelRequest request) throws ValueNotFoundException{
        String modelName = request.getModelName();

        return modelRepository.findByModelNameIgnoreCase(modelName)
                .orElseThrow(() -> new ValueNotFoundException("Model with name: " + modelName + " not found"));
    }

    @Override
    public void updateModel(VehicleModel model) {
        modelRepository.save(model);
    }

    private VehicleBrand getBrand(String brandName) throws ValueNotFoundException{
        VehicleBrandRequest brandRequest = new VehicleBrandRequest();
        brandRequest.setBrandName(brandRequest.getBrandName());
        return brandService.findByBrandName(brandRequest);
    }

    private VehicleModelResponse mapToResponse(VehicleModel model) {
        VehicleModelResponse response = new VehicleModelResponse();
        response.setModelName(model.getModelName());
        return response;
    }
}
