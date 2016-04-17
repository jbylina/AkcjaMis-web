package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.ChristmasPackageNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ChristmasPackageNote entity.
 */
public interface ChristmasPackageNoteSearchRepository extends ElasticsearchRepository<ChristmasPackageNote, Long> {
}
