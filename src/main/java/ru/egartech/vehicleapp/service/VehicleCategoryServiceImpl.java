package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleCategoryRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleCategory;
import ru.egartech.vehicleapp.repository.VehicleCategoryRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleCategoryService;
import ru.egartech.vehicleapp.service.response.VehicleCategoryResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository categoryRepository;


    @Override
    public VehicleCategoryResponse create(VehicleCategoryRequest request) throws ExistingValueException{
        String categoryName = request.getCategoryName();
        if (categoryRepository.findByCategoryNameIgnoreCase(categoryName).isPresent()) {
            throw new ExistingValueException("Category with name: " + categoryName + " already exists");
        }
        VehicleCategory category = new VehicleCategory();
        category.setName(categoryName);
        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    @Override
    public VehicleCategory findByCategoryName(VehicleCategoryRequest request) throws ValueNotFoundException{
        String categoryName = request.getCategoryName();
        return categoryRepository.findByCategoryNameIgnoreCase(categoryName)
                .orElseThrow(() -> new ValueNotFoundException("Category with name: " + categoryName + " not found"));
    }

    @Override
    public List<VehicleCategoryResponse> findAll() {
        List<VehicleCategory> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapToResponse).toList();
    }

    @Override
    public void updateCategory(VehicleCategory category) {
        categoryRepository.save(category);
    }

    private VehicleCategoryResponse mapToResponse(VehicleCategory category) {
        VehicleCategoryResponse response = new VehicleCategoryResponse();
        response.setCategoryName(category.getName());
        return response;
    }
}
