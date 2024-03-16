package ru.gb.vehicleapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.vehicleapp.api.request.SearchRequest;
import ru.gb.vehicleapp.api.request.VehicleRequest;
import ru.gb.vehicleapp.exceptions.ExistingValueException;
import ru.gb.vehicleapp.model.*;
import ru.gb.vehicleapp.repository.VehicleRepository;
import ru.gb.vehicleapp.service.interfaces.*;
import ru.gb.vehicleapp.service.response.VehicleResponse;
import ru.gb.vehicleapp.service.response.VehicleTypeResponse;
import ru.gb.vehicleapp.specification.VehicleSpecification;

import java.util.List;
import java.util.Optional;

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
     *
     * @param request - запрос с параметрами создаваемого ТС
     * @return - VehicleResponse - response-объект с параметрами созданного ТС
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
     *
     * @param request   - запрос с обновляемыми параметрами
     * @param regNumber - гос. номер обновляемого ТС
     * @return - VehicleResponse - response-объект с параметрами созданного ТС
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
            brandService.update(oldBrand);
            setBrand(request.getBrand(), vehicle);
        }
        if (!vehicle.getModel().getName().equalsIgnoreCase(request.getModel())) {
            VehicleModel oldModel = vehicle.getModel();
            oldModel.getVehicles().remove(vehicle);
            modelService.update(oldModel);
            setModel(request.getModel(), request.getBrand(), vehicle);
        }
        if (!vehicle.getType().getName().equalsIgnoreCase(request.getType())) {
            VehicleType oldType = vehicle.getType();
            oldType.getVehicles().remove(vehicle);
            typeService.update(oldType);
            setType(request.getType(), vehicle);
        }
        if (!vehicle.getCategory().getName().equals(request.getCategory())) {
            VehicleCategory oldCategory = vehicle.getCategory();
            oldCategory.getVehicles().remove(vehicle);
            categoryService.update(oldCategory);
            setCategory(request.getCategory(), vehicle);
        }
        vehicle = vehicleRepository.save(vehicle);
        return mapToResponse(vehicle);
    }

    /**
     * Получение всех ТС из БД
     *
     * @return List - список response-объектов с параметрами ТС
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    /**
     * Получение ТС из БД по запросу с параметрами поиска
     *
     * @param request - запрос с параметрами поиска
     * @return List - список response-объектов с параметрами ТС
     */
    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> findAllByRequest(SearchRequest request) {
        final Specification<Vehicle> specification = new VehicleSpecification(request);
        return vehicleRepository.findAll(specification)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Получение ТС из БД по гос. номеру
     *
     * @param regNumber - запрос с параметрами поиска
     * @return List - список response-объектов с параметрами ТС
     */
    @Override
    @Transactional(readOnly = true)
    public VehicleResponse findByRegNumber(String regNumber) {
        Vehicle vehicle = vehicleRepository.findByRegNumberIgnoreCase(regNumber).orElseThrow();
        return mapToResponse(vehicle);
    }

    /**
     * Получение списка типов ТС
     *
     * @return - список типов ТС
     */
    @Override
    public List<VehicleTypeResponse> getTypes() {
        return typeService.findAll();
    }

    /**
     * Маппер Vehicle в VehicleResponse
     *
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
        response.setHasTrailer(vehicle.isHasTrailer());
        return response;
    }

    /**
     * Проверка гос. номера ТС на наличие в БД
     *
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
     *
     * @param brandName имя ямарки ТС
     * @param vehicle   сущность ТС
     */
    private void setBrand(String brandName, Vehicle vehicle) {
        Optional<VehicleBrand> optionalBrand = brandService.findByName(brandName);
        if (optionalBrand.isPresent()) {
            VehicleBrand brand = optionalBrand.get();
            vehicle.setBrand(brand);
            brand.getVehicles().add(vehicle);
        } else {
            VehicleBrand brand = brandService.create(brandName);
            vehicle.setBrand(brand);
            brand.getVehicles().add(vehicle);
            brandService.update(brand);
        }
    }

    /**
     * Установка модели ТС
     *
     * @param modelName имя модели ТС
     * @param brandName имя марки ТС
     * @param vehicle   сущность ТС
     */
    private void setModel(String modelName, String brandName, Vehicle vehicle) {
        Optional<VehicleModel> optionalModel = modelService.findByName(modelName);
        if (optionalModel.isPresent()) {
            VehicleModel model = optionalModel.get();
            vehicle.setModel(model);
            model.getVehicles().add(vehicle);
        } else {
            VehicleModel model = modelService.create(brandName, modelName);
            vehicle.setModel(model);
            model.getVehicles().add(vehicle);
            modelService.update(model);
        }
    }

    /**
     * Установка типа ТС
     *
     * @param typeName тип ТС
     * @param vehicle  сущность ТС
     */
    private void setType(String typeName, Vehicle vehicle) {
        Optional<VehicleType> optionalType = typeService.findByName(typeName);
        if (optionalType.isPresent()) {
            VehicleType type = optionalType.get();
            vehicle.setType(type);
            type.getVehicles().add(vehicle);
        } else {
            VehicleType type = typeService.create(typeName);
            vehicle.setType(type);
            type.getVehicles().add(vehicle);
            typeService.update(type);
        }
    }

    /**
     * Установка категории ТС
     *
     * @param categoryName категория ТС
     * @param vehicle      сущность ТС
     */
    private void setCategory(String categoryName, Vehicle vehicle) {
        Optional<VehicleCategory> optionalCategory = categoryService.findByName(categoryName);
        if (optionalCategory.isPresent()) {
            VehicleCategory category = optionalCategory.get();
            vehicle.setCategory(category);
            category.getVehicles().add(vehicle);
        } else {
            VehicleCategory category = categoryService.create(categoryName);
            vehicle.setCategory(category);
            category.getVehicles().add(vehicle);
            categoryService.update(category);
        }
    }

}
