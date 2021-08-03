package touba.xassaide.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import touba.xassaide.IntegrationTest;
import touba.xassaide.domain.Drouss;
import touba.xassaide.repository.DroussRepository;

/**
 * Integration tests for the {@link DroussResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DroussResourceIT {

    private static final Integer DEFAULT_NOMBRE = 1;
    private static final Integer UPDATED_NOMBRE = 2;

    private static final String ENTITY_API_URL = "/api/drousses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DroussRepository droussRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDroussMockMvc;

    private Drouss drouss;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drouss createEntity(EntityManager em) {
        Drouss drouss = new Drouss().nombre(DEFAULT_NOMBRE);
        return drouss;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drouss createUpdatedEntity(EntityManager em) {
        Drouss drouss = new Drouss().nombre(UPDATED_NOMBRE);
        return drouss;
    }

    @BeforeEach
    public void initTest() {
        drouss = createEntity(em);
    }

    @Test
    @Transactional
    void createDrouss() throws Exception {
        int databaseSizeBeforeCreate = droussRepository.findAll().size();
        // Create the Drouss
        restDroussMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drouss)))
            .andExpect(status().isCreated());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeCreate + 1);
        Drouss testDrouss = droussList.get(droussList.size() - 1);
        assertThat(testDrouss.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void createDroussWithExistingId() throws Exception {
        // Create the Drouss with an existing ID
        drouss.setId(1L);

        int databaseSizeBeforeCreate = droussRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDroussMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drouss)))
            .andExpect(status().isBadRequest());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = droussRepository.findAll().size();
        // set the field null
        drouss.setNombre(null);

        // Create the Drouss, which fails.

        restDroussMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drouss)))
            .andExpect(status().isBadRequest());

        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDrousses() throws Exception {
        // Initialize the database
        droussRepository.saveAndFlush(drouss);

        // Get all the droussList
        restDroussMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drouss.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)));
    }

    @Test
    @Transactional
    void getDrouss() throws Exception {
        // Initialize the database
        droussRepository.saveAndFlush(drouss);

        // Get the drouss
        restDroussMockMvc
            .perform(get(ENTITY_API_URL_ID, drouss.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(drouss.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE));
    }

    @Test
    @Transactional
    void getNonExistingDrouss() throws Exception {
        // Get the drouss
        restDroussMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDrouss() throws Exception {
        // Initialize the database
        droussRepository.saveAndFlush(drouss);

        int databaseSizeBeforeUpdate = droussRepository.findAll().size();

        // Update the drouss
        Drouss updatedDrouss = droussRepository.findById(drouss.getId()).get();
        // Disconnect from session so that the updates on updatedDrouss are not directly saved in db
        em.detach(updatedDrouss);
        updatedDrouss.nombre(UPDATED_NOMBRE);

        restDroussMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDrouss.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDrouss))
            )
            .andExpect(status().isOk());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
        Drouss testDrouss = droussList.get(droussList.size() - 1);
        assertThat(testDrouss.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void putNonExistingDrouss() throws Exception {
        int databaseSizeBeforeUpdate = droussRepository.findAll().size();
        drouss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroussMockMvc
            .perform(
                put(ENTITY_API_URL_ID, drouss.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(drouss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDrouss() throws Exception {
        int databaseSizeBeforeUpdate = droussRepository.findAll().size();
        drouss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroussMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(drouss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDrouss() throws Exception {
        int databaseSizeBeforeUpdate = droussRepository.findAll().size();
        drouss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroussMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(drouss)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDroussWithPatch() throws Exception {
        // Initialize the database
        droussRepository.saveAndFlush(drouss);

        int databaseSizeBeforeUpdate = droussRepository.findAll().size();

        // Update the drouss using partial update
        Drouss partialUpdatedDrouss = new Drouss();
        partialUpdatedDrouss.setId(drouss.getId());

        partialUpdatedDrouss.nombre(UPDATED_NOMBRE);

        restDroussMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrouss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDrouss))
            )
            .andExpect(status().isOk());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
        Drouss testDrouss = droussList.get(droussList.size() - 1);
        assertThat(testDrouss.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void fullUpdateDroussWithPatch() throws Exception {
        // Initialize the database
        droussRepository.saveAndFlush(drouss);

        int databaseSizeBeforeUpdate = droussRepository.findAll().size();

        // Update the drouss using partial update
        Drouss partialUpdatedDrouss = new Drouss();
        partialUpdatedDrouss.setId(drouss.getId());

        partialUpdatedDrouss.nombre(UPDATED_NOMBRE);

        restDroussMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrouss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDrouss))
            )
            .andExpect(status().isOk());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
        Drouss testDrouss = droussList.get(droussList.size() - 1);
        assertThat(testDrouss.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingDrouss() throws Exception {
        int databaseSizeBeforeUpdate = droussRepository.findAll().size();
        drouss.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroussMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, drouss.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(drouss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDrouss() throws Exception {
        int databaseSizeBeforeUpdate = droussRepository.findAll().size();
        drouss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroussMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(drouss))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDrouss() throws Exception {
        int databaseSizeBeforeUpdate = droussRepository.findAll().size();
        drouss.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroussMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(drouss)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drouss in the database
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDrouss() throws Exception {
        // Initialize the database
        droussRepository.saveAndFlush(drouss);

        int databaseSizeBeforeDelete = droussRepository.findAll().size();

        // Delete the drouss
        restDroussMockMvc
            .perform(delete(ENTITY_API_URL_ID, drouss.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Drouss> droussList = droussRepository.findAll();
        assertThat(droussList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
