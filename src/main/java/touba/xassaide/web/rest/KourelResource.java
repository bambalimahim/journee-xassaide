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
import touba.xassaide.domain.Kourel;
import touba.xassaide.repository.KourelRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Kourel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KourelResource {

    private final Logger log = LoggerFactory.getLogger(KourelResource.class);

    private static final String ENTITY_NAME = "kourel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KourelRepository kourelRepository;

    public KourelResource(KourelRepository kourelRepository) {
        this.kourelRepository = kourelRepository;
    }

    /**
     * {@code POST  /kourels} : Create a new kourel.
     *
     * @param kourel the kourel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kourel, or with status {@code 400 (Bad Request)} if the kourel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kourels")
    public ResponseEntity<Kourel> createKourel(@Valid @RequestBody Kourel kourel) throws URISyntaxException {
        log.debug("REST request to save Kourel : {}", kourel);
        if (kourel.getId() != null) {
            throw new BadRequestAlertException("A new kourel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kourel result = kourelRepository.save(kourel);
        return ResponseEntity
            .created(new URI("/api/kourels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kourels/:id} : Updates an existing kourel.
     *
     * @param id the id of the kourel to save.
     * @param kourel the kourel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kourel,
     * or with status {@code 400 (Bad Request)} if the kourel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kourel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kourels/{id}")
    public ResponseEntity<Kourel> updateKourel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Kourel kourel
    ) throws URISyntaxException {
        log.debug("REST request to update Kourel : {}, {}", id, kourel);
        if (kourel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kourel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kourelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Kourel result = kourelRepository.save(kourel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kourel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kourels/:id} : Partial updates given fields of an existing kourel, field will ignore if it is null
     *
     * @param id the id of the kourel to save.
     * @param kourel the kourel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kourel,
     * or with status {@code 400 (Bad Request)} if the kourel is not valid,
     * or with status {@code 404 (Not Found)} if the kourel is not found,
     * or with status {@code 500 (Internal Server Error)} if the kourel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kourels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Kourel> partialUpdateKourel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Kourel kourel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Kourel partially : {}, {}", id, kourel);
        if (kourel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kourel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kourelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Kourel> result = kourelRepository
            .findById(kourel.getId())
            .map(
                existingKourel -> {
                    if (kourel.getNom() != null) {
                        existingKourel.setNom(kourel.getNom());
                    }

                    return existingKourel;
                }
            )
            .map(kourelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kourel.getId().toString())
        );
    }

    /**
     * {@code GET  /kourels} : get all the kourels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kourels in body.
     */
    @GetMapping("/kourels")
    public ResponseEntity<List<Kourel>> getAllKourels(Pageable pageable) {
        log.debug("REST request to get a page of Kourels");
        Page<Kourel> page = kourelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kourels/:id} : get the "id" kourel.
     *
     * @param id the id of the kourel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kourel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kourels/{id}")
    public ResponseEntity<Kourel> getKourel(@PathVariable Long id) {
        log.debug("REST request to get Kourel : {}", id);
        Optional<Kourel> kourel = kourelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kourel);
    }

    /**
     * {@code DELETE  /kourels/:id} : delete the "id" kourel.
     *
     * @param id the id of the kourel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kourels/{id}")
    public ResponseEntity<Void> deleteKourel(@PathVariable Long id) {
        log.debug("REST request to delete Kourel : {}", id);
        kourelRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
