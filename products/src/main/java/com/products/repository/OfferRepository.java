package com.products.repository;

import com.products.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<OfferEntity, UUID> {
    @Query(value = "SELECT * FROM offer off WHERE off.parent_id = :id", nativeQuery = true)
    Collection<OfferEntity> findChildOfferById(@Param("id") UUID id);

    @Query(value = "SELECT * FROM offer off WHERE off.parent_id IN :uuids", nativeQuery = true)
    Collection<OfferEntity> findChildOffer(@Param("uuids") List<UUID> uuids);

    @Query(value = "SELECT * FROM offer off WHERE off.date >= :from AND off.date <= :to",
            nativeQuery = true)
    Collection<OfferEntity> findAll(ZonedDateTime from, ZonedDateTime to);
}
