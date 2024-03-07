package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.model.VehicleBrand;

/**
 * Сервис марок ТС
 */
public interface VehicleBrandService {
    /**
     * Создание новой марки
     * @param brandName - имя марки ТС
     * @return VehicleBrand - сущность марки ТС
     */
    VehicleBrand create(String brandName);

    /**
     * Поиск марки по имени
     * @param brandName - имя марки ТС
     * @return VehicleBrand - сущность марки ТС
     */
    VehicleBrand findByName(String brandName);

    /**
     * Обновление марки в репозитории
     * @param brand - сущность марки ТС
     */
    void updateBrand(VehicleBrand brand);
}
