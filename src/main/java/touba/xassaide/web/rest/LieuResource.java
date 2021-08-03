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
import touba.xassaide.domain.Lieu;
import touba.xassaide.repository.LieuRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Lieu}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LieuResource {

    private final Logger log = LoggerFactory.getLogger(LieuResource.class);

    private static final String ENTITY_NAME = "lieu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LieuRepository lieuRepository;

    public LieuResource(LieuRepository lieuRepository) {
        this.lieuRepository = lieuRepository;
    }

    /**
     * {@code POST  /lieus} : Create a new lieu.
     *
     * @param lieu the lieu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lieu, or with status {@code 400 (Bad Request)} if the lieu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lieus")
    public ResponseEntity<Lieu> createLieu(@Valid @RequestBody Lieu lieu) throws URISyntaxException {
        log.debug("REST request to save Lieu : {}", lieu);
        if (lieu.getId() != null) {
            throw new BadRequestAlertException("A new lieu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lieu result = lieuRepository.save(lieu);
        return ResponseEntity
            .created(new URI("/api/lieus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lieus/:id} : Updates an existing lieu.
     *
     * @param id the id of the lieu to save.
     * @param lieu the lieu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lieu,
     * or with status {@code 400 (Bad Request)} if the lieu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lieu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lieus/{id}")
    public ResponseEntity<Lieu> updateLieu(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Lieu lieu)
        throws URISyntaxException {
        log.debug("REST request to update Lieu : {}, {}", id, lieu);
        if (lieu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lieu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lieuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lieu result = lieuRepository.save(lieu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lieu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lieus/:id} : Partial updates given fields of an existing lieu, field will ignore if it is null
     *
     * @param id the id of the lieu to save.
     * @param lieu the lieu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lieu,
     * or with status {@code 400 (Bad Request)} if the lieu is not valid,
     * or with status {@code 404 (Not Found)} if the lieu is not found,
     * or with status {@code 500 (Internal Server Error)} if the lieu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lieus/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Lieu> partialUpdateLieu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Lieu lieu
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lieu partially : {}, {}", id, lieu);
        if (lieu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lieu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lieuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lieu> result = lieuRepository
            .findById(lieu.getId())
            .map(
                existingLieu -> {
                    if (lieu.getNom() != null) {
                        existingLieu.setNom(lieu.getNom());
                    }

                    return existingLieu;
                }
            )
            .map(lieuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lieu.getId().toString())
        );
    }

    /**
     * {@code GET  /lieus} : get all the lieus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lieus in body.
     */
    @GetMapping("/lieus")
    public ResponseEntity<List<Lieu>> getAllLieus(Pageable pageable) {
        log.debug("REST request to get a page of Lieus");
        Page<Lieu> page = lieuRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lieus/:id} : get the "id" lieu.
     *
     * @param id the id of the lieu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lieu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lieus/{id}")
    public ResponseEntity<Lieu> getLieu(@PathVariable Long id) {
        log.debug("REST request to get Lieu : {}", id);
        Optional<Lieu> lieu = lieuRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lieu);
    }

    /**
     * {@code DELETE  /lieus/:id} : delete the "id" lieu.
     *
     * @param id the id of the lieu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lieus/{id}")
    public ResponseEntity<Void> deleteLieu(@PathVariable Long id) {
        log.debug("REST request to delete Lieu : {}", id);
        lieuRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
