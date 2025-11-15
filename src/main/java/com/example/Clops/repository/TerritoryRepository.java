package com.example.Clops.repository;

import com.example.Clops.entity.Territory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TerritoryRepository extends JpaRepository<Territory, Integer> {

    Optional<Territory> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    Page<Territory> findAll(Pageable pageable);

    @Query("SELECT t FROM Territory t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Territory> searchByNameOrDescription(@Param("search") String search, Pageable pageable);

//    @Query("SELECT COUNT(s) > 0 FROM SpatialObject s WHERE s.territory.id = :territoryId")
//    boolean hasSpatialObjects(@Param("territoryId") Integer territoryId);
}
