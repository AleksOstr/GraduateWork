package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.model.VehicleModel;
import ru.egartech.vehicleapp.repository.VehicleModelRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleBrandService;
import ru.egartech.vehicleapp.service.interfaces.VehicleModelService;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Сервис моделей ТС
 */
@Service
@RequiredArgsConstructor
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository modelRepository;
    private final VehicleBrandService brandService;

    /**
     * Создание новой модели ТС
     * @param brandName - имя марки ТС
     * @param modelName - имя модели ТС
     * @return - VehicleModel сущность модели ТС
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

    /**
     * Поиск модели ТС по имени
     * @param modelName - имя модепли ТС
     * @return - VehicleModel сущность модели ТС
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleModel> findByName(String modelName) {
        return modelRepository.findByNameIgnoreCase(modelName);
    }

    /**
     * Обновление модели ТС
     * @param model - модель ТС
     */
    @Override
    public VehicleModel update(VehicleModel model) {
        return modelRepository.save(model);
    }

    /**
     * Получение марки ТС по имени
     * @param brandName имя марки ТС
     * @return марка ТС
     * @throws ValueNotFoundException если марка ТС не найдена
     */
    private VehicleBrand getBrand(String brandName) throws ValueNotFoundException {
        return brandService.findByName(brandName).orElseThrow();
    }
}
