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
import touba.xassaide.domain.Mouride;
import touba.xassaide.repository.MourideRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Mouride}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MourideResource {

    private final Logger log = LoggerFactory.getLogger(MourideResource.class);

    private static final String ENTITY_NAME = "mouride";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MourideRepository mourideRepository;

    public MourideResource(MourideRepository mourideRepository) {
        this.mourideRepository = mourideRepository;
    }

    /**
     * {@code POST  /mourides} : Create a new mouride.
     *
     * @param mouride the mouride to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mouride, or with status {@code 400 (Bad Request)} if the mouride has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mourides")
    public ResponseEntity<Mouride> createMouride(@Valid @RequestBody Mouride mouride) throws URISyntaxException {
        log.debug("REST request to save Mouride : {}", mouride);
        if (mouride.getId() != null) {
            throw new BadRequestAlertException("A new mouride cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mouride result = mourideRepository.save(mouride);
        return ResponseEntity
            .created(new URI("/api/mourides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mourides/:id} : Updates an existing mouride.
     *
     * @param id the id of the mouride to save.
     * @param mouride the mouride to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mouride,
     * or with status {@code 400 (Bad Request)} if the mouride is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mouride couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mourides/{id}")
    public ResponseEntity<Mouride> updateMouride(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mouride mouride
    ) throws URISyntaxException {
        log.debug("REST request to update Mouride : {}, {}", id, mouride);
        if (mouride.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mouride.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mourideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mouride result = mourideRepository.save(mouride);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mouride.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mourides/:id} : Partial updates given fields of an existing mouride, field will ignore if it is null
     *
     * @param id the id of the mouride to save.
     * @param mouride the mouride to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mouride,
     * or with status {@code 400 (Bad Request)} if the mouride is not valid,
     * or with status {@code 404 (Not Found)} if the mouride is not found,
     * or with status {@code 500 (Internal Server Error)} if the mouride couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mourides/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Mouride> partialUpdateMouride(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mouride mouride
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mouride partially : {}, {}", id, mouride);
        if (mouride.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mouride.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mourideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mouride> result = mourideRepository
            .findById(mouride.getId())
            .map(
                existingMouride -> {
                    if (mouride.getPrenom() != null) {
                        existingMouride.setPrenom(mouride.getPrenom());
                    }
                    if (mouride.getNom() != null) {
                        existingMouride.setNom(mouride.getNom());
                    }
                    if (mouride.getEmail() != null) {
                        existingMouride.setEmail(mouride.getEmail());
                    }
                    if (mouride.getTelephone() != null) {
                        existingMouride.setTelephone(mouride.getTelephone());
                    }
                    if (mouride.getMatricule() != null) {
                        existingMouride.setMatricule(mouride.getMatricule());
                    }

                    return existingMouride;
                }
            )
            .map(mourideRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mouride.getId().toString())
        );
    }

    /**
     * {@code GET  /mourides} : get all the mourides.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mourides in body.
     */
    @GetMapping("/mourides")
    public ResponseEntity<List<Mouride>> getAllMourides(Pageable pageable) {
        log.debug("REST request to get a page of Mourides");
        Page<Mouride> page = mourideRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mourides/:id} : get the "id" mouride.
     *
     * @param id the id of the mouride to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mouride, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mourides/{id}")
    public ResponseEntity<Mouride> getMouride(@PathVariable Long id) {
        log.debug("REST request to get Mouride : {}", id);
        Optional<Mouride> mouride = mourideRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mouride);
    }

    /**
     * {@code DELETE  /mourides/:id} : delete the "id" mouride.
     *
     * @param id the id of the mouride to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mourides/{id}")
    public ResponseEntity<Void> deleteMouride(@PathVariable Long id) {
        log.debug("REST request to delete Mouride : {}", id);
        mourideRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
