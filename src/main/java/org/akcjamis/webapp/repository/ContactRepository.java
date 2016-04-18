package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Contact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contact entity.
 */
public interface ContactRepository extends JpaRepository<Contact,Long> {

    List<Contact>findByFamily_id(Long id);

    Contact findByIdAndFamily_id(Long id, Long FamilyId);
}
