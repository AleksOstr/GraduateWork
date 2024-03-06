package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;
import ru.egartech.vehicleapp.service.response.VehicleBrandResponse;

@Service
@RequiredArgsConstructor
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository brandRepository;
    @Override
    public VehicleBrandResponse create(VehicleBrandRequest request) throws ExistingValueException{
        String brandName = request.getBrandName();
        if (brandRepository.findByBrandNameIgnoreCase(brandName).isPresent()) {
            throw new ExistingValueException("Brand with name: " + brandName + " already exists");
        }
        VehicleBrand brand = new VehicleBrand();
        brand.setBrandName(brandName);
        brand = brandRepository.save(brand);
        return mapToResponse(brand);
    }

    @Override
    public VehicleBrandResponse findByBrandName(VehicleBrandRequest request) throws ValueNotFoundException{
        String brandName = request.getBrandName();
        VehicleBrand brand = brandRepository.findByBrandNameIgnoreCase(brandName)
                .orElseThrow(() -> new ValueNotFoundException("Brand with name: " + brandName + " not found"));
        return mapToResponse(brand);
    }

    @Override
    public void updateBrand(VehicleBrand brand) {
        brandRepository.save(brand);
    }

    private VehicleBrandResponse mapToResponse(VehicleBrand brand) {
        VehicleBrandResponse response = new VehicleBrandResponse();
        response.setBrandName(brand.getBrandName());
        return response;
    }
}
