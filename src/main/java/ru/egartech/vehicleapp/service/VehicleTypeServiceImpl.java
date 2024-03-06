package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleTypeRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.VehicleType;
import ru.egartech.vehicleapp.repository.VehicleTypeRepository;
import ru.egartech.vehicleapp.service.interfaces.VehicleTypeService;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService {

    private final VehicleTypeRepository typeRepository;

    @Override
    public VehicleTypeResponse create(VehicleTypeRequest request) throws ExistingValueException{
        String typeName = request.getTypeName();
        if (typeRepository.findByTypeNameIgnoreCase(typeName).isPresent()) {
            throw new ExistingValueException("Type with name: " + " already exists");
        }
        VehicleType type = new VehicleType();
        type.setTypeName(typeName);
        type = typeRepository.save(type);
        return mapToResponse(type);
    }

    @Override
    public VehicleType findByName(VehicleTypeRequest request) throws ValueNotFoundException {
        String typeName = request.getTypeName();
        return typeRepository.findByTypeNameIgnoreCase(typeName)
                .orElseThrow(() -> new ValueNotFoundException("Type with name: " + typeName + " not found"));
    }

    @Override
    public List<VehicleTypeResponse> findAll() {
        List<VehicleType> types = typeRepository.findAll();
        return types.stream().map(this::mapToResponse).toList();
    }

    @Override
    public void updateType(VehicleType type) {
        typeRepository.save(type);
    }

    private VehicleTypeResponse mapToResponse(VehicleType type) {
        VehicleTypeResponse response = new VehicleTypeResponse();
        response.setTypeName(type.getTypeName());
        return response;
    }
}
