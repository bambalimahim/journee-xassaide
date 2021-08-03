package touba.xassaide.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import touba.xassaide.domain.Drouss;
import touba.xassaide.repository.DroussRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Drouss}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DroussResource {

    private final Logger log = LoggerFactory.getLogger(DroussResource.class);

    private static final String ENTITY_NAME = "drouss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DroussRepository droussRepository;

    public DroussResource(DroussRepository droussRepository) {
        this.droussRepository = droussRepository;
    }

    /**
     * {@code POST  /drousses} : Create a new drouss.
     *
     * @param drouss the drouss to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new drouss, or with status {@code 400 (Bad Request)} if the drouss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/drousses")
    public ResponseEntity<Drouss> createDrouss(@Valid @RequestBody Drouss drouss) throws URISyntaxException {
        log.debug("REST request to save Drouss : {}", drouss);
        if (drouss.getId() != null) {
            throw new BadRequestAlertException("A new drouss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Drouss result = droussRepository.save(drouss);
        return ResponseEntity
            .created(new URI("/api/drousses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /drousses/:id} : Updates an existing drouss.
     *
     * @param id the id of the drouss to save.
     * @param drouss the drouss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drouss,
     * or with status {@code 400 (Bad Request)} if the drouss is not valid,
     * or with status {@code 500 (Internal Server Error)} if the drouss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/drousses/{id}")
    public ResponseEntity<Drouss> updateDrouss(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Drouss drouss
    ) throws URISyntaxException {
        log.debug("REST request to update Drouss : {}, {}", id, drouss);
        if (drouss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, drouss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droussRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Drouss result = droussRepository.save(drouss);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, drouss.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /drousses/:id} : Partial updates given fields of an existing drouss, field will ignore if it is null
     *
     * @param id the id of the drouss to save.
     * @param drouss the drouss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drouss,
     * or with status {@code 400 (Bad Request)} if the drouss is not valid,
     * or with status {@code 404 (Not Found)} if the drouss is not found,
     * or with status {@code 500 (Internal Server Error)} if the drouss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/drousses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Drouss> partialUpdateDrouss(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Drouss drouss
    ) throws URISyntaxException {
        log.debug("REST request to partial update Drouss partially : {}, {}", id, drouss);
        if (drouss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, drouss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droussRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Drouss> result = droussRepository
            .findById(drouss.getId())
            .map(
                existingDrouss -> {
                    if (drouss.getNombre() != null) {
                        existingDrouss.setNombre(drouss.getNombre());
                    }

                    return existingDrouss;
                }
            )
            .map(droussRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, drouss.getId().toString())
        );
    }

    /**
     * {@code GET  /drousses} : get all the drousses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of drousses in body.
     */
    @GetMapping("/drousses")
    public ResponseEntity<List<Drouss>> getAllDrousses(Pageable pageable) {
        log.debug("REST request to get a page of Drousses");
        Page<Drouss> page = droussRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /drousses/:id} : get the "id" drouss.
     *
     * @param id the id of the drouss to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the drouss, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/drousses/{id}")
    public ResponseEntity<Drouss> getDrouss(@PathVariable Long id) {
        log.debug("REST request to get Drouss : {}", id);
        Optional<Drouss> drouss = droussRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(drouss);
    }

    /**
     * {@code DELETE  /drousses/:id} : delete the "id" drouss.
     *
     * @param id the id of the drouss to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/drousses/{id}")
    public ResponseEntity<Void> deleteDrouss(@PathVariable Long id) {
        log.debug("REST request to delete Drouss : {}", id);
        droussRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
