package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleType;
import ru.egartech.vehicleapp.repository.VehicleTypeRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleTypeService;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository typeRepository;

    @Override
    public VehicleType create(String typeName) throws ExistingValueException {
        if (typeRepository.findByNameIgnoreCase(typeName).isPresent()) {
            throw new ExistingValueException("Category with name: " + typeName +
                    " already exists");
        }
        VehicleType type = new VehicleType();
        type.setName(typeName);
        type.setVehicles(new ArrayList<>());
        return typeRepository.save(type);
    }

    @Override
    public VehicleType findByName(String typeName) throws ValueNotFoundException {
        return typeRepository.findByNameIgnoreCase(typeName)
                .orElseThrow(() -> new ValueNotFoundException("Type with name: " + typeName + " not found"));
    }

    @Override
    public List<VehicleTypeResponse> findAll() {
        List<VehicleType> types = typeRepository.findAll();
        return types.stream().map(this::mapToResponse).toList();
    }

    @Override
    public VehicleType updateType(VehicleType type) {
        return typeRepository.save(type);
    }

    private VehicleTypeResponse mapToResponse(VehicleType type) {
        VehicleTypeResponse response = new VehicleTypeResponse();
        response.setTypeName(type.getName());
        return response;
    }
}
