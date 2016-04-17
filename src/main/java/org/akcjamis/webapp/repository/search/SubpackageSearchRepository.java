package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.Subpackage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Subpackage entity.
 */
public interface SubpackageSearchRepository extends ElasticsearchRepository<Subpackage, Long> {
}
