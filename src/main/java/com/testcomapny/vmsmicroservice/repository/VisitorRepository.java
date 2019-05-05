package com.testcomapny.vmsmicroservice.repository;

import org.springframework.stereotype.Repository;

import com.testcomapny.vmsmicroservice.domain.Visitor;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Visitor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

}
