package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.ChristmasPackageChange;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChristmasPackageChange entity.
 */
public interface ChristmasPackageChangeSearchRepository extends ElasticsearchRepository<ChristmasPackageChange,Integer> {
}
