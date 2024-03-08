package ru.egartech.vehicleapp.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.egartech.vehicleapp.model.Vehicle;
import ru.egartech.vehicleapp.model.VehicleBrand;
import ru.egartech.vehicleapp.model.VehicleCategory;
import ru.egartech.vehicleapp.model.VehicleModel;

public class VehicleSpecification {
    public static Specification<Vehicle> byBrand(String brandName) {
        return (root, query, criteriaBuilder) -> {
            if (brandName == null) {
                return criteriaBuilder.conjunction();
            }
            Join<VehicleBrand, Vehicle> vehicleBrand = root.join("brand");
            return criteriaBuilder.equal(vehicleBrand.get("name"), brandName);
        };
    }

    public static Specification<Vehicle> byModel(String modelName) {
        return (root, query, criteriaBuilder) -> {
            if (modelName == null) {
                return criteriaBuilder.conjunction();
            }
            Join<VehicleModel, Vehicle> vehicleModel = root.join("model");
            return criteriaBuilder.equal(vehicleModel.get("name"), modelName);
        };
    }

    public static Specification<Vehicle> byCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null) {
                return criteriaBuilder.conjunction();
            }
            Join<VehicleCategory,Vehicle> vehicleCategory = root.join("category");
            return criteriaBuilder.equal(vehicleCategory.get("name"), categoryName);
        };
    }

    public static Specification<Vehicle> byRegNumber(String regNumber) {
        return (root, query, criteriaBuilder) -> {
            if (regNumber == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("regNumber"), regNumber);
        };
    }

    public static Specification<Vehicle> byProdYear(String prodYear) {
        return (root, query, criteriaBuilder) -> {
            if (prodYear == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("prodYear"), Integer.parseInt(prodYear));
        };
    }
}
