package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.PackageChangelog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PackageChangelog entity.
 */
public interface PackageChangelogRepository extends JpaRepository<PackageChangelog,Long> {

}
