package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.Sybpackage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Sybpackage entity.
 */
public interface SybpackageSearchRepository extends ElasticsearchRepository<Sybpackage, Long> {
}
