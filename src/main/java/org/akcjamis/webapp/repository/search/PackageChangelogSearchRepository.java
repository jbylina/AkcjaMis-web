package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.PackageChangelog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PackageChangelog entity.
 */
public interface PackageChangelogSearchRepository extends ElasticsearchRepository<PackageChangelog, Long> {
}
