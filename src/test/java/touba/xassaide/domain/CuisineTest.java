package touba.xassaide.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import touba.xassaide.web.rest.TestUtil;

class CuisineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cuisine.class);
        Cuisine cuisine1 = new Cuisine();
        cuisine1.setId(1L);
        Cuisine cuisine2 = new Cuisine();
        cuisine2.setId(cuisine1.getId());
        assertThat(cuisine1).isEqualTo(cuisine2);
        cuisine2.setId(2L);
        assertThat(cuisine1).isNotEqualTo(cuisine2);
        cuisine1.setId(null);
        assertThat(cuisine1).isNotEqualTo(cuisine2);
    }
}
