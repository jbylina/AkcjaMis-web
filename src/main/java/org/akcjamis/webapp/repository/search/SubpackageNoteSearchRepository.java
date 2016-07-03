package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.SubpackageNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SubpackageNote entity.
 */
public interface SubpackageNoteSearchRepository extends ElasticsearchRepository<SubpackageNote, Integer> {
}
