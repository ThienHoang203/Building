package com.management.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.Space;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    // Lấy tất cả con cháu của một Space
    // @Query(value = """
    // WITH SpaceHierarchy AS (
    // -- Anchor: Space gốc
    // SELECT id, name, parent_space_id, 0 as level
    // FROM spaces
    // WHERE id = :spaceId

    // UNION ALL

    // -- Recursive: Tìm con
    // SELECT s.id, s.name, s.parent_space_id, sh.level + 1
    // FROM spaces s
    // INNER JOIN SpaceHierarchy sh ON s.parent_space_id = sh.id
    // )
    // SELECT * FROM SpaceHierarchy
    // ORDER BY level, name
    // """, nativeQuery = true)
    // List<Object[]> findAllDescendants(@Param("spaceId") String spaceId);

    // // Lấy tất cả tổ tiên của một Space
    // @Query(value = """
    // WITH SpaceAncestors AS (
    // -- Anchor: Space hiện tại
    // SELECT id, name, parent_space_id, 0 as level
    // FROM spaces
    // WHERE id = :spaceId

    // UNION ALL

    // -- Recursive: Tìm cha
    // SELECT s.id, s.name, s.parent_space_id, sa.level + 1
    // FROM spaces s
    // INNER JOIN SpaceAncestors sa ON s.id = sa.parent_space_id
    // )
    // SELECT * FROM SpaceAncestors
    // ORDER BY level DESC
    // """, nativeQuery = true)
    // List<Object[]> findAllAncestors(@Param("spaceId") String spaceId);

    // // Lấy toàn bộ cây với độ sâu tối đa
    // @Query(value = """
    // WITH SpaceTree AS (
    // -- Root nodes (không có parent)
    // SELECT id, name, parent_space_id, 0 as level,
    // CAST(name AS NVARCHAR(MAX)) as path
    // FROM spaces
    // WHERE parent_space_id IS NULL

    // UNION ALL

    // -- Children
    // SELECT s.id, s.name, s.parent_space_id, st.level + 1,
    // CAST(st.path + ' > ' + s.name AS NVARCHAR(MAX))
    // FROM spaces s
    // INNER JOIN SpaceTree st ON s.parent_space_id = st.id
    // WHERE st.level < :maxDepth
    // )
    // SELECT * FROM SpaceTree
    // ORDER BY path
    // """, nativeQuery = true)
    // List<Object[]> findSpaceTree(@Param("maxDepth") int maxDepth);
    boolean existsByName(String name);
}
