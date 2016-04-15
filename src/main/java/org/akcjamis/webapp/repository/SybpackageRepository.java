package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Sybpackage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sybpackage entity.
 */
public interface SybpackageRepository extends JpaRepository<Sybpackage,Long> {

}
