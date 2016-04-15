package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.TeamMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TeamMember entity.
 */
public interface TeamMemberSearchRepository extends ElasticsearchRepository<TeamMember, Long> {
}
