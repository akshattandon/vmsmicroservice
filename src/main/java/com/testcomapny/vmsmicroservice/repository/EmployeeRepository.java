package com.testcomapny.vmsmicroservice.repository;

import org.springframework.stereotype.Repository;

import com.testcomapny.vmsmicroservice.domain.Employee;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
