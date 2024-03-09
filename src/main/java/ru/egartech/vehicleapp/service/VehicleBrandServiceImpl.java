package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.repository.VehicleBrandRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository brandRepository;
    @Override
    public VehicleBrand create(String brandName) throws ExistingValueException {
        if (brandRepository.findByNameIgnoreCase(brandName).isPresent()) {
            throw new ExistingValueException("Brand with name: " + brandName +
                    " already exists");
        }
        VehicleBrand brand = new VehicleBrand();
        brand.setName(brandName);
        brand.setVehicles(new ArrayList<>());
        brand.setModels(new ArrayList<>());
        return brandRepository.save(brand);
    }

    @Override
    public VehicleBrand findByName(String brandName) throws ValueNotFoundException{
        return brandRepository.findByNameIgnoreCase(brandName)
                .orElseThrow(() -> new ValueNotFoundException("Brand with name: " + brandName + " not found"));
    }

    @Override
    public void updateBrand(VehicleBrand brand) {
        brandRepository.save(brand);
    }

}
