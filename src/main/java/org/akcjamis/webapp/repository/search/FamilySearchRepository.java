package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.Family;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Family entity.
 */
public interface FamilySearchRepository extends ElasticsearchRepository<Family, Integer> {
}
