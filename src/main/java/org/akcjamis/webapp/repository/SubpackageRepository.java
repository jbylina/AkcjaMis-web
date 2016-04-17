package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Subpackage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Subpackage entity.
 */
public interface SubpackageRepository extends JpaRepository<Subpackage,Long> {

}