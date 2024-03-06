package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleBrandRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;

@Service
@RequiredArgsConstructor
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository brandRepository;
    @Override
    public VehicleBrand create(VehicleBrandRequest request) throws ExistingValueException{
        String brandName = request.getBrandName();
        if (brandRepository.findByBrandNameIgnoreCase(brandName).isPresent()) {
            throw new ExistingValueException("Brand with name: " + brandName + " already exists");
        }
        VehicleBrand brand = new VehicleBrand();
        brand.setBrandName(brandName);
        return brandRepository.save(brand);
    }

    @Override
    public VehicleBrand findByBrandName(VehicleBrandRequest request) throws ValueNotFoundException{
        String brandName = request.getBrandName();
        return brandRepository.findByBrandNameIgnoreCase(brandName)
                .orElseThrow(() -> new ValueNotFoundException("Brand with name: " + brandName + " not found"));
    }

    @Override
    public VehicleBrand updateBrand(VehicleBrand brand) {
        return brandRepository.save(brand);
    }
}
