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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            WITH SpaceTypeSubtree AS (
                SELECT
                    name,
                    parent_name,
                    level
                FROM space_type
                WHERE name = :parentNodeName

                UNION ALL

                SELECT
                    st.name,
                    st.parent_name,
                    subtree.level + 1 as level
                FROM space_type st
                INNER JOIN SpaceTypeSubtree subtree ON st.parent_name = subtree.name
            )
            UPDATE st
            SET level = subtree.level
            FROM space_type st
            INNER JOIN SpaceTypeSubtree subtree ON st.name = subtree.name
            """, nativeQuery = true)
    void updateChildSpaceLevels(@Param("parentNodeName") String parentNodeName);

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
                INNER JOIN SpaceTypeHierarchy sth ON sth.name = st.parent_name
                WHERE st.name IS NOT NULL
            )
            SELECT name, level, depth, parent_name, path
            FROM SpaceTypeHierarchy
            ORDER BY depth ASC, name ASC
            """, nativeQuery = true)
    List<Object[]> findChildrenByCurrentName(@Param("currentName") String currentName);

    @Query(value = """
              SELECT s FROM SpaceType s LEFT JOIN FETCH s.parent WHERE s.name = :name
            """)
    Optional<SpaceType> findByNameWithParent(@Param("name") String name);

    @Query(value = """
              SELECT s FROM SpaceType s LEFT JOIN FETCH s.spaces WHERE s.name = :name
            """)
    Optional<SpaceType> findByNameWithSpaces(@Param("name") String name);

    @Query(value = """
            SELECT COUNT(*) FROM space_type st  WHERE st.parent_name = :parentName
             """, nativeQuery = true)
    Long countChildrenByParentName(@Param("parentName") String parentName);
}
