package com.example.Clops.repository;

import com.example.Clops.entity.LinkType;
import com.example.Clops.entity.ObjectLink;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectLinkRepository extends JpaRepository<ObjectLink, Integer> {

    List<ObjectLink> findByFromObjectId(Integer fromObjectId);

    Page<ObjectLink> findByFromObjectId(Integer fromObjectId, Pageable pageable);

    List<ObjectLink> findByToObjectId(Integer toObjectId);

    Page<ObjectLink> findByToObjectId(Integer toObjectId, Pageable pageable);

    List<ObjectLink> findByLinkType(LinkType linkType);

    Page<ObjectLink> findByLinkType(LinkType linkType, Pageable pageable);

    @Query("SELECT ol FROM ObjectLink ol WHERE ol.fromObject.id = :fromObjectId AND ol.toObject.id = :toObjectId")
    List<ObjectLink> findByFromAndToObjects(@Param("fromObjectId") Integer fromObjectId,
                                            @Param("toObjectId") Integer toObjectId);

    @Query("SELECT ol FROM ObjectLink ol WHERE (ol.fromObject.id = :objectId OR ol.toObject.id = :objectId)")
    List<ObjectLink> findByObjectId(@Param("objectId") Integer objectId);

    @Query("SELECT ol FROM ObjectLink ol WHERE (ol.fromObject.id = :objectId OR ol.toObject.id = :objectId)")
    Page<ObjectLink> findByObjectId(@Param("objectId") Integer objectId, Pageable pageable);

    @Query("SELECT ol FROM ObjectLink ol WHERE ol.fromObject.id = :fromObjectId AND ol.toObject.id = :toObjectId AND ol.linkType = :linkType")
    Optional<ObjectLink> findExistingLink(@Param("fromObjectId") Integer fromObjectId,
                                          @Param("toObjectId") Integer toObjectId,
                                          @Param("linkType") LinkType linkType);

    @Query("SELECT COUNT(ol) FROM ObjectLink ol WHERE ol.fromObject.id = :objectId OR ol.toObject.id = :objectId")
    long countByObjectId(@Param("objectId") Integer objectId);

    @Query("SELECT COUNT(ol) FROM ObjectLink ol WHERE ol.linkType = :linkType")
    long countByLinkType(@Param("linkType") LinkType linkType);

    @Query("SELECT ol FROM ObjectLink ol WHERE ol.fromObject.id = :fromObjectId AND ol.linkType = :linkType")
    List<ObjectLink> findByFromObjectAndLinkType(@Param("fromObjectId") Integer fromObjectId,
                                                 @Param("linkType") LinkType linkType);

    @Query("SELECT ol FROM ObjectLink ol WHERE ol.toObject.id = :toObjectId AND ol.linkType = :linkType")
    List<ObjectLink> findByToObjectAndLinkType(@Param("toObjectId") Integer toObjectId,
                                               @Param("linkType") LinkType linkType);
}
