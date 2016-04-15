package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.Child;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Child entity.
 */
public interface ChildSearchRepository extends ElasticsearchRepository<Child, Long> {
}
