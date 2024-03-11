package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleCategory;
import ru.egartech.vehicleapp.repository.VehicleCategoryRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleCategoryService;

import java.util.ArrayList;

/**
 * Сервис категорий ТС
 */
@Service
@RequiredArgsConstructor
public class VehicleCategoryServiceImpl implements VehicleCategoryService {

    private final VehicleCategoryRepository categoryRepository;

    /**
     * Создание новой категории ТС
     * @param categoryName - имя катеогрии ТС
     * @return VehicleCategory - сущность категории ТС
     */
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

    /**
     * Поиск категории ТС по имени
     * @param categoryName - имя катеогрии ТС
     * @return VehicleCategory - сущность категории ТС
     */
    @Override
    public VehicleCategory findByName(String categoryName) throws ValueNotFoundException {
        return categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new ValueNotFoundException("Category with name: " + categoryName + " not found"));
    }

    /**
     * Обновление категории ТС в репозитории
     * @param category - категория ТС
     */
    @Override
    public VehicleCategory update(VehicleCategory category) {
        return categoryRepository.save(category);
    }
}
