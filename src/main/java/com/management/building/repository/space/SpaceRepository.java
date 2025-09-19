package com.management.building.repository.space;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.space.Space;
import com.management.building.entity.space.SpaceType;

public interface SpaceRepository extends JpaRepository<Space, Long> {

       @Query(value = """
                     WITH SpaceDescendants AS (
                         -- Base case: direct children
                         SELECT s.id, s.name, s.status, s.capacity, s.area, s.length, s.width, s.height,
                                s.parent_space_id, s.space_type_id, 1 as level,
                                CAST(CONCAT('/', CAST(s.id AS VARCHAR)) AS VARCHAR(1000)) as path
                         FROM space s
                         WHERE s.parent_space_id = :parentId

                         UNION ALL

                         -- Recursive case: children of children
                         SELECT s.id, s.name, s.status, s.capacity, s.area, s.length, s.width, s.height,
                                s.parent_space_id, s.space_type_id, sd.level + 1,
                                CAST(CONCAT(sd.path, '/', CAST(s.id AS VARCHAR)) AS VARCHAR(1000))
                         FROM space s
                         INNER JOIN SpaceDescendants sd ON s.parent_space_id = sd.id
                         WHERE sd.level < :maxDepth
                     ),
                     FilteredResults AS (
                         SELECT *,
                                CONCAT(CAST(level AS VARCHAR), ':', name, ':', CAST(id AS VARCHAR)) as cursor_value,
                                ROW_NUMBER() OVER (ORDER BY level :sortOrder, name :sortOrder, id :sortOrder) as row_num
                         FROM SpaceDescendants
                         WHERE (:cursor IS NULL OR CONCAT(CAST(level AS VARCHAR), ':', name, ':', CAST(id AS VARCHAR)) > :cursor)
                     )
                     SELECT id, name, status, capacity, area, length, width, height,
                            parent_space_id, space_type_id, level, path, cursor_value
                     FROM FilteredResults
                     WHERE row_num <= :limit
                     ORDER BY level :sortOrder, name :sortOrder, id :sortOrder
                     """, nativeQuery = true)
       List<Object[]> findChildSpacessWithPagination(
                     @Param("parentId") Long parentId,
                     @Param("maxDepth") Integer maxDepth,
                     @Param("cursor") String cursor,
                     @Param("limit") Integer limit,
                     @Param("sortOrder") String sortOrder);

       @Query(value = """
                     WITH SpaceAncestors AS (
                         -- Base case: direct parent
                         SELECT p.id, p.name, p.status, p.capacity, p.area, p.length, p.width, p.height,
                                p.parent_space_id, p.space_type_id, 1 as level,
                                CAST(CONCAT('/', CAST(p.id AS VARCHAR)) AS VARCHAR(1000)) as path
                         FROM space s
                         JOIN space p ON s.parent_space_id = p.id
                         WHERE s.id = :spaceId

                         UNION ALL

                         -- Recursive case: parents of parents
                         SELECT p.id, p.name, p.status, p.capacity, p.area, p.length, p.width, p.height,
                                p.parent_space_id, p.space_type_id, sa.level + 1,
                                CAST(CONCAT('/', CAST(p.id AS VARCHAR), sa.path) AS VARCHAR(1000))
                         FROM space p
                         INNER JOIN SpaceAncestors sa ON p.id = sa.parent_space_id
                         WHERE sa.level < :maxDepth
                     )
                     SELECT id, name, status, capacity, area, length, width, height,
                            parent_space_id, space_type_id, level, path
                     FROM SpaceAncestors
                     ORDER BY level DESC
                     """, nativeQuery = true)
       List<Object[]> findParentSpaces(@Param("spaceId") Long spaceId, @Param("maxDepth") Integer maxDepth);

       boolean existsByName(String name);
}
