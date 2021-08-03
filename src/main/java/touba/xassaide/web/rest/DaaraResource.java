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
import touba.xassaide.domain.Daara;
import touba.xassaide.repository.DaaraRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Daara}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DaaraResource {

    private final Logger log = LoggerFactory.getLogger(DaaraResource.class);

    private static final String ENTITY_NAME = "daara";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DaaraRepository daaraRepository;

    public DaaraResource(DaaraRepository daaraRepository) {
        this.daaraRepository = daaraRepository;
    }

    /**
     * {@code POST  /daaras} : Create a new daara.
     *
     * @param daara the daara to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new daara, or with status {@code 400 (Bad Request)} if the daara has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daaras")
    public ResponseEntity<Daara> createDaara(@Valid @RequestBody Daara daara) throws URISyntaxException {
        log.debug("REST request to save Daara : {}", daara);
        if (daara.getId() != null) {
            throw new BadRequestAlertException("A new daara cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Daara result = daaraRepository.save(daara);
        return ResponseEntity
            .created(new URI("/api/daaras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /daaras/:id} : Updates an existing daara.
     *
     * @param id the id of the daara to save.
     * @param daara the daara to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daara,
     * or with status {@code 400 (Bad Request)} if the daara is not valid,
     * or with status {@code 500 (Internal Server Error)} if the daara couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daaras/{id}")
    public ResponseEntity<Daara> updateDaara(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Daara daara)
        throws URISyntaxException {
        log.debug("REST request to update Daara : {}, {}", id, daara);
        if (daara.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, daara.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!daaraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Daara result = daaraRepository.save(daara);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daara.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /daaras/:id} : Partial updates given fields of an existing daara, field will ignore if it is null
     *
     * @param id the id of the daara to save.
     * @param daara the daara to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daara,
     * or with status {@code 400 (Bad Request)} if the daara is not valid,
     * or with status {@code 404 (Not Found)} if the daara is not found,
     * or with status {@code 500 (Internal Server Error)} if the daara couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/daaras/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Daara> partialUpdateDaara(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Daara daara
    ) throws URISyntaxException {
        log.debug("REST request to partial update Daara partially : {}, {}", id, daara);
        if (daara.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, daara.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!daaraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Daara> result = daaraRepository
            .findById(daara.getId())
            .map(
                existingDaara -> {
                    if (daara.getNom() != null) {
                        existingDaara.setNom(daara.getNom());
                    }

                    return existingDaara;
                }
            )
            .map(daaraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daara.getId().toString())
        );
    }

    /**
     * {@code GET  /daaras} : get all the daaras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of daaras in body.
     */
    @GetMapping("/daaras")
    public ResponseEntity<List<Daara>> getAllDaaras(Pageable pageable) {
        log.debug("REST request to get a page of Daaras");
        Page<Daara> page = daaraRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /daaras/:id} : get the "id" daara.
     *
     * @param id the id of the daara to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the daara, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daaras/{id}")
    public ResponseEntity<Daara> getDaara(@PathVariable Long id) {
        log.debug("REST request to get Daara : {}", id);
        Optional<Daara> daara = daaraRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(daara);
    }

    /**
     * {@code DELETE  /daaras/:id} : delete the "id" daara.
     *
     * @param id the id of the daara to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daaras/{id}")
    public ResponseEntity<Void> deleteDaara(@PathVariable Long id) {
        log.debug("REST request to delete Daara : {}", id);
        daaraRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
