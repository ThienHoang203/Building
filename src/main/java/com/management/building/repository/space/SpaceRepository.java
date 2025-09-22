package com.management.building.repository.space;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.space.Space;
import com.management.building.enums.space.SpaceStatus;

public interface SpaceRepository extends JpaRepository<Space, Long> {

       @Query("SELECT s FROM Space s LEFT JOIN FETCH s.parent LEFT JOIN FETCH s.type WHERE s.id = :id")
       Optional<Space> findByIdWithDetails(@Param("id") Long id);

       @Query("SELECT s FROM Space s LEFT JOIN FETCH s.children WHERE s.id = :id")
       Optional<Space> findByIdWithChildren(@Param("id") Long id);

       @Query("SELECT s FROM Space s WHERE s.status = :status ORDER BY s.name")
       List<Space> findByStatus(@Param("status") SpaceStatus status);

       @Query("SELECT s FROM Space s WHERE s.type.name = :typeName ORDER BY s.name")
       List<Space> findByTypeName(@Param("typeName") String typeName);

       @Query("SELECT COUNT(s) FROM Space s WHERE s.parent.id = :parentId")
       Long countByParentId(@Param("parentId") Long parentId);

       @Query("SELECT COUNT(s) FROM Space s WHERE s.type.name = :typeName")
       Long countByTypeName(@Param("typeName") String typeName);

       @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Space s WHERE s.name = :name")
       boolean existsByName(@Param("name") String name);

       @Query(value = """
                     WITH SpaceHierarchy AS (
                         SELECT id, name, status, parent_id, type_name,
                                CAST(CONCAT('/', id) AS VARCHAR(1000)) as path,
                                0 as depth,
                                0 as level
                         FROM space
                         WHERE id = :currentId

                         UNION ALL

                         SELECT s.id, s.name, s.status, s.parent_id, s.type_name,
                                CAST(CONCAT('/', s.id, sh.path) AS VARCHAR(1000)) as path,
                                sh.depth + 1 as depth,
                                sh.level + 1 as level
                         FROM space s
                         INNER JOIN SpaceHierarchy sh ON sh.parent_id = s.id
                         WHERE s.id IS NOT NULL
                     )
                     SELECT id, name, status, level, depth, path
                     FROM SpaceHierarchy
                     ORDER BY depth ASC, name ASC
                     """, nativeQuery = true)
       List<Object[]> findParentHierarchy(@Param("currentId") Long currentId);

       @Query(value = """
                     WITH SpaceHierarchy AS (
                         SELECT id, name, status, parent_id, type_name,
                                CAST(CONCAT('/', id) AS VARCHAR(1000)) as path,
                                0 as depth,
                                0 as level
                         FROM space
                         WHERE id = :currentId

                         UNION ALL

                         SELECT s.id, s.name, s.status, s.parent_id, s.type_name,
                                CAST(CONCAT(sh.path, '/', s.id) AS VARCHAR(1000)) as path,
                                sh.depth + 1 as depth,
                                sh.level - 1 as level
                         FROM space s
                         INNER JOIN SpaceHierarchy sh ON s.parent_id = sh.id
                         WHERE s.id IS NOT NULL
                     )
                     SELECT id, name, status, level, depth, path
                     FROM SpaceHierarchy
                     WHERE id != :currentId
                     ORDER BY depth ASC, name ASC
                     """, nativeQuery = true)
       List<Object[]> findChildHierarchy(@Param("currentId") Long currentId);

       @Query("SELECT s FROM Space s LEFT JOIN FETCH s.parent WHERE s.id = :id")
       Optional<Space> findByIdWithParent(@Param("id") Long id);

       @Query("SELECT s FROM Space s LEFT JOIN FETCH s.parent LEFT JOIN FETCH s.type WHERE s.id = :id")
       Optional<Space> findByIdWithParentAndType(@Param("id") Long id);

       @Query("SELECT s FROM Space s LEFT JOIN FETCH s.type WHERE s.id = :id")
       Optional<Space> findByIdWithype(@Param("id") Long id);

       @Query("SELECT s FROM Space s WHERE s.parent IS NULL ORDER BY s.name")
       List<Space> findRootSpaces();

       @Query("SELECT s FROM Space s WHERE s.parent.id = :parentId ORDER BY s.name")
       List<Space> findByParentId(@Param("parentId") Long parentId);
}