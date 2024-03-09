package ru.egartech.vehicleapp.service.interfaces;

import ru.egartech.vehicleapp.api.request.SearchRequest;
import ru.egartech.vehicleapp.api.request.VehicleRequest;
import ru.egartech.vehicleapp.service.response.VehicleResponse;
import ru.egartech.vehicleapp.service.response.VehicleTypeResponse;

import java.util.List;

/**
 * Сервис ТС
 */
public interface VehicleService {

    /**
     * Создание нового ТС
     * @param request - запрос с параметрами создаваемого ТС
     * @return - VehicleResponse - response-объект с параметрами созданного ТС
     */
    VehicleResponse create(VehicleRequest request);

    /**
     * Обновление параметров ТС
     * @param request - запрос с обновляемыми параметрами
     * @param regNumber - гос. номер обновляемого ТС
     * @return - VehicleResponse - response-объект с параметрами созданного ТС
     */
    VehicleResponse update(VehicleRequest request, String regNumber);

    /**
     * Получение всех ТС из БД
     * @return List - список response-объектов с параметрами ТС
     */
    List<VehicleResponse> findAll();

    /**
     * Получение ТС из БД по запросу с параметрами поиска
     * @param request - запрос с параметрами поиска
     * @return List - список response-объектов с параметрами ТС
     */
    List<VehicleResponse> findAllByRequest(SearchRequest request);

    /**
     * Получение ТС из БД по гос. номеру
     * @param regNumber - запрос с параметрами поиска
     * @return List - список response-объектов с параметрами ТС
     */
    VehicleResponse findByRegNumber(String regNumber);

    /**
     * Получение списка типов ТС
     * @return - список типов ТС
     */
    List<VehicleTypeResponse> getTypes();

}
