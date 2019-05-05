package com.testcomapny.vmsmicroservice.service.mapper;

import org.mapstruct.*;

import com.testcomapny.vmsmicroservice.domain.*;
import com.testcomapny.vmsmicroservice.service.dto.EmployeeDTO;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {



    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
