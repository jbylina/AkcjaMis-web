package org.akcjamis.webapp.repository;

import org.akcjamis.webapp.domain.Team;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("select distinct team from Team team left join fetch team.users")
    List<Team> findAllWithEagerRelationships();

    @Query("select team from Team team left join fetch team.users where team.id =:id")
    Team findOneWithEagerRelationships(@Param("id") Integer id);

    @Query("select team from Team team " +
        "left join fetch team.christmasPackages christmasPackages " +
        "left join fetch team.users users " +
        "left join fetch team.event event " +
        "left join fetch christmasPackages.family family " +
        "left join fetch family.familyNotes familyNotes " +
        "left join fetch christmasPackages.subpackages subpackages " +
        "left join fetch subpackages.child child " +
        "where team.teamNumber =:teamNumber and event.year =:year")
    Team findOneByEvent_Year(@Param("year") Short year, @Param("teamNumber") Integer teamNumber);

    List<Team> findByEvent_year(Short year);

    @Query("select team from Team team " +
        "left join fetch team.christmasPackages christmasPackages " +
        "left join fetch team.users users " +
        "left join fetch team.event event " +
        "left join fetch christmasPackages.family family " +
        "left join fetch family.familyNotes familyNotes " +
        "left join fetch christmasPackages.subpackages subpackages " +
        "left join fetch christmasPackages.christmasPackageNotes christmasPackageNotes " +
        "left join fetch subpackages.child child " +
        "where users.login = :userLogin")
    Team findOneByUserLogin(@Param("userLogin") String userLogin);

}
