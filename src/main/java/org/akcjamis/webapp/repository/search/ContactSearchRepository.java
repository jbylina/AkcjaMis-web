package org.akcjamis.webapp.repository.search;

import org.akcjamis.webapp.domain.Contact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Contact entity.
 */
public interface ContactSearchRepository extends ElasticsearchRepository<Contact,Integer> {
}
