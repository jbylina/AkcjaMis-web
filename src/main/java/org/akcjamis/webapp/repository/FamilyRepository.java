package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Family entity.
 */
public interface FamilyRepository extends JpaRepository<Family, Integer> {

}
