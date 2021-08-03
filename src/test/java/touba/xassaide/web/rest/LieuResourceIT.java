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
import touba.xassaide.domain.Lieu;
import touba.xassaide.repository.LieuRepository;

/**
 * Integration tests for the {@link LieuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LieuResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lieus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LieuRepository lieuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLieuMockMvc;

    private Lieu lieu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lieu createEntity(EntityManager em) {
        Lieu lieu = new Lieu().nom(DEFAULT_NOM);
        return lieu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lieu createUpdatedEntity(EntityManager em) {
        Lieu lieu = new Lieu().nom(UPDATED_NOM);
        return lieu;
    }

    @BeforeEach
    public void initTest() {
        lieu = createEntity(em);
    }

    @Test
    @Transactional
    void createLieu() throws Exception {
        int databaseSizeBeforeCreate = lieuRepository.findAll().size();
        // Create the Lieu
        restLieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieu)))
            .andExpect(status().isCreated());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeCreate + 1);
        Lieu testLieu = lieuList.get(lieuList.size() - 1);
        assertThat(testLieu.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createLieuWithExistingId() throws Exception {
        // Create the Lieu with an existing ID
        lieu.setId(1L);

        int databaseSizeBeforeCreate = lieuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieu)))
            .andExpect(status().isBadRequest());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = lieuRepository.findAll().size();
        // set the field null
        lieu.setNom(null);

        // Create the Lieu, which fails.

        restLieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieu)))
            .andExpect(status().isBadRequest());

        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLieus() throws Exception {
        // Initialize the database
        lieuRepository.saveAndFlush(lieu);

        // Get all the lieuList
        restLieuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lieu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getLieu() throws Exception {
        // Initialize the database
        lieuRepository.saveAndFlush(lieu);

        // Get the lieu
        restLieuMockMvc
            .perform(get(ENTITY_API_URL_ID, lieu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lieu.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingLieu() throws Exception {
        // Get the lieu
        restLieuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLieu() throws Exception {
        // Initialize the database
        lieuRepository.saveAndFlush(lieu);

        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();

        // Update the lieu
        Lieu updatedLieu = lieuRepository.findById(lieu.getId()).get();
        // Disconnect from session so that the updates on updatedLieu are not directly saved in db
        em.detach(updatedLieu);
        updatedLieu.nom(UPDATED_NOM);

        restLieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLieu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLieu))
            )
            .andExpect(status().isOk());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
        Lieu testLieu = lieuList.get(lieuList.size() - 1);
        assertThat(testLieu.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingLieu() throws Exception {
        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();
        lieu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lieu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLieu() throws Exception {
        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();
        lieu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLieu() throws Exception {
        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();
        lieu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLieuWithPatch() throws Exception {
        // Initialize the database
        lieuRepository.saveAndFlush(lieu);

        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();

        // Update the lieu using partial update
        Lieu partialUpdatedLieu = new Lieu();
        partialUpdatedLieu.setId(lieu.getId());

        partialUpdatedLieu.nom(UPDATED_NOM);

        restLieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLieu))
            )
            .andExpect(status().isOk());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
        Lieu testLieu = lieuList.get(lieuList.size() - 1);
        assertThat(testLieu.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateLieuWithPatch() throws Exception {
        // Initialize the database
        lieuRepository.saveAndFlush(lieu);

        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();

        // Update the lieu using partial update
        Lieu partialUpdatedLieu = new Lieu();
        partialUpdatedLieu.setId(lieu.getId());

        partialUpdatedLieu.nom(UPDATED_NOM);

        restLieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLieu))
            )
            .andExpect(status().isOk());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
        Lieu testLieu = lieuList.get(lieuList.size() - 1);
        assertThat(testLieu.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingLieu() throws Exception {
        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();
        lieu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLieu() throws Exception {
        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();
        lieu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLieu() throws Exception {
        int databaseSizeBeforeUpdate = lieuRepository.findAll().size();
        lieu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lieu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lieu in the database
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLieu() throws Exception {
        // Initialize the database
        lieuRepository.saveAndFlush(lieu);

        int databaseSizeBeforeDelete = lieuRepository.findAll().size();

        // Delete the lieu
        restLieuMockMvc
            .perform(delete(ENTITY_API_URL_ID, lieu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lieu> lieuList = lieuRepository.findAll();
        assertThat(lieuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
