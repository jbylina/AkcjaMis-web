package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.ChristmasPackage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChristmasPackage entity.
 */
public interface ChristmasPackageSearchRepository extends ElasticsearchRepository<ChristmasPackage, Long> {
}
