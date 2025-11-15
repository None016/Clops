package com.example.Clops.repository;

import com.example.Clops.entity.ConnectionStatus;
import com.example.Clops.entity.FiberConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FiberConnectionRepository extends JpaRepository<FiberConnection, Integer> {

    List<FiberConnection> findByCableId(Integer cableId);

    Page<FiberConnection> findByCableId(Integer cableId, Pageable pageable);

    List<FiberConnection> findByToObjectId(Integer toObjectId);

    Page<FiberConnection> findByToObjectId(Integer toObjectId, Pageable pageable);

    List<FiberConnection> findByStatus(ConnectionStatus status);

    Page<FiberConnection> findByStatus(ConnectionStatus status, Pageable pageable);

    @Query("SELECT fc FROM FiberConnection fc WHERE fc.cable.id = :cableId AND fc.fromFiber = :fiber")
    List<FiberConnection> findByCableAndFiber(@Param("cableId") Integer cableId, @Param("fiber") Integer fiber);

    @Query("SELECT fc FROM FiberConnection fc WHERE fc.toObject.id = :objectId AND fc.toFiber = :fiber")
    List<FiberConnection> findByObjectAndFiber(@Param("objectId") Integer objectId, @Param("fiber") Integer fiber);

    @Query("SELECT COUNT(fc) FROM FiberConnection fc WHERE fc.cable.id = :cableId")
    long countByCableId(@Param("cableId") Integer cableId);

    @Query("SELECT COUNT(fc) FROM FiberConnection fc WHERE fc.toObject.id = :objectId")
    long countByObjectId(@Param("objectId") Integer objectId);

    @Query("SELECT fc FROM FiberConnection fc WHERE " +
            "fc.cable.id = :cableId AND fc.fromFiber = :fromFiber AND " +
            "fc.toObject.id = :toObjectId AND fc.toFiber = :toFiber")
    Optional<FiberConnection> findExistingConnection(
            @Param("cableId") Integer cableId,
            @Param("fromFiber") Integer fromFiber,
            @Param("toObjectId") Integer toObjectId,
            @Param("toFiber") Integer toFiber);

    @Query("SELECT fc FROM FiberConnection fc WHERE fc.cable.id = :cableId AND fc.status = :status")
    List<FiberConnection> findByCableAndStatus(@Param("cableId") Integer cableId,
                                               @Param("status") ConnectionStatus status);
}
