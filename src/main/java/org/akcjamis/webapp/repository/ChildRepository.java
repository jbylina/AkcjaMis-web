package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Child entity.
 */
public interface ChildRepository extends JpaRepository<Child,Integer> {

    List<Child> findByFamily_id(Integer id);

    Child findByIdAndFamily_id(Integer id, Integer FamilyId);
}
