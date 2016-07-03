package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Event entity.
 */
public interface EventRepository extends JpaRepository<Event,Short> {

    Event findTop1ByOrderByYearDesc();
}
