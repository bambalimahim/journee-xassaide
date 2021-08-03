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
import touba.xassaide.domain.Cuisine;
import touba.xassaide.repository.CuisineRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Cuisine}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CuisineResource {

    private final Logger log = LoggerFactory.getLogger(CuisineResource.class);

    private static final String ENTITY_NAME = "cuisine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuisineRepository cuisineRepository;

    public CuisineResource(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    /**
     * {@code POST  /cuisines} : Create a new cuisine.
     *
     * @param cuisine the cuisine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuisine, or with status {@code 400 (Bad Request)} if the cuisine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cuisines")
    public ResponseEntity<Cuisine> createCuisine(@Valid @RequestBody Cuisine cuisine) throws URISyntaxException {
        log.debug("REST request to save Cuisine : {}", cuisine);
        if (cuisine.getId() != null) {
            throw new BadRequestAlertException("A new cuisine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cuisine result = cuisineRepository.save(cuisine);
        return ResponseEntity
            .created(new URI("/api/cuisines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cuisines/:id} : Updates an existing cuisine.
     *
     * @param id the id of the cuisine to save.
     * @param cuisine the cuisine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuisine,
     * or with status {@code 400 (Bad Request)} if the cuisine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuisine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cuisines/{id}")
    public ResponseEntity<Cuisine> updateCuisine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Cuisine cuisine
    ) throws URISyntaxException {
        log.debug("REST request to update Cuisine : {}, {}", id, cuisine);
        if (cuisine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuisine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuisineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cuisine result = cuisineRepository.save(cuisine);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuisine.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cuisines/:id} : Partial updates given fields of an existing cuisine, field will ignore if it is null
     *
     * @param id the id of the cuisine to save.
     * @param cuisine the cuisine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuisine,
     * or with status {@code 400 (Bad Request)} if the cuisine is not valid,
     * or with status {@code 404 (Not Found)} if the cuisine is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuisine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cuisines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Cuisine> partialUpdateCuisine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Cuisine cuisine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cuisine partially : {}, {}", id, cuisine);
        if (cuisine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuisine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuisineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cuisine> result = cuisineRepository
            .findById(cuisine.getId())
            .map(
                existingCuisine -> {
                    if (cuisine.getNom() != null) {
                        existingCuisine.setNom(cuisine.getNom());
                    }
                    if (cuisine.getCapacite() != null) {
                        existingCuisine.setCapacite(cuisine.getCapacite());
                    }

                    return existingCuisine;
                }
            )
            .map(cuisineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuisine.getId().toString())
        );
    }

    /**
     * {@code GET  /cuisines} : get all the cuisines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuisines in body.
     */
    @GetMapping("/cuisines")
    public ResponseEntity<List<Cuisine>> getAllCuisines(Pageable pageable) {
        log.debug("REST request to get a page of Cuisines");
        Page<Cuisine> page = cuisineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cuisines/:id} : get the "id" cuisine.
     *
     * @param id the id of the cuisine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuisine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cuisines/{id}")
    public ResponseEntity<Cuisine> getCuisine(@PathVariable Long id) {
        log.debug("REST request to get Cuisine : {}", id);
        Optional<Cuisine> cuisine = cuisineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cuisine);
    }

    /**
     * {@code DELETE  /cuisines/:id} : delete the "id" cuisine.
     *
     * @param id the id of the cuisine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cuisines/{id}")
    public ResponseEntity<Void> deleteCuisine(@PathVariable Long id) {
        log.debug("REST request to delete Cuisine : {}", id);
        cuisineRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
