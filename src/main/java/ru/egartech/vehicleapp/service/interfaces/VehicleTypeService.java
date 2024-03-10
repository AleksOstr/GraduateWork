package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.model.VehicleType;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.List;

/**
 * Сервис типов ТС
 */
public interface VehicleTypeService {

    /**
     * Создание нового типа ТС
     * @param typeName - имя типа ТС
     * @return - VehicleType - сущность типа ТС
     */
    VehicleType create(String typeName);

    /**
     * Пооиск типа ТС по имени
     * @param typeName - имя типа ТС
     * @return - VehicleType - сущность типа ТС
     */
    VehicleType findByName(String typeName);

    /**
     * Получение всех типов ТС из БД
     * @return List - список типов ТС
     */
    List<VehicleTypeResponse> findAll();

    /**
     * Оновление типа ТС
     * @param type - тип ТС
     */
    VehicleType updateType(VehicleType type);
}
