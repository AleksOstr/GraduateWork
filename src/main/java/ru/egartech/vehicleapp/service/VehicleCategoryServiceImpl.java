package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleCategory;
import ru.egartech.vehicleapp.repository.VehicleCategoryRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleCategoryService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository categoryRepository;


    @Override
    public VehicleCategory create(String categoryName) throws ExistingValueException {
        if (categoryRepository.findByNameIgnoreCase(categoryName).isPresent()) {
            throw new ExistingValueException("Category with name: " + categoryName +
                    " already exists");
        }
        VehicleCategory category = new VehicleCategory();
        category.setName(categoryName);
        category.setVehicles(new ArrayList<>());
        return categoryRepository.save(category);
    }

    @Override
    public VehicleCategory findByName(String categoryName) throws ValueNotFoundException {
        return categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new ValueNotFoundException("Category with name: " + categoryName + " not found"));
    }


    @Override
    public void updateCategory(VehicleCategory category) {
        categoryRepository.save(category);
    }
}
