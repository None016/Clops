package com.example.Clops.repository;


import com.example.Clops.entity.SpatialObject;
import com.example.Clops.entity.SpatialObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpatialObjectRepository extends JpaRepository<SpatialObject, Integer> {

    Optional<SpatialObject> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    Page<SpatialObject> findAll(Pageable pageable);

    List<SpatialObject> findByType(SpatialObjectType type);

    Page<SpatialObject> findByType(SpatialObjectType type, Pageable pageable);

    List<SpatialObject> findByTerritoryId(Integer territoryId);

    Page<SpatialObject> findByTerritoryId(Integer territoryId, Pageable pageable);

    @Query("SELECT so FROM SpatialObject so WHERE " +
            "LOWER(so.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(so.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<SpatialObject> searchByNameOrDescription(@Param("search") String search, Pageable pageable);

    @Query("SELECT so FROM SpatialObject so WHERE so.territory.id = :territoryId AND " +
            "(LOWER(so.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(so.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<SpatialObject> searchByTerritoryAndNameOrDescription(@Param("territoryId") Integer territoryId,
                                                              @Param("search") String search,
                                                              Pageable pageable);

    @Query("SELECT COUNT(so) FROM SpatialObject so WHERE so.territory.id = :territoryId")
    long countByTerritoryId(@Param("territoryId") Integer territoryId);

    // Пространственные запросы (требуют PostGIS)
    @Query(value = "SELECT * FROM spatial_objects WHERE ST_DWithin(geometry, ST_GeogFromText(:wkt), :distance)",
            nativeQuery = true)
    List<SpatialObject> findWithinDistance(@Param("wkt") String wkt,
                                           @Param("distance") double distance);

    @Query(value = "SELECT * FROM spatial_objects WHERE ST_Within(geometry, ST_GeogFromText(:polygonWkt))",
            nativeQuery = true)
    List<SpatialObject> findWithinPolygon(@Param("polygonWkt") String polygonWkt);

    @Query(value = "SELECT * FROM spatial_objects WHERE ST_Distance(geometry, ST_GeogFromText(:pointWkt)) < :distance",
            nativeQuery = true)
    List<SpatialObject> findByDistance(@Param("pointWkt") String pointWkt,
                                       @Param("distance") double distance);
}