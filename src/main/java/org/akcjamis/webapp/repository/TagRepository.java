package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Set<Tag> findByCodeIn(Set<String> tagCodes);

}
