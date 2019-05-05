package com.wherescape.vmsmicroservice.service.mapper;

import com.wherescape.vmsmicroservice.domain.*;
import com.wherescape.vmsmicroservice.service.dto.VisitorDTO;

import org.mapstruct.*;

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
