package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.FamilyNote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the FamilyNote entity.
 */
public interface FamilyNoteSearchRepository extends ElasticsearchRepository<FamilyNote,Integer> {
}
