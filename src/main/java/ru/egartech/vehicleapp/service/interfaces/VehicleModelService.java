package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.model.VehicleModel;

/**
 * Сервис моделей ТС
 */
public interface VehicleModelService {

    /**
     * Создание новой модели ТС
     * @param brandName - имя марки ТС
     * @param modelName - имя модели ТС
     * @return - VehicleModel сущность модели ТС
     */
    VehicleModel create(String brandName, String modelName);

    /**
     * Поиск модели ТС по имени
     * @param modelName - имя модепли ТС
     * @return - VehicleModel сущность модели ТС
     */
    VehicleModel findByName(String modelName);

    /**
     * Обновление модели ТС
     * @param model - модель ТС
     */
    VehicleModel updateModel(VehicleModel model);

}
