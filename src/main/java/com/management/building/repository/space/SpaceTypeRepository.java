package com.management.building.repository.space;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.space.SpaceType;

public interface SpaceTypeRepository extends JpaRepository<SpaceType, String> {
    @Query(value = """
                SELECT st FROM SpaceType st WHERE st.name = :name AND st.level = :level
            """)
    Optional<SpaceType> findByNameAndLevel(@Param("name") String name, @Param("level") int level);

    @Query(value = """
            SELECT st.name, st.description, st.specification, st.requiresSpecialAccess, st.maxCapacity
            FROM space_type st
            LEFT JOIN FETCH st.spaces
            WHERE st.name = :name
            """, nativeQuery = true)
    List<Object[]> findByNameWithSpace(@Param("name") String name);

    @Query(value = """
              SELECT s FROM SpaceType s LEFT JOIN FETCH s.parent WHERE s.name = :name
            """)
    Optional<SpaceType> findByNameWithParent(@Param("name") String name);

    @Query(value = """
            WITH SpaceTypeSubTree AS (
                SELECT name, level,  max_capacity, requires_special_access, specifications, description, parent_name, 0 as depth FROM space_type WHERE name = :name

                UNION ALL

                SELECT st.name, st.level,  st.max_capacity, st.requires_special_access, st.specifications, st.description, st.parent_name, sub.depth + 1 as depth
                FROM space_type st
                INNER JOIN SpaceTypeSubTree sub ON st.parent_name = sub.name
                WHERE sub.depth < :limitDepth
            )
            SELECT * FROM SpaceTypeSubTree ORDER BY level
            """, nativeQuery = true)
    List<Object[]> findByNameWithChildren(@Param("name") String name, int limitDepth);

    @Query(value = """
            WITH SpaceTypeHierarchy AS (
                SELECT name, level, parent_name,
                       CAST(CONCAT('/', name) AS VARCHAR(1000)) as path,
                       0 as depth
                FROM space_type
                WHERE name = :currentName

                UNION ALL

                SELECT st.name, st.level, st.parent_name,
                       CAST(CONCAT('/', st.name, sth.path) AS VARCHAR(1000)) as path,
                       sth.depth + 1 as depth
                FROM space_type st
                INNER JOIN SpaceTypeHierarchy sth ON sth.parent_name = st.name
                WHERE st.name IS NOT NULL
            )
            SELECT name, level, depth, parent_name, path
            FROM SpaceTypeHierarchy
            ORDER BY depth ASC, name ASC
            """, nativeQuery = true)
    List<Object[]> findRootsByCurrentName(@Param("currentName") String currentName);
}
