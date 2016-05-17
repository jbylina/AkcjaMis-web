package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.ChristmasPackage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the ChristmasPackage entity.
 */
public interface ChristmasPackageRepository extends JpaRepository<ChristmasPackage,Long> {

    Page<ChristmasPackage> findByEvent_year(Short year, Pageable var1);

    ChristmasPackage findByEvent_yearAndId(Short eventYear, Long id);

    ChristmasPackage findById(Long id);

    @Query(value = "select christmasPackage from ChristmasPackage christmasPackage " +
        "left join fetch christmasPackage.subpackages subpackages " +
        "left join fetch subpackages.subpackageNotes subpackageNotes " +
        "left join fetch christmasPackage.christmasPackageNotes christmasPackageNotes " +
        "where christmasPackage.event.year =:eventYear",
        countQuery = "select count(christmasPackage) from ChristmasPackage christmasPackage " +
            "where christmasPackage.event.year =:eventYear")
    Page<ChristmasPackage> getList(@Param("eventYear") Short eventYear, Pageable page);
}
