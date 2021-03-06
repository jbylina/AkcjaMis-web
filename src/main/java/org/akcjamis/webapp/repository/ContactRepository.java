package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Contact entity.
 */
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    List<Contact>findByFamily_id(Integer id);

    Contact findByIdAndFamily_id(Integer id, Integer FamilyId);
}
