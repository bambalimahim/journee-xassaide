package touba.xassaide.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import touba.xassaide.web.rest.TestUtil;

class DroussTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Drouss.class);
        Drouss drouss1 = new Drouss();
        drouss1.setId(1L);
        Drouss drouss2 = new Drouss();
        drouss2.setId(drouss1.getId());
        assertThat(drouss1).isEqualTo(drouss2);
        drouss2.setId(2L);
        assertThat(drouss1).isNotEqualTo(drouss2);
        drouss1.setId(null);
        assertThat(drouss1).isNotEqualTo(drouss2);
    }
}
