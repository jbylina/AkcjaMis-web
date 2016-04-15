package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.PackageGift;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PackageGift entity.
 */
public interface PackageGiftSearchRepository extends ElasticsearchRepository<PackageGift, Long> {
}
