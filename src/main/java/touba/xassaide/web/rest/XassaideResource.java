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
import touba.xassaide.domain.Xassaide;
import touba.xassaide.repository.XassaideRepository;
import touba.xassaide.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link touba.xassaide.domain.Xassaide}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class XassaideResource {

    private final Logger log = LoggerFactory.getLogger(XassaideResource.class);

    private static final String ENTITY_NAME = "xassaide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XassaideRepository xassaideRepository;

    public XassaideResource(XassaideRepository xassaideRepository) {
        this.xassaideRepository = xassaideRepository;
    }

    /**
     * {@code POST  /xassaides} : Create a new xassaide.
     *
     * @param xassaide the xassaide to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xassaide, or with status {@code 400 (Bad Request)} if the xassaide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xassaides")
    public ResponseEntity<Xassaide> createXassaide(@Valid @RequestBody Xassaide xassaide) throws URISyntaxException {
        log.debug("REST request to save Xassaide : {}", xassaide);
        if (xassaide.getId() != null) {
            throw new BadRequestAlertException("A new xassaide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Xassaide result = xassaideRepository.save(xassaide);
        return ResponseEntity
            .created(new URI("/api/xassaides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /xassaides/:id} : Updates an existing xassaide.
     *
     * @param id the id of the xassaide to save.
     * @param xassaide the xassaide to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xassaide,
     * or with status {@code 400 (Bad Request)} if the xassaide is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xassaide couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xassaides/{id}")
    public ResponseEntity<Xassaide> updateXassaide(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Xassaide xassaide
    ) throws URISyntaxException {
        log.debug("REST request to update Xassaide : {}, {}", id, xassaide);
        if (xassaide.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xassaide.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xassaideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Xassaide result = xassaideRepository.save(xassaide);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xassaide.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /xassaides/:id} : Partial updates given fields of an existing xassaide, field will ignore if it is null
     *
     * @param id the id of the xassaide to save.
     * @param xassaide the xassaide to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xassaide,
     * or with status {@code 400 (Bad Request)} if the xassaide is not valid,
     * or with status {@code 404 (Not Found)} if the xassaide is not found,
     * or with status {@code 500 (Internal Server Error)} if the xassaide couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xassaides/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Xassaide> partialUpdateXassaide(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Xassaide xassaide
    ) throws URISyntaxException {
        log.debug("REST request to partial update Xassaide partially : {}, {}", id, xassaide);
        if (xassaide.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xassaide.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xassaideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Xassaide> result = xassaideRepository
            .findById(xassaide.getId())
            .map(
                existingXassaide -> {
                    if (xassaide.getNom() != null) {
                        existingXassaide.setNom(xassaide.getNom());
                    }

                    return existingXassaide;
                }
            )
            .map(xassaideRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, xassaide.getId().toString())
        );
    }

    /**
     * {@code GET  /xassaides} : get all the xassaides.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xassaides in body.
     */
    @GetMapping("/xassaides")
    public ResponseEntity<List<Xassaide>> getAllXassaides(Pageable pageable) {
        log.debug("REST request to get a page of Xassaides");
        Page<Xassaide> page = xassaideRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /xassaides/:id} : get the "id" xassaide.
     *
     * @param id the id of the xassaide to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xassaide, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xassaides/{id}")
    public ResponseEntity<Xassaide> getXassaide(@PathVariable Long id) {
        log.debug("REST request to get Xassaide : {}", id);
        Optional<Xassaide> xassaide = xassaideRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(xassaide);
    }

    /**
     * {@code DELETE  /xassaides/:id} : delete the "id" xassaide.
     *
     * @param id the id of the xassaide to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xassaides/{id}")
    public ResponseEntity<Void> deleteXassaide(@PathVariable Long id) {
        log.debug("REST request to delete Xassaide : {}", id);
        xassaideRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
