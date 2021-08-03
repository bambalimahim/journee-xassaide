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
import touba.xassaide.domain.Kourel;
import touba.xassaide.repository.KourelRepository;

/**
 * Integration tests for the {@link KourelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KourelResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kourels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KourelRepository kourelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKourelMockMvc;

    private Kourel kourel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kourel createEntity(EntityManager em) {
        Kourel kourel = new Kourel().nom(DEFAULT_NOM);
        return kourel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kourel createUpdatedEntity(EntityManager em) {
        Kourel kourel = new Kourel().nom(UPDATED_NOM);
        return kourel;
    }

    @BeforeEach
    public void initTest() {
        kourel = createEntity(em);
    }

    @Test
    @Transactional
    void createKourel() throws Exception {
        int databaseSizeBeforeCreate = kourelRepository.findAll().size();
        // Create the Kourel
        restKourelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kourel)))
            .andExpect(status().isCreated());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeCreate + 1);
        Kourel testKourel = kourelList.get(kourelList.size() - 1);
        assertThat(testKourel.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createKourelWithExistingId() throws Exception {
        // Create the Kourel with an existing ID
        kourel.setId(1L);

        int databaseSizeBeforeCreate = kourelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKourelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kourel)))
            .andExpect(status().isBadRequest());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = kourelRepository.findAll().size();
        // set the field null
        kourel.setNom(null);

        // Create the Kourel, which fails.

        restKourelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kourel)))
            .andExpect(status().isBadRequest());

        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKourels() throws Exception {
        // Initialize the database
        kourelRepository.saveAndFlush(kourel);

        // Get all the kourelList
        restKourelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kourel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getKourel() throws Exception {
        // Initialize the database
        kourelRepository.saveAndFlush(kourel);

        // Get the kourel
        restKourelMockMvc
            .perform(get(ENTITY_API_URL_ID, kourel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kourel.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingKourel() throws Exception {
        // Get the kourel
        restKourelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKourel() throws Exception {
        // Initialize the database
        kourelRepository.saveAndFlush(kourel);

        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();

        // Update the kourel
        Kourel updatedKourel = kourelRepository.findById(kourel.getId()).get();
        // Disconnect from session so that the updates on updatedKourel are not directly saved in db
        em.detach(updatedKourel);
        updatedKourel.nom(UPDATED_NOM);

        restKourelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKourel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKourel))
            )
            .andExpect(status().isOk());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
        Kourel testKourel = kourelList.get(kourelList.size() - 1);
        assertThat(testKourel.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingKourel() throws Exception {
        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();
        kourel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKourelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kourel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kourel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKourel() throws Exception {
        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();
        kourel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKourelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kourel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKourel() throws Exception {
        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();
        kourel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKourelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kourel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKourelWithPatch() throws Exception {
        // Initialize the database
        kourelRepository.saveAndFlush(kourel);

        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();

        // Update the kourel using partial update
        Kourel partialUpdatedKourel = new Kourel();
        partialUpdatedKourel.setId(kourel.getId());

        restKourelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKourel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKourel))
            )
            .andExpect(status().isOk());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
        Kourel testKourel = kourelList.get(kourelList.size() - 1);
        assertThat(testKourel.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateKourelWithPatch() throws Exception {
        // Initialize the database
        kourelRepository.saveAndFlush(kourel);

        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();

        // Update the kourel using partial update
        Kourel partialUpdatedKourel = new Kourel();
        partialUpdatedKourel.setId(kourel.getId());

        partialUpdatedKourel.nom(UPDATED_NOM);

        restKourelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKourel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKourel))
            )
            .andExpect(status().isOk());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
        Kourel testKourel = kourelList.get(kourelList.size() - 1);
        assertThat(testKourel.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingKourel() throws Exception {
        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();
        kourel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKourelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kourel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kourel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKourel() throws Exception {
        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();
        kourel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKourelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kourel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKourel() throws Exception {
        int databaseSizeBeforeUpdate = kourelRepository.findAll().size();
        kourel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKourelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(kourel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kourel in the database
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKourel() throws Exception {
        // Initialize the database
        kourelRepository.saveAndFlush(kourel);

        int databaseSizeBeforeDelete = kourelRepository.findAll().size();

        // Delete the kourel
        restKourelMockMvc
            .perform(delete(ENTITY_API_URL_ID, kourel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kourel> kourelList = kourelRepository.findAll();
        assertThat(kourelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
