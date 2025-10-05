package com.management.building.repository.device;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.device.CategoryStatus;

public interface CategoryStatusRepository extends JpaRepository<CategoryStatus, String> {
    @Query(value = """
            SELECT cs
            FROM category_status cs
            WHERE cs.category_code = :categoryCode
            AND cs.code IN (:statusCodes)
            """, nativeQuery = true)
    List<CategoryStatus> findAllStatusCodeInRangeByCategoryCode(
            @Param("categoryCode") String categoryCode,
            @Param("statusCodes") Set<String> statusCodes);

    @Query(value = """
            SELECT cs
            FROM category_status cs
            WHERE cs.category_code = :categoryCode
            AND cs.code IN (:statusCode)
            """, nativeQuery = true)
    Optional<CategoryStatus> findStatusCodeByCategoryCode(
            @Param("categoryCode") String categoryCode,
            @Param("statusCode") String statusCode);
}
