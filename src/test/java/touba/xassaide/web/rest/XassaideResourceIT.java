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
import touba.xassaide.domain.Xassaide;
import touba.xassaide.repository.XassaideRepository;

/**
 * Integration tests for the {@link XassaideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class XassaideResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/xassaides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private XassaideRepository xassaideRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restXassaideMockMvc;

    private Xassaide xassaide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Xassaide createEntity(EntityManager em) {
        Xassaide xassaide = new Xassaide().nom(DEFAULT_NOM);
        return xassaide;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Xassaide createUpdatedEntity(EntityManager em) {
        Xassaide xassaide = new Xassaide().nom(UPDATED_NOM);
        return xassaide;
    }

    @BeforeEach
    public void initTest() {
        xassaide = createEntity(em);
    }

    @Test
    @Transactional
    void createXassaide() throws Exception {
        int databaseSizeBeforeCreate = xassaideRepository.findAll().size();
        // Create the Xassaide
        restXassaideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xassaide)))
            .andExpect(status().isCreated());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeCreate + 1);
        Xassaide testXassaide = xassaideList.get(xassaideList.size() - 1);
        assertThat(testXassaide.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createXassaideWithExistingId() throws Exception {
        // Create the Xassaide with an existing ID
        xassaide.setId(1L);

        int databaseSizeBeforeCreate = xassaideRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restXassaideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xassaide)))
            .andExpect(status().isBadRequest());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = xassaideRepository.findAll().size();
        // set the field null
        xassaide.setNom(null);

        // Create the Xassaide, which fails.

        restXassaideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xassaide)))
            .andExpect(status().isBadRequest());

        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllXassaides() throws Exception {
        // Initialize the database
        xassaideRepository.saveAndFlush(xassaide);

        // Get all the xassaideList
        restXassaideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(xassaide.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getXassaide() throws Exception {
        // Initialize the database
        xassaideRepository.saveAndFlush(xassaide);

        // Get the xassaide
        restXassaideMockMvc
            .perform(get(ENTITY_API_URL_ID, xassaide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(xassaide.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingXassaide() throws Exception {
        // Get the xassaide
        restXassaideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewXassaide() throws Exception {
        // Initialize the database
        xassaideRepository.saveAndFlush(xassaide);

        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();

        // Update the xassaide
        Xassaide updatedXassaide = xassaideRepository.findById(xassaide.getId()).get();
        // Disconnect from session so that the updates on updatedXassaide are not directly saved in db
        em.detach(updatedXassaide);
        updatedXassaide.nom(UPDATED_NOM);

        restXassaideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedXassaide.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedXassaide))
            )
            .andExpect(status().isOk());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
        Xassaide testXassaide = xassaideList.get(xassaideList.size() - 1);
        assertThat(testXassaide.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingXassaide() throws Exception {
        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();
        xassaide.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXassaideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, xassaide.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xassaide))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchXassaide() throws Exception {
        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();
        xassaide.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXassaideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(xassaide))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamXassaide() throws Exception {
        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();
        xassaide.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXassaideMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(xassaide)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateXassaideWithPatch() throws Exception {
        // Initialize the database
        xassaideRepository.saveAndFlush(xassaide);

        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();

        // Update the xassaide using partial update
        Xassaide partialUpdatedXassaide = new Xassaide();
        partialUpdatedXassaide.setId(xassaide.getId());

        restXassaideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXassaide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXassaide))
            )
            .andExpect(status().isOk());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
        Xassaide testXassaide = xassaideList.get(xassaideList.size() - 1);
        assertThat(testXassaide.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateXassaideWithPatch() throws Exception {
        // Initialize the database
        xassaideRepository.saveAndFlush(xassaide);

        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();

        // Update the xassaide using partial update
        Xassaide partialUpdatedXassaide = new Xassaide();
        partialUpdatedXassaide.setId(xassaide.getId());

        partialUpdatedXassaide.nom(UPDATED_NOM);

        restXassaideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedXassaide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedXassaide))
            )
            .andExpect(status().isOk());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
        Xassaide testXassaide = xassaideList.get(xassaideList.size() - 1);
        assertThat(testXassaide.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingXassaide() throws Exception {
        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();
        xassaide.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restXassaideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, xassaide.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xassaide))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchXassaide() throws Exception {
        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();
        xassaide.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXassaideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(xassaide))
            )
            .andExpect(status().isBadRequest());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamXassaide() throws Exception {
        int databaseSizeBeforeUpdate = xassaideRepository.findAll().size();
        xassaide.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restXassaideMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(xassaide)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Xassaide in the database
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteXassaide() throws Exception {
        // Initialize the database
        xassaideRepository.saveAndFlush(xassaide);

        int databaseSizeBeforeDelete = xassaideRepository.findAll().size();

        // Delete the xassaide
        restXassaideMockMvc
            .perform(delete(ENTITY_API_URL_ID, xassaide.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Xassaide> xassaideList = xassaideRepository.findAll();
        assertThat(xassaideList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
