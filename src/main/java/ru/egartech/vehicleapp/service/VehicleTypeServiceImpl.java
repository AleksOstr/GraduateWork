package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.model.VehicleType;
import ru.egartech.vehicleapp.repository.VehicleTypeRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleTypeService;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис типов ТС
 */
@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository typeRepository;

    /**
     * Создание нового типа ТС
     * @param typeName - имя типа ТС
     * @return - VehicleType - сущность типа ТС
     */
    @Override
    public VehicleType create(String typeName) throws ExistingValueException {
        if (typeRepository.findByNameIgnoreCase(typeName).isPresent()) {
            throw new ExistingValueException("Type with name: " + typeName +
                    " already exists");
        }
        VehicleType type = new VehicleType();
        type.setName(typeName);
        type.setVehicles(new ArrayList<>());
        return typeRepository.save(type);
    }

    /**
     * Пооиск типа ТС по имени
     *
     * @param typeName - имя типа ТС
     * @return - VehicleType - сущность типа ТС
     */
    @Override
    public Optional<VehicleType> findByName(String typeName) {
        return typeRepository.findByNameIgnoreCase(typeName);
    }

    /**
     * Получение всех типов ТС из БД
     * @return List - список типов ТС
     */
    @Override
    public List<VehicleTypeResponse> findAll() {
        List<VehicleType> types = typeRepository.findAll();
        return types.stream().map(this::mapToResponse).toList();
    }

    /**
     * Оновление типа ТС
     * @param type - тип ТС
     */
    @Override
    public VehicleType update(VehicleType type) {
        return typeRepository.save(type);
    }

    /**
     * Маппер VehicleType в VehicleTypeResponse
     * @param type тип ТС
     * @return response-объект с параметрами типа ТС
     */
    private VehicleTypeResponse mapToResponse(VehicleType type) {
        VehicleTypeResponse response = new VehicleTypeResponse();
        response.setTypeName(type.getName());
        return response;
    }
}
