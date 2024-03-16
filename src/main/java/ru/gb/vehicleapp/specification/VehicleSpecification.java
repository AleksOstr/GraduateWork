package ru.gb.vehicleapp.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.gb.vehicleapp.api.request.SearchRequest;
import ru.gb.vehicleapp.model.Vehicle;
import ru.gb.vehicleapp.model.VehicleBrand;
import ru.gb.vehicleapp.model.VehicleCategory;
import ru.gb.vehicleapp.model.VehicleModel;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class VehicleSpecification implements Specification<Vehicle>{

    private final SearchRequest request;

    @Override
    public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final List<Predicate> predicates = new ArrayList<>();
        if (!request.getBrand().isEmpty()) {
            Join<VehicleBrand, Vehicle> vehicleBrand = root.join("brand");
            final Predicate brandName = builder.equal(builder.lower(vehicleBrand.get("name")),
                    request.getBrand().toLowerCase());
            predicates.add(brandName);
        }
        if (!request.getModel().isEmpty()) {
            Join<VehicleModel, Vehicle> vehicleModel = root.join("model");
            final Predicate modelName = builder.equal(builder.lower(vehicleModel.get("name")),
                    request.getModel().toLowerCase());
            predicates.add(modelName);
        }
        if (!request.getCategory().isEmpty()) {
            Join<VehicleCategory, Vehicle> vehicleCategory = root.join("category");
            final Predicate categoryName = builder.equal(builder.lower(vehicleCategory.get("name")),
                    request.getCategory().toLowerCase());
            predicates.add(categoryName);
        }
        if (!request.getRegNumber().isEmpty()) {
            final Predicate regNumber = builder.equal(builder.lower(root.get("regNumber")),
                    request.getRegNumber().toLowerCase());
            predicates.add(regNumber);
        }
        if (!request.getProdYear().isEmpty()) {
            final Predicate prodYear = builder.equal(root.get("prodYear"), Integer.parseInt(request.getProdYear()));
            predicates.add(prodYear);
        }
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
