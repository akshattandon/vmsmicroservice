package com.wherescape.vmsmicroservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wherescape.vmsmicroservice.service.VisitorService;
import com.wherescape.vmsmicroservice.web.rest.errors.BadRequestAlertException;
import com.wherescape.vmsmicroservice.web.rest.util.HeaderUtil;
import com.wherescape.vmsmicroservice.service.dto.VisitorDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Visitor.
 */
@RestController
@RequestMapping("/api")
public class VisitorResource {

    private final Logger log = LoggerFactory.getLogger(VisitorResource.class);

    private static final String ENTITY_NAME = "visitor";

    private final VisitorService visitorService;

    public VisitorResource(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * POST  /visitors : Create a new visitor.
     *
     * @param visitorDTO the visitorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new visitorDTO, or with status 400 (Bad Request) if the visitor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/visitors")
    @Timed
    public ResponseEntity<VisitorDTO> createVisitor(@Valid @RequestBody VisitorDTO visitorDTO) throws URISyntaxException {
        log.debug("REST request to save Visitor : {}", visitorDTO);
        if (visitorDTO.getId() != null) {
            throw new BadRequestAlertException("A new visitor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VisitorDTO result = visitorService.save(visitorDTO);
        return ResponseEntity.created(new URI("/api/visitors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /visitors : Updates an existing visitor.
     *
     * @param visitorDTO the visitorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated visitorDTO,
     * or with status 400 (Bad Request) if the visitorDTO is not valid,
     * or with status 500 (Internal Server Error) if the visitorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/visitors")
    @Timed
    public ResponseEntity<VisitorDTO> updateVisitor(@Valid @RequestBody VisitorDTO visitorDTO) throws URISyntaxException {
        log.debug("REST request to update Visitor : {}", visitorDTO);
        if (visitorDTO.getId() == null) {
            return createVisitor(visitorDTO);
        }
        VisitorDTO result = visitorService.save(visitorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visitorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /visitors : get all the visitors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of visitors in body
     */
    @GetMapping("/visitors")
    @Timed
    public List<VisitorDTO> getAllVisitors() {
        log.debug("REST request to get all Visitors");
        return visitorService.findAll();
        }

    /**
     * GET  /visitors/:id : get the "id" visitor.
     *
     * @param id the id of the visitorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the visitorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/visitors/{id}")
    @Timed
    public ResponseEntity<VisitorDTO> getVisitor(@PathVariable Long id) {
        log.debug("REST request to get Visitor : {}", id);
        VisitorDTO visitorDTO = visitorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visitorDTO));
    }

    /**
     * DELETE  /visitors/:id : delete the "id" visitor.
     *
     * @param id the id of the visitorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/visitors/{id}")
    @Timed
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        log.debug("REST request to delete Visitor : {}", id);
        visitorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
