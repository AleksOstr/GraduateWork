package ru.egartech.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.egartech.vehicleapp.api.request.SearchRequest;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.exceptions.ExistingValueException;
import ru.egartech.vehicleapp.exceptions.NullValueException;
import ru.egartech.vehicleapp.exceptions.ValueNotFoundException;
import ru.egartech.vehicleapp.model.*;
import ru.egartech.vehicleapp.repository.VehicleRepository;
import ru.egartech.vehicleapp.service.interfaces.*;
import ru.egartech.vehicleapp.service.response.VehicleResponse;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;
import ru.egartech.vehicleapp.specification.VehicleSpecification;

import java.util.List;

/**
 * Сервис ТС
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleBrandService brandService;
    private final VehicleModelService modelService;
    private final VehicleTypeService typeService;
    private final VehicleCategoryService categoryService;

    /**
     * Создание нового ТС
     * @param request - запрос с параметрами создаваемого ТС
     * @return - VehicleResponse - response-объект с параметрами созданного ТС
     */
    @Override
    public VehicleResponse create(VehicleRequest request) throws ExistingValueException {
        checkRegNumber(request.getRegNumber());

        Vehicle vehicle = new Vehicle();
        setBrand(request.getBrand(), vehicle);
        setModel(request.getModel(), request.getBrand(), vehicle);
        setType(request.getType(), vehicle);
        setCategory(request.getCategory(), vehicle);
        vehicle.setRegNumber(request.getRegNumber());
        vehicle.setProdYear(Integer.parseInt(request.getProdYear()));
        vehicle.setHasTrailer(request.getHasTrailer().equalsIgnoreCase("да"));
        Vehicle saved = vehicleRepository.save(vehicle);

        return mapToResponse(saved);
    }

    /**
     * Обновление параметров ТС
     * @param request - запрос с обновляемыми параметрами
     * @param regNumber - гос. номер обновляемого ТС
     * @return - VehicleResponse - response-объект с параметрами созданного ТС
     */
    @Override
    public VehicleResponse update(VehicleRequest request, String regNumber) {
        Vehicle vehicle = vehicleRepository.findByRegNumberIgnoreCase(regNumber).orElseThrow();
        if (!request.getRegNumber().equalsIgnoreCase(vehicle.getRegNumber())) {
            checkRegNumber(request.getRegNumber());
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
            setBrand(request.getBrand(), vehicle);
        }
        if (!vehicle.getModel().getName().equalsIgnoreCase(request.getModel())) {
            VehicleModel oldModel = vehicle.getModel();
            oldModel.getVehicles().remove(vehicle);
            modelService.updateModel(oldModel);
            setModel(request.getModel(), request.getBrand(), vehicle);
        }
        if (!vehicle.getType().getName().equalsIgnoreCase(request.getType())) {
            VehicleType oldType = vehicle.getType();
            oldType.getVehicles().remove(vehicle);
            typeService.updateType(oldType);
            setType(request.getType(), vehicle);
        }
        if (!vehicle.getCategory().getName().equals(request.getCategory())) {
            VehicleCategory oldCategory = vehicle.getCategory();
            oldCategory.getVehicles().remove(vehicle);
            categoryService.updateCategory(oldCategory);
            setCategory(request.getCategory(), vehicle);
        }
        vehicle = vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    /**
     * Получение всех ТС из БД
     * @return List - список response-объектов с параметрами ТС
     */
    @Override
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    /**
     * Получение ТС из БД по запросу с параметрами поиска
     * @param request - запрос с параметрами поиска
     * @return List - список response-объектов с параметрами ТС
     */
    @Override
    public List<VehicleResponse> findAllByRequest(SearchRequest request) {
        final Specification<Vehicle> specification = new VehicleSpecification(request);
        return vehicleRepository.findAll(specification)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Получение ТС из БД по гос. номеру
     * @param regNumber - запрос с параметрами поиска
     * @return List - список response-объектов с параметрами ТС
     */
    @Override
    public VehicleResponse findByRegNumber(String regNumber) {
        Vehicle vehicle = vehicleRepository.findByRegNumberIgnoreCase(regNumber).orElseThrow();
        return mapToResponse(vehicle);
    }

    /**
     * Получение списка типов ТС
     * @return - список типов ТС
     */
    @Override
    public List<VehicleTypeResponse> getTypes() {
        return typeService.findAll();
    }

    /**
     * Маппер Vehicle в VehicleResponse
     * @param vehicle сущость ТС
     * @return response-объект с параметрами ТС
     */
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

    /**
     * Проверка гос. номера ТС на наличие в БД
     * @param regNumber гос. номер ТС
     */
    private void checkRegNumber(String regNumber) {
        if (vehicleRepository.findByRegNumberIgnoreCase(regNumber).isPresent()) {
            throw new ExistingValueException("Транспортное средство с гос. номером: " + regNumber +
                    " уже существует");
        }
    }

    /**
     * Установка марки ТС
     * @param brandName имя ямарки ТС
     * @param vehicle сущность ТС
     */
    private void setBrand(String brandName, Vehicle vehicle) {
        try {
            VehicleBrand brand = brandService.findByName(brandName);
            vehicle.setBrand(brand);
            brand.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleBrand brand = brandService.create(brandName);
            vehicle.setBrand(brand);
            brand.getVehicles().add(vehicle);
            brandService.updateBrand(brand);
        }
    }

    /**
     * Установка модели ТС
     * @param modelName имя модели ТС
     * @param brandName имя марки ТС
     * @param vehicle сущность ТС
     */
    private void setModel(String modelName, String brandName, Vehicle vehicle) {
        try {
            VehicleModel model = modelService.findByName(modelName);
            vehicle.setModel(model);
            model.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleModel model = modelService.create(brandName, modelName);
            vehicle.setModel(model);
            model.getVehicles().add(vehicle);
            modelService.updateModel(model);
        }
    }

    /**
     * Установка типа ТС
     * @param typeName тип ТС
     * @param vehicle сущность ТС
     */
    private void setType(String typeName, Vehicle vehicle) {
        try {
            VehicleType type = typeService.findByName(typeName);
            vehicle.setType(type);
            type.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleType type = typeService.create(typeName);
            vehicle.setType(type);
            type.getVehicles().add(vehicle);
            typeService.updateType(type);
        }
    }

    /**
     * Установка категории ТС
     * @param categoryName категория ТС
     * @param vehicle сущность ТС
     */
    private void setCategory(String categoryName, Vehicle vehicle) {
        try {
            VehicleCategory category = categoryService.findByName(categoryName);
            vehicle.setCategory(category);
            category.getVehicles().add(vehicle);
        } catch (ValueNotFoundException e) {
            VehicleCategory category = categoryService.create(categoryName);
            vehicle.setCategory(category);
            category.getVehicles().add(vehicle);
            categoryService.updateCategory(category);
        }
    }

}
