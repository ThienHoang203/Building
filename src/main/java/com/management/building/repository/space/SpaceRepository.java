package com.management.building.repository.space;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.space.Space;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    @Query(value = """
            SELECT s FROM Space s LEFT JOIN FETCH s.type WHERE s.id = :id AND s.type.level = :level
            """)
    Optional<Space> findByIdAndLevel(@Param(value = "id") long id, @Param(value = "level") int level);

    @Query(value = """
            WITH SpaceSubTree AS (
                SELECT s.id, s.area, s.capacity, s.name, s.status, s.parent_id, s.type_name, 0 as depth
                FROM space s WHERE s.id = :id

                UNION ALL

                SELECT s.id, s.area, s.capacity, s.name, s.status, s.parent_id, s.type_name, sub.depth + 1 as depth
                FROM space s
                INNER JOIN SpaceSubTree sub ON s.parent_id = sub.id
                WHERE sub.depth < :limitDepth
            )
            SELECT sst.id, sst.area, sst.capacity, sst.name, sst.status, sst.parent_id, sst.type_name, sst.depth, st.level
            FROM SpaceSubTree sst
            LEFT JOIN space_type st ON sst.type_name = st.name
            """, nativeQuery = true)
    List<Object[]> findByNameWithChildren(@Param("id") Long id, int limitDepth);
}