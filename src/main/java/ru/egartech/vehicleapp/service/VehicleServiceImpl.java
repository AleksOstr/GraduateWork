package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.NullValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.*;
import ru.egartech.vehicleapp.repository.VehicleRepository;
import ru.egartech.vehicleapp.service.interfaces.*;
import ru.egartech.vehicleapp.service.response.VehicleResponse;
import ru.egartech.vehicleapp.specification.VehicleSpecification;

import java.util.List;
import java.util.UUID;

import static ru.egartech.vehicleapp.specification.VehicleSpecification.*;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleBrandService brandService;
    private final VehicleModelService modelService;
    private final VehicleTypeService typeService;
    private final VehicleCategoryService categoryService;

    @Override
    public VehicleResponse create(VehicleRequest request) throws ExistingValueException, NullValueException {
        validateRequest(request);
        checkRegNumber(request);

        Vehicle vehicle = new Vehicle();
        setBrand(request, vehicle);
        setModel(request, vehicle);
        setType(request, vehicle);
        setCategory(request, vehicle);
        vehicle.setRegNumber(request.getRegNumber());
        vehicle.setProdYear(Integer.parseInt(request.getProdYear()));
        vehicle.setHasTrailer(request.getHasTrailer().equalsIgnoreCase("да"));
        Vehicle saved = vehicleRepository.save(vehicle);

        return mapToResponse(saved);
    }

    @Override
    public VehicleResponse update(VehicleRequest request, UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow();
        validateRequest(request);
        if (!request.getRegNumber().equalsIgnoreCase(vehicle.getRegNumber())) {
            checkRegNumber(request);
            vehicle.setRegNumber(request.getRegNumber());
        }
        if (!(Integer.parseInt(request.getProdYear()) == vehicle.getProdYear())) {
            vehicle.setProdYear(Integer.parseInt(request.getProdYear()));
        }
        vehicle.setHasTrailer(request.getHasTrailer().equals("да"));
        if (!vehicle.getBrand().getName().equalsIgnoreCase(request.getBrand())) {
            VehicleBrand oldBrand = vehicle.getBrand();
            oldBrand.getVehicles().remove(vehicle);
            brandService.updateBrand(oldBrand);
            setBrand(request, vehicle);
        }
        if (!vehicle.getModel().getName().equalsIgnoreCase(request.getModel())) {
            VehicleModel oldModel = vehicle.getModel();
            oldModel.getVehicles().remove(vehicle);
            modelService.updateModel(oldModel);
            setModel(request, vehicle);
        }
        if (!vehicle.getType().getName().equalsIgnoreCase(request.getType())) {
            VehicleType oldType = vehicle.getType();
            oldType.getVehicles().remove(vehicle);
            typeService.updateType(oldType);
            setType(request, vehicle);
        }
        if (!vehicle.getCategory().getName().equals(request.getCategory())) {
            VehicleCategory oldCategory = vehicle.getCategory();
            oldCategory.getVehicles().remove(vehicle);
            categoryService.updateCategory(oldCategory);
            setCategory(request, vehicle);
        }
        vehicle = vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    @Override
    public List<VehicleResponse> findAllByRequest(VehicleRequest request) {
        Specification<Vehicle> specification =
                VehicleSpecification.byBrand(request.getBrand())
                        .and(byModel(request.getModel()))
                        .and(byCategory(request.getCategory()))
                        .and(byRegNumber(request.getRegNumber()))
                        .and(byProdYear(request.getProdYear()));
        return vehicleRepository.findAll(specification).stream().map(this::mapToResponse).toList();
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setBrand(vehicle.getBrand().getName());
        response.setModel(vehicle.getModel().getName());
        response.setCategory(vehicle.getCategory().getName());
        response.setType(vehicle.getType().getName());
        response.setProdYear(vehicle.getProdYear());
        response.setRegNumber(vehicle.getRegNumber());
        response.setHasTrailer(vehicle.isHasTrailer() ? "да" : "нет");
        return response;
    }

    private void checkRegNumber(VehicleRequest request) {
        if (vehicleRepository.findByRegNumberIgnoreCase(request.getRegNumber()).isPresent()) {
            throw new ExistingValueException("Vehicle with registration number: " + request.getRegNumber() +
                    "already exists");
        }
    }

    private void validateRequest(VehicleRequest request) {
        if (
                request.getBrand().isEmpty() || request.getModel().isEmpty() || request.getCategory().isEmpty() ||
                        request.getType().isEmpty() || request.getRegNumber().isEmpty() || request.getProdYear() == null ||
                        request.getHasTrailer().isEmpty()
        ) {
            throw new NullValueException("All fields of request must be filled");
        }
    }

    private void setBrand(VehicleRequest request, Vehicle vehicle) {
        try {
            VehicleBrand brand = brandService.findByName(request.getBrand());
            vehicle.setBrand(brand);
            brand.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleBrand brand = brandService.create(request.getBrand());
            vehicle.setBrand(brand);
            brand.getVehicles().add(vehicle);
            brandService.updateBrand(brand);
        }
    }

    private void setModel(VehicleRequest request, Vehicle vehicle) {
        try {
            VehicleModel model = modelService.findByName(request.getModel());
            vehicle.setModel(model);
            model.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleModel model = modelService.create(request.getBrand(), request.getModel());
            vehicle.setModel(model);
            model.getVehicles().add(vehicle);
            modelService.updateModel(model);
        }
    }

    private void setType(VehicleRequest request, Vehicle vehicle) {
        try {
            VehicleType type = typeService.findByName(request.getType());
            vehicle.setType(type);
            type.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleType type = typeService.create(request.getType());
            vehicle.setType(type);
            type.getVehicles().add(vehicle);
            typeService.updateType(type);
        }
    }

    private void setCategory(VehicleRequest request, Vehicle vehicle) {
        try {
            VehicleCategory category = categoryService.findByName(request.getCategory());
            vehicle.setCategory(category);
            category.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleCategory category = categoryService.create(request.getCategory());
            vehicle.setCategory(category);
            category.getVehicles().add(vehicle);
            categoryService.updateCategory(category);
        }
    }

}
