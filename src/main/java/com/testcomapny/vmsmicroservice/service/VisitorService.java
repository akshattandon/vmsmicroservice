package com.testcomapny.vmsmicroservice.service;

import java.util.List;

import com.testcomapny.vmsmicroservice.service.dto.VisitorDTO;

/**
 * Service Interface for managing Visitor.
 */
public interface VisitorService {

    /**
     * Save a visitor.
     *
     * @param visitorDTO the entity to save
     * @return the persisted entity
     */
    VisitorDTO save(VisitorDTO visitorDTO);

    /**
     * Get all the visitors.
     *
     * @return the list of entities
     */
    List<VisitorDTO> findAll();

    /**
     * Get the "id" visitor.
     *
     * @param id the id of the entity
     * @return the entity
     */
    VisitorDTO findOne(Long id);

    /**
     * Delete the "id" visitor.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
