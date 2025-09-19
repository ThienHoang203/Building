package com.management.building.repository.space;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.space.SpaceType;

public interface SpaceTypeRepository extends JpaRepository<SpaceType, String> {

        @Query(value = """
                        SELECT st.name, st.description, st.specification, st.requiresSpecialAccess, st.maxCapacity
                        FROM space_type st
                        LEFT JOIN FETCH st.spaces
                        WHERE st.name = :name
                        """, nativeQuery = true)
        List<Object[]> findByNameWithSpace(@Param("name") String name);

        @Query(value = """
                        SELECT st.level
                        FROM space_type st
                        WHERE st.name = :parentName
                        """, nativeQuery = true)
        Optional<Object[]> getParentLevel(@Param("parentName") String parentName);

        @Modifying
        @Query(value = """
                        WITH SpaceTypeSubtree AS (
                            SELECT
                                name,
                                parent_space_type_id,
                                level
                            FROM SpaceType
                            WHERE name = :parentNodeName

                            UNION ALL

                            SELECT
                                st.name,
                                st.parent_space_type_id,
                                subtree.level + 1 as level
                            FROM SpaceType st
                            INNER JOIN SpaceTypeSubtree subtree ON st.parent_space_type_id = subtree.name
                        )
                        UPDATE st
                        SET level = subtree.level
                        FROM SpaceType st
                        INNER JOIN SpaceTypeSubtree subtree ON st.name = subtree.name
                        """, nativeQuery = true)
        void updateSubtreeLevels(@Param("parentNodeName") String parentNodeName);
}
