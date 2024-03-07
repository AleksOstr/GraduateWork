package ru.egartech.vehicleapp.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.egartech.vehicleapp.model.Vehicle;
import ru.egartech.vehicleapp.model.VehicleBrand;

public class VehicleSpecification {
    public static Specification<Vehicle> byProdYear(String prodYear) {
        return (root, query, criteriaBuilder) -> {
            if (prodYear == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("prodYear"), Integer.parseInt(prodYear));
        };


    }

    public static Specification<Vehicle> byHasTrialer(String hasTrailer) {
        return (root, query, criteriaBuilder) -> {
            if (hasTrailer == null) {
                return criteriaBuilder.conjunction();
            }
            if (hasTrailer.equalsIgnoreCase("да")) {
                return criteriaBuilder.equal(root.get("hasTrailer"), true);
            } else {
                return criteriaBuilder.equal(root.get("hasTrailer"), false);
            }
        };
    }

    public static Specification<Vehicle> byBrandName(String brandName) {
        return (root, query, criteriaBuilder) -> {
            if (brandName == null) {
                return criteriaBuilder.conjunction();
            }
            Join<VehicleBrand, Vehicle> vehicleBrand = root.join("brand");
            return criteriaBuilder.equal(vehicleBrand.get("name"), brandName);
        };
    }
}
