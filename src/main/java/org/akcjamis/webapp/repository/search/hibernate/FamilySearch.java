package org.akcjamis.webapp.repository.search.hibernate;

import org.akcjamis.webapp.domain.Family;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by Lareth on 2016-06-06.
 */

@Repository
@Transactional
public class FamilySearch  {

    // Spring will inject here the entity manager object
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * A basic search for the entity User. The search is done by exact match per
     * keywords on fields name, city and email.
     *
     * @param text The query text.
     */
    public List search(String text) {

        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager =
            org.hibernate.search.jpa.Search.
                getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder =
            fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(Family.class).get();

        // a very basic query by keywords
        org.apache.lucene.search.Query query =
            queryBuilder
                .keyword()
                .onFields("street",
                    "houseNo",
                    "flatNo",
                    "postalcode",
                    "city",
                    "region",
                    "source")
                .matching(text)
                .createQuery();

        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery =
            fullTextEntityManager.createFullTextQuery(query, Family.class);

        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
        List results = jpaQuery.getResultList();

        return results;
    } // method search
}
