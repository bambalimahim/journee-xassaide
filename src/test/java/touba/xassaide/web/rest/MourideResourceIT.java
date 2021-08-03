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
import touba.xassaide.domain.Mouride;
import touba.xassaide.repository.MourideRepository;

/**
 * Integration tests for the {@link MourideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MourideResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mourides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MourideRepository mourideRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMourideMockMvc;

    private Mouride mouride;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mouride createEntity(EntityManager em) {
        Mouride mouride = new Mouride()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .matricule(DEFAULT_MATRICULE);
        return mouride;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mouride createUpdatedEntity(EntityManager em) {
        Mouride mouride = new Mouride()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .matricule(UPDATED_MATRICULE);
        return mouride;
    }

    @BeforeEach
    public void initTest() {
        mouride = createEntity(em);
    }

    @Test
    @Transactional
    void createMouride() throws Exception {
        int databaseSizeBeforeCreate = mourideRepository.findAll().size();
        // Create the Mouride
        restMourideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isCreated());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeCreate + 1);
        Mouride testMouride = mourideList.get(mourideList.size() - 1);
        assertThat(testMouride.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testMouride.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testMouride.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMouride.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testMouride.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
    }

    @Test
    @Transactional
    void createMourideWithExistingId() throws Exception {
        // Create the Mouride with an existing ID
        mouride.setId(1L);

        int databaseSizeBeforeCreate = mourideRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMourideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isBadRequest());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = mourideRepository.findAll().size();
        // set the field null
        mouride.setPrenom(null);

        // Create the Mouride, which fails.

        restMourideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isBadRequest());

        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = mourideRepository.findAll().size();
        // set the field null
        mouride.setNom(null);

        // Create the Mouride, which fails.

        restMourideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isBadRequest());

        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = mourideRepository.findAll().size();
        // set the field null
        mouride.setTelephone(null);

        // Create the Mouride, which fails.

        restMourideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isBadRequest());

        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMourides() throws Exception {
        // Initialize the database
        mourideRepository.saveAndFlush(mouride);

        // Get all the mourideList
        restMourideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mouride.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)));
    }

    @Test
    @Transactional
    void getMouride() throws Exception {
        // Initialize the database
        mourideRepository.saveAndFlush(mouride);

        // Get the mouride
        restMourideMockMvc
            .perform(get(ENTITY_API_URL_ID, mouride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mouride.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE));
    }

    @Test
    @Transactional
    void getNonExistingMouride() throws Exception {
        // Get the mouride
        restMourideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMouride() throws Exception {
        // Initialize the database
        mourideRepository.saveAndFlush(mouride);

        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();

        // Update the mouride
        Mouride updatedMouride = mourideRepository.findById(mouride.getId()).get();
        // Disconnect from session so that the updates on updatedMouride are not directly saved in db
        em.detach(updatedMouride);
        updatedMouride
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .matricule(UPDATED_MATRICULE);

        restMourideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMouride.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMouride))
            )
            .andExpect(status().isOk());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
        Mouride testMouride = mourideList.get(mourideList.size() - 1);
        assertThat(testMouride.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testMouride.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMouride.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMouride.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testMouride.getMatricule()).isEqualTo(UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void putNonExistingMouride() throws Exception {
        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();
        mouride.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMourideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mouride.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mouride))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMouride() throws Exception {
        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();
        mouride.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMourideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mouride))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMouride() throws Exception {
        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();
        mouride.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMourideMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMourideWithPatch() throws Exception {
        // Initialize the database
        mourideRepository.saveAndFlush(mouride);

        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();

        // Update the mouride using partial update
        Mouride partialUpdatedMouride = new Mouride();
        partialUpdatedMouride.setId(mouride.getId());

        partialUpdatedMouride.nom(UPDATED_NOM).email(UPDATED_EMAIL).telephone(UPDATED_TELEPHONE);

        restMourideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMouride.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMouride))
            )
            .andExpect(status().isOk());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
        Mouride testMouride = mourideList.get(mourideList.size() - 1);
        assertThat(testMouride.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testMouride.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMouride.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMouride.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testMouride.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
    }

    @Test
    @Transactional
    void fullUpdateMourideWithPatch() throws Exception {
        // Initialize the database
        mourideRepository.saveAndFlush(mouride);

        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();

        // Update the mouride using partial update
        Mouride partialUpdatedMouride = new Mouride();
        partialUpdatedMouride.setId(mouride.getId());

        partialUpdatedMouride
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .matricule(UPDATED_MATRICULE);

        restMourideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMouride.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMouride))
            )
            .andExpect(status().isOk());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
        Mouride testMouride = mourideList.get(mourideList.size() - 1);
        assertThat(testMouride.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testMouride.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testMouride.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMouride.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testMouride.getMatricule()).isEqualTo(UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void patchNonExistingMouride() throws Exception {
        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();
        mouride.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMourideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mouride.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mouride))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMouride() throws Exception {
        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();
        mouride.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMourideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mouride))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMouride() throws Exception {
        int databaseSizeBeforeUpdate = mourideRepository.findAll().size();
        mouride.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMourideMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mouride)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mouride in the database
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMouride() throws Exception {
        // Initialize the database
        mourideRepository.saveAndFlush(mouride);

        int databaseSizeBeforeDelete = mourideRepository.findAll().size();

        // Delete the mouride
        restMourideMockMvc
            .perform(delete(ENTITY_API_URL_ID, mouride.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mouride> mourideList = mourideRepository.findAll();
        assertThat(mourideList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
