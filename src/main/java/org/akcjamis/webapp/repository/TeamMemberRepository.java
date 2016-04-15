package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.TeamMember;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TeamMember entity.
 */
public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> {

}
