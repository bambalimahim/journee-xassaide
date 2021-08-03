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
import touba.xassaide.domain.Daara;
import touba.xassaide.repository.DaaraRepository;

/**
 * Integration tests for the {@link DaaraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DaaraResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/daaras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DaaraRepository daaraRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDaaraMockMvc;

    private Daara daara;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Daara createEntity(EntityManager em) {
        Daara daara = new Daara().nom(DEFAULT_NOM);
        return daara;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Daara createUpdatedEntity(EntityManager em) {
        Daara daara = new Daara().nom(UPDATED_NOM);
        return daara;
    }

    @BeforeEach
    public void initTest() {
        daara = createEntity(em);
    }

    @Test
    @Transactional
    void createDaara() throws Exception {
        int databaseSizeBeforeCreate = daaraRepository.findAll().size();
        // Create the Daara
        restDaaraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daara)))
            .andExpect(status().isCreated());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeCreate + 1);
        Daara testDaara = daaraList.get(daaraList.size() - 1);
        assertThat(testDaara.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createDaaraWithExistingId() throws Exception {
        // Create the Daara with an existing ID
        daara.setId(1L);

        int databaseSizeBeforeCreate = daaraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDaaraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daara)))
            .andExpect(status().isBadRequest());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = daaraRepository.findAll().size();
        // set the field null
        daara.setNom(null);

        // Create the Daara, which fails.

        restDaaraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daara)))
            .andExpect(status().isBadRequest());

        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDaaras() throws Exception {
        // Initialize the database
        daaraRepository.saveAndFlush(daara);

        // Get all the daaraList
        restDaaraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(daara.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getDaara() throws Exception {
        // Initialize the database
        daaraRepository.saveAndFlush(daara);

        // Get the daara
        restDaaraMockMvc
            .perform(get(ENTITY_API_URL_ID, daara.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(daara.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getNonExistingDaara() throws Exception {
        // Get the daara
        restDaaraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDaara() throws Exception {
        // Initialize the database
        daaraRepository.saveAndFlush(daara);

        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();

        // Update the daara
        Daara updatedDaara = daaraRepository.findById(daara.getId()).get();
        // Disconnect from session so that the updates on updatedDaara are not directly saved in db
        em.detach(updatedDaara);
        updatedDaara.nom(UPDATED_NOM);

        restDaaraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDaara.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDaara))
            )
            .andExpect(status().isOk());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
        Daara testDaara = daaraList.get(daaraList.size() - 1);
        assertThat(testDaara.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingDaara() throws Exception {
        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();
        daara.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDaaraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, daara.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(daara))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDaara() throws Exception {
        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();
        daara.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaaraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(daara))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDaara() throws Exception {
        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();
        daara.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaaraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daara)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDaaraWithPatch() throws Exception {
        // Initialize the database
        daaraRepository.saveAndFlush(daara);

        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();

        // Update the daara using partial update
        Daara partialUpdatedDaara = new Daara();
        partialUpdatedDaara.setId(daara.getId());

        restDaaraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDaara.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDaara))
            )
            .andExpect(status().isOk());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
        Daara testDaara = daaraList.get(daaraList.size() - 1);
        assertThat(testDaara.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void fullUpdateDaaraWithPatch() throws Exception {
        // Initialize the database
        daaraRepository.saveAndFlush(daara);

        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();

        // Update the daara using partial update
        Daara partialUpdatedDaara = new Daara();
        partialUpdatedDaara.setId(daara.getId());

        partialUpdatedDaara.nom(UPDATED_NOM);

        restDaaraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDaara.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDaara))
            )
            .andExpect(status().isOk());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
        Daara testDaara = daaraList.get(daaraList.size() - 1);
        assertThat(testDaara.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingDaara() throws Exception {
        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();
        daara.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDaaraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, daara.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(daara))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDaara() throws Exception {
        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();
        daara.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaaraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(daara))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDaara() throws Exception {
        int databaseSizeBeforeUpdate = daaraRepository.findAll().size();
        daara.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaaraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(daara)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Daara in the database
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDaara() throws Exception {
        // Initialize the database
        daaraRepository.saveAndFlush(daara);

        int databaseSizeBeforeDelete = daaraRepository.findAll().size();

        // Delete the daara
        restDaaraMockMvc
            .perform(delete(ENTITY_API_URL_ID, daara.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Daara> daaraList = daaraRepository.findAll();
        assertThat(daaraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
