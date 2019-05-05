package com.testcomapny.vmsmicroservice.service.mapper;

import org.mapstruct.*;

import com.testcomapny.vmsmicroservice.domain.*;
import com.testcomapny.vmsmicroservice.service.dto.VisitorDTO;

/**
 * Mapper for the entity Visitor and its DTO VisitorDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface VisitorMapper extends EntityMapper<VisitorDTO, Visitor> {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.name", target = "employeeName")
    VisitorDTO toDto(Visitor visitor);

    @Mapping(source = "employeeId", target = "employee")
    Visitor toEntity(VisitorDTO visitorDTO);

    default Visitor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Visitor visitor = new Visitor();
        visitor.setId(id);
        return visitor;
    }
}
