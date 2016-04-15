package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.PackageNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PackageNote entity.
 */
public interface PackageNoteSearchRepository extends ElasticsearchRepository<PackageNote, Long> {
}
