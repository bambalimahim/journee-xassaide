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
import touba.xassaide.domain.Cuisine;
import touba.xassaide.repository.CuisineRepository;

/**
 * Integration tests for the {@link CuisineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuisineResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITE = 1;
    private static final Integer UPDATED_CAPACITE = 2;

    private static final String ENTITY_API_URL = "/api/cuisines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuisineMockMvc;

    private Cuisine cuisine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuisine createEntity(EntityManager em) {
        Cuisine cuisine = new Cuisine().nom(DEFAULT_NOM).capacite(DEFAULT_CAPACITE);
        return cuisine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuisine createUpdatedEntity(EntityManager em) {
        Cuisine cuisine = new Cuisine().nom(UPDATED_NOM).capacite(UPDATED_CAPACITE);
        return cuisine;
    }

    @BeforeEach
    public void initTest() {
        cuisine = createEntity(em);
    }

    @Test
    @Transactional
    void createCuisine() throws Exception {
        int databaseSizeBeforeCreate = cuisineRepository.findAll().size();
        // Create the Cuisine
        restCuisineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuisine)))
            .andExpect(status().isCreated());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeCreate + 1);
        Cuisine testCuisine = cuisineList.get(cuisineList.size() - 1);
        assertThat(testCuisine.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCuisine.getCapacite()).isEqualTo(DEFAULT_CAPACITE);
    }

    @Test
    @Transactional
    void createCuisineWithExistingId() throws Exception {
        // Create the Cuisine with an existing ID
        cuisine.setId(1L);

        int databaseSizeBeforeCreate = cuisineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuisineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuisine)))
            .andExpect(status().isBadRequest());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuisineRepository.findAll().size();
        // set the field null
        cuisine.setNom(null);

        // Create the Cuisine, which fails.

        restCuisineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuisine)))
            .andExpect(status().isBadRequest());

        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCuisines() throws Exception {
        // Initialize the database
        cuisineRepository.saveAndFlush(cuisine);

        // Get all the cuisineList
        restCuisineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuisine.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)));
    }

    @Test
    @Transactional
    void getCuisine() throws Exception {
        // Initialize the database
        cuisineRepository.saveAndFlush(cuisine);

        // Get the cuisine
        restCuisineMockMvc
            .perform(get(ENTITY_API_URL_ID, cuisine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuisine.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE));
    }

    @Test
    @Transactional
    void getNonExistingCuisine() throws Exception {
        // Get the cuisine
        restCuisineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCuisine() throws Exception {
        // Initialize the database
        cuisineRepository.saveAndFlush(cuisine);

        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();

        // Update the cuisine
        Cuisine updatedCuisine = cuisineRepository.findById(cuisine.getId()).get();
        // Disconnect from session so that the updates on updatedCuisine are not directly saved in db
        em.detach(updatedCuisine);
        updatedCuisine.nom(UPDATED_NOM).capacite(UPDATED_CAPACITE);

        restCuisineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCuisine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCuisine))
            )
            .andExpect(status().isOk());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
        Cuisine testCuisine = cuisineList.get(cuisineList.size() - 1);
        assertThat(testCuisine.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCuisine.getCapacite()).isEqualTo(UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void putNonExistingCuisine() throws Exception {
        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();
        cuisine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuisineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuisine.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuisine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuisine() throws Exception {
        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();
        cuisine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuisine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuisine() throws Exception {
        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();
        cuisine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuisine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuisineWithPatch() throws Exception {
        // Initialize the database
        cuisineRepository.saveAndFlush(cuisine);

        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();

        // Update the cuisine using partial update
        Cuisine partialUpdatedCuisine = new Cuisine();
        partialUpdatedCuisine.setId(cuisine.getId());

        partialUpdatedCuisine.capacite(UPDATED_CAPACITE);

        restCuisineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuisine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuisine))
            )
            .andExpect(status().isOk());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
        Cuisine testCuisine = cuisineList.get(cuisineList.size() - 1);
        assertThat(testCuisine.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCuisine.getCapacite()).isEqualTo(UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void fullUpdateCuisineWithPatch() throws Exception {
        // Initialize the database
        cuisineRepository.saveAndFlush(cuisine);

        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();

        // Update the cuisine using partial update
        Cuisine partialUpdatedCuisine = new Cuisine();
        partialUpdatedCuisine.setId(cuisine.getId());

        partialUpdatedCuisine.nom(UPDATED_NOM).capacite(UPDATED_CAPACITE);

        restCuisineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuisine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuisine))
            )
            .andExpect(status().isOk());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
        Cuisine testCuisine = cuisineList.get(cuisineList.size() - 1);
        assertThat(testCuisine.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCuisine.getCapacite()).isEqualTo(UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void patchNonExistingCuisine() throws Exception {
        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();
        cuisine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuisineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuisine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuisine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuisine() throws Exception {
        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();
        cuisine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuisine))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuisine() throws Exception {
        int databaseSizeBeforeUpdate = cuisineRepository.findAll().size();
        cuisine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuisineMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cuisine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuisine in the database
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuisine() throws Exception {
        // Initialize the database
        cuisineRepository.saveAndFlush(cuisine);

        int databaseSizeBeforeDelete = cuisineRepository.findAll().size();

        // Delete the cuisine
        restCuisineMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuisine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cuisine> cuisineList = cuisineRepository.findAll();
        assertThat(cuisineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
